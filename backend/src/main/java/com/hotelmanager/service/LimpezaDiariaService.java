package com.hotelmanager.service;

import com.hotelmanager.dto.LimpezaDiariaQuartoDTO;
import com.hotelmanager.enums.StatusReserva;
import com.hotelmanager.exception.RegraDeNegocioException;
import com.hotelmanager.exception.RecursoNaoEncontradoException;
import com.hotelmanager.model.LimpezaDiaria;
import com.hotelmanager.model.Reserva;
import com.hotelmanager.repository.LimpezaDiariaRepository;
import com.hotelmanager.repository.ReservaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LimpezaDiariaService {

    private final ReservaRepository reservaRepository;
    private final LimpezaDiariaRepository limpezaDiariaRepository;

    public LimpezaDiariaService(
            ReservaRepository reservaRepository,
            LimpezaDiariaRepository limpezaDiariaRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.limpezaDiariaRepository = limpezaDiariaRepository;
    }

    public Map<Long, LimpezaDiariaQuartoDTO> buscarLimpezasDeHojePorQuarto() {
        LocalDate hoje = LocalDate.now();
        Map<Long, LimpezaDiariaQuartoDTO> limpezasPorQuarto = new LinkedHashMap<>();

        reservaRepository.findByStatusOrderByDataEntradaAsc(StatusReserva.CHECKIN_REALIZADO)
                .stream()
                .filter(reserva -> deveTerLimpezaDiaria(reserva, hoje))
                .forEach(reserva -> {
                    boolean realizada = limpezaDiariaRepository
                            .findByReservaIdAndDataLimpeza(reserva.getId(), hoje)
                            .map(LimpezaDiaria::getRealizada)
                            .orElse(false);

                    limpezasPorQuarto.put(
                            reserva.getQuarto().getId(),
                            new LimpezaDiariaQuartoDTO(
                                    reserva.getId(),
                                    reserva.getQuarto().getId(),
                                    hoje,
                                    realizada
                            )
                    );
                });

        return limpezasPorQuarto;
    }

    public void marcarLimpezaDeHojeComoRealizada(Long quartoId, Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva nao encontrada."));

        if (!reserva.getQuarto().getId().equals(quartoId)) {
            throw new RegraDeNegocioException("A reserva informada nao pertence a este quarto.");
        }

        LocalDate hoje = LocalDate.now();

        if (!deveTerLimpezaDiaria(reserva, hoje)) {
            throw new RegraDeNegocioException("Hoje nao existe limpeza diaria programada para esta hospedagem.");
        }

        LimpezaDiaria limpezaDiaria = limpezaDiariaRepository
                .findByReservaIdAndDataLimpeza(reservaId, hoje)
                .orElseGet(() -> criarLimpezaDiaria(reserva, hoje));

        limpezaDiaria.setRealizada(true);
        limpezaDiaria.setDataRealizacao(LocalDateTime.now());

        limpezaDiariaRepository.save(limpezaDiaria);
    }

    private LimpezaDiaria criarLimpezaDiaria(Reserva reserva, LocalDate dataLimpeza) {
        LimpezaDiaria limpezaDiaria = new LimpezaDiaria();
        limpezaDiaria.setReserva(reserva);
        limpezaDiaria.setQuarto(reserva.getQuarto());
        limpezaDiaria.setDataLimpeza(dataLimpeza);
        limpezaDiaria.setRealizada(false);
        return limpezaDiaria;
    }

    private boolean deveTerLimpezaDiaria(Reserva reserva, LocalDate data) {
        if (reserva.getStatus() != StatusReserva.CHECKIN_REALIZADO) {
            return false;
        }

        if (reserva.getDataEntrada() == null || reserva.getDataSaida() == null) {
            return false;
        }

        if (!data.isAfter(reserva.getDataEntrada()) || !data.isBefore(reserva.getDataSaida())) {
            return false;
        }

        long diasDesdeEntrada = ChronoUnit.DAYS.between(reserva.getDataEntrada(), data);
        return diasDesdeEntrada % 2 == 1;
    }
}
