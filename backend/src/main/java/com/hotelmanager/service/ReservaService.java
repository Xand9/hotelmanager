package com.hotelmanager.service;

import com.hotelmanager.dto.ReservaRequestDTO;
import com.hotelmanager.enums.StatusChamado;
import com.hotelmanager.enums.StatusQuarto;
import com.hotelmanager.enums.StatusReserva;
import com.hotelmanager.enums.TipoChamado;
import com.hotelmanager.exception.RecursoNaoEncontradoException;
import com.hotelmanager.exception.RegraDeNegocioException;
import com.hotelmanager.model.ChamadoInterno;
import com.hotelmanager.model.Hospede;
import com.hotelmanager.model.Quarto;
import com.hotelmanager.model.Reserva;
import com.hotelmanager.repository.ChamadoInternoRepository;
import com.hotelmanager.repository.HospedeRepository;
import com.hotelmanager.repository.QuartoRepository;
import com.hotelmanager.repository.ReservaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;//Calcular diferença entre datas.
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservaService {

    private static final List<StatusReserva> STATUS_QUE_BLOQUEIAM_RESERVA = List.of(
            StatusReserva.RESERVADA,
            StatusReserva.CHECKIN_REALIZADO
    );

    private final ReservaRepository reservaRepository;
    private final HospedeRepository hospedeRepository;
    private final QuartoRepository quartoRepository;
    private final ChamadoInternoRepository chamadoInternoRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            HospedeRepository hospedeRepository,
            QuartoRepository quartoRepository,
            ChamadoInternoRepository chamadoInternoRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.hospedeRepository = hospedeRepository;
        this.quartoRepository = quartoRepository;
        this.chamadoInternoRepository = chamadoInternoRepository;
    }

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva nao encontrada."));
    }

    public Reserva cadastrarReserva(ReservaRequestDTO dto) {
        Hospede hospede = buscarHospedeAtivo(dto.hospedeId());
        Quarto quarto = buscarQuartoAtivo(dto.quartoId());

        validarReserva(quarto, dto.dataEntrada(), dto.dataSaida(), null);

        Reserva reserva = new Reserva();
        reserva.setHospede(hospede);
        reserva.setQuarto(quarto);
        reserva.setDataEntrada(dto.dataEntrada());
        reserva.setDataSaida(dto.dataSaida());
        reserva.setQuantidadeDiarias(calcularQuantidadeDiarias(dto.dataEntrada(), dto.dataSaida()));
        reserva.setValorTotal(calcularValorTotal(quarto, reserva.getQuantidadeDiarias()));
        reserva.setObservacoes(dto.observacoes());
        reserva.setStatus(StatusReserva.RESERVADA);
        reserva.setDataCriacao(LocalDateTime.now());

        return reservaRepository.save(reserva);
    }

    public Reserva atualizar(Long id, ReservaRequestDTO dto) {
        Reserva reserva = buscarPorId(id);
        //Só reservas ainda reservadas podem ser editadas.

        if (reserva.getStatus() != StatusReserva.RESERVADA) {
            throw new RegraDeNegocioException("Apenas reservas reservadas podem ser atualizadas.");
        }

        Hospede hospede = buscarHospedeAtivo(dto.hospedeId());
        Quarto quarto = buscarQuartoAtivo(dto.quartoId());

        validarReserva(quarto, dto.dataEntrada(), dto.dataSaida(), id);

        reserva.setHospede(hospede);
        reserva.setQuarto(quarto);
        reserva.setDataEntrada(dto.dataEntrada());
        reserva.setDataSaida(dto.dataSaida());
        reserva.setQuantidadeDiarias(calcularQuantidadeDiarias(dto.dataEntrada(), dto.dataSaida()));
        reserva.setValorTotal(calcularValorTotal(quarto, reserva.getQuantidadeDiarias()));
        reserva.setObservacoes(dto.observacoes());

        return reservaRepository.save(reserva);
    }

    public Reserva cancelar(Long id) {
        Reserva reserva = buscarPorId(id);
        reserva.setStatus(StatusReserva.CANCELADA);
        return reservaRepository.save(reserva);
    }

    public Reserva realizarCheckin(Long id) {
        Reserva reserva = buscarPorId(id);
        if (reserva.getStatus() != StatusReserva.RESERVADA) {
            throw new RegraDeNegocioException("Apenas reservas reservadas podem receber check-in.");
        }

        Quarto quarto = reserva.getQuarto();
        validarChamadoBloqueante(quarto);

        if (quarto.getStatus() == StatusQuarto.EM_MANUTENCAO) {
            throw new RegraDeNegocioException("Nao e possivel fazer check-in em quarto em manutencao.");
        }

        reserva.setStatus(StatusReserva.CHECKIN_REALIZADO);
        quarto.setStatus(StatusQuarto.OCUPADO);
        quartoRepository.save(quarto);
        return reservaRepository.save(reserva);
    }

    public Reserva realizarCheckout(Long id) {
        Reserva reserva = buscarPorId(id);
        if (reserva.getStatus() != StatusReserva.CHECKIN_REALIZADO) {
            throw new RegraDeNegocioException("Apenas reservas com check-in realizado podem receber check-out.");
        }

        Quarto quarto = reserva.getQuarto();
        reserva.setStatus(StatusReserva.CHECKOUT_REALIZADO);
        quarto.setStatus(StatusQuarto.EM_LIMPEZA);
        quartoRepository.save(quarto);
        return reservaRepository.save(reserva);
    }

    private Hospede buscarHospedeAtivo(Long id) {
        Hospede hospede = hospedeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Hospede nao encontrado."));

        if (!Boolean.TRUE.equals(hospede.getAtivo())) {
            throw new RegraDeNegocioException("Hospede inativo nao pode ser usado em nova reserva.");
        }

        return hospede;
    }

    private Quarto buscarQuartoAtivo(Long id) {
        Quarto quarto = quartoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quarto nao encontrado."));

        if (!Boolean.TRUE.equals(quarto.getAtivo())) {
            throw new RegraDeNegocioException("Quarto inativo nao pode ser usado em nova reserva.");
        }

        return quarto;
    }

    private void validarReserva(Quarto quarto, LocalDate dataEntrada, LocalDate dataSaida, Long reservaIgnoradaId) {
        if (dataEntrada == null || dataSaida == null) {
            throw new RegraDeNegocioException("As datas de entrada e saida sao obrigatorias.");
        }

        if (!dataSaida.isAfter(dataEntrada)) {
            throw new RegraDeNegocioException("A data de saida deve ser posterior a data de entrada.");
        }

        validarChamadoBloqueante(quarto);

        if (existeConflitoDeDatas(quarto.getId(), dataEntrada, dataSaida, reservaIgnoradaId)) {
            throw new RegraDeNegocioException("Ja existe reserva ativa para este quarto no periodo informado.");
        }
    }

    private void validarChamadoBloqueante(Quarto quarto) {
        List<ChamadoInterno> chamadosBloqueantes = chamadoInternoRepository.findByQuartoIdAndStatusInAndTipoIn(
                quarto.getId(),
                List.of(StatusChamado.ABERTO, StatusChamado.EM_ANDAMENTO),
                List.of(TipoChamado.LIMPEZA, TipoChamado.MANUTENCAO)
        );

        if (chamadosBloqueantes.isEmpty()) {
            return;
        }

        ChamadoInterno chamado = chamadosBloqueantes.get(0);
        throw new RegraDeNegocioException(
                "Nao e possivel reservar este quarto porque existe um chamado ativo de "
                        + chamado.getTipo().getDescricao()
                        + ". Conclua ou exclua o chamado antes de criar ou alterar a reserva."
        );
    }

    private boolean existeConflitoDeDatas(
            Long quartoId,
            LocalDate dataEntrada,
            LocalDate dataSaida,
            Long reservaIgnoradaId
    ) {
        return !reservaRepository.buscarReservasConflitantes(
                quartoId,
                STATUS_QUE_BLOQUEIAM_RESERVA,
                dataEntrada,
                dataSaida,
                reservaIgnoradaId
        ).isEmpty();
    }

    private Integer calcularQuantidadeDiarias(LocalDate dataEntrada, LocalDate dataSaida) {
        return Math.toIntExact(ChronoUnit.DAYS.between(dataEntrada, dataSaida));
    }

    private BigDecimal calcularValorTotal(Quarto quarto, Integer quantidadeDiarias) {
        return quarto.getValorDiaria().multiply(BigDecimal.valueOf(quantidadeDiarias));
    }
}
