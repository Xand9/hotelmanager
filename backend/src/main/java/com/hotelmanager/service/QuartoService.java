package com.hotelmanager.service;

import com.hotelmanager.enums.StatusChamado;
import com.hotelmanager.enums.StatusQuarto;
import com.hotelmanager.enums.StatusReserva;
import com.hotelmanager.enums.TipoChamado;
import com.hotelmanager.exception.RegraDeNegocioException;
import com.hotelmanager.exception.RecursoNaoEncontradoException;
import com.hotelmanager.model.ChamadoInterno;
import com.hotelmanager.model.Quarto;
import com.hotelmanager.model.Reserva;
import com.hotelmanager.repository.ChamadoInternoRepository;
import com.hotelmanager.repository.QuartoRepository;
import com.hotelmanager.repository.ReservaRepository;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;//"Spring essa classe é um service."
import org.springframework.transaction.annotation.Transactional;//Controla transações com o banco.

@Service//Diz ao Spring que essa classe pertence à camada de serviço.
@Transactional
public class QuartoService {

    private final QuartoRepository quartoRepository;//Conversar com o banco.
    private final ChamadoInternoRepository chamadoInternoRepository;
    private final ReservaRepository reservaRepository;

    public QuartoService(
            QuartoRepository quartoRepository,
            ChamadoInternoRepository chamadoInternoRepository,
            ReservaRepository reservaRepository
    ) {//Construtor
        this.quartoRepository = quartoRepository;
        this.chamadoInternoRepository = chamadoInternoRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<Quarto> listarTodos() {//Listar quartos ATIVOS
        return quartoRepository.findByAtivoTrue();
    }

    public List<Quarto> listarDisponiveis() {//Listar quartos ATIVOS DISPONIVEL
        return quartoRepository.findByAtivoTrueAndStatus(StatusQuarto.DISPONIVEL);
    }

    public Map<Long, Reserva> buscarReservasAtivasPorQuarto() {
        Map<Long, Reserva> reservasPorQuarto = new LinkedHashMap<>();

        reservaRepository.findByStatusOrderByDataEntradaAsc(StatusReserva.RESERVADA)
                .forEach(reserva -> reservasPorQuarto.putIfAbsent(reserva.getQuarto().getId(), reserva));

        reservaRepository.findByStatusOrderByDataEntradaAsc(StatusReserva.CHECKIN_REALIZADO)
                .forEach(reserva -> reservasPorQuarto.put(reserva.getQuarto().getId(), reserva));

        return reservasPorQuarto;
    }

    public Quarto buscarPorId(Long id) {
        return quartoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quarto nao encontrado."));
    }

    public Quarto buscarParaEdicao(Long id) {
        Quarto quarto = buscarPorId(id);
        validarChamadoAtivo(quarto);
        validarReservaAtiva(quarto);
        return quarto;
    }

    public Quarto cadastrar(Quarto quarto) {
        if (quarto.getStatus() == null) {
            quarto.setStatus(StatusQuarto.DISPONIVEL);
        }

        quarto.setAtivo(true);
        return quartoRepository.save(quarto);
    }

    public Quarto atualizar(Long id, Quarto quartoAtualizado) {
        Quarto quarto = buscarPorId(id);
        validarChamadoAtivo(quarto);
        validarReservaAtiva(quarto);

        quarto.setNumero(quartoAtualizado.getNumero());
        quarto.setTipo(quartoAtualizado.getTipo());
        quarto.setCapacidade(quartoAtualizado.getCapacidade());
        quarto.setValorDiaria(quartoAtualizado.getValorDiaria());
        quarto.setObservacoes(quartoAtualizado.getObservacoes());

        if (quartoAtualizado.getStatus() != null) {
            quarto.setStatus(quartoAtualizado.getStatus());
        }

        return quartoRepository.save(quarto);
    }

    public Quarto alterarStatus(Long id, StatusQuarto status) {
        Quarto quarto = buscarPorId(id);
        validarChamadoAtivo(quarto);
        validarReservaAtiva(quarto);
        quarto.setStatus(status);
        return quartoRepository.save(quarto);
    }

    public void inativar(Long id) {
        Quarto quarto = buscarPorId(id);
        quarto.setAtivo(false);
        quartoRepository.save(quarto);
    }

    private void validarChamadoAtivo(Quarto quarto) {
        List<ChamadoInterno> chamadosAtivos = chamadoInternoRepository.findByQuartoIdAndStatusInAndTipoIn(
                quarto.getId(),
                List.of(StatusChamado.ABERTO, StatusChamado.EM_ANDAMENTO),
                List.of(TipoChamado.LIMPEZA, TipoChamado.MANUTENCAO, TipoChamado.SOLICITACAO_DO_HOSPEDE)
        );

        if (chamadosAtivos.isEmpty()) {
            return;
        }

        ChamadoInterno chamado = chamadosAtivos.get(0);
        throw new RegraDeNegocioException(
                "Existe um chamado aberto para este quarto. Tipo do chamado: "
                        + chamado.getTipo().getDescricao()
                        + ". Conclua ou exclua o chamado para liberar a alteracao do quarto."
        );
    }

    private void validarReservaAtiva(Quarto quarto) {
        List<Reserva> reservasAtivas = reservaRepository.findByQuartoIdAndStatus(quarto.getId(), StatusReserva.CHECKIN_REALIZADO);

        if (reservasAtivas.isEmpty()) {
            reservasAtivas = reservaRepository.findByQuartoIdAndStatus(quarto.getId(), StatusReserva.RESERVADA);
        }

        if (reservasAtivas.isEmpty()) {
            return;
        }

        Reserva reserva = reservasAtivas.get(0);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean quartoOcupado = reserva.getStatus() == StatusReserva.CHECKIN_REALIZADO;

        throw new RegraDeNegocioException(
                (quartoOcupado ? "Existe um check-in realizado para este quarto. Hospede: " : "Existe uma reserva ativa para este quarto. Reservado para: ")
                        + reserva.getHospede().getNome()
                        + ". Periodo: "
                        + reserva.getDataEntrada().format(formato)
                        + " a "
                        + reserva.getDataSaida().format(formato)
                        + (quartoOcupado
                        ? ". Use a aba Reservas para fazer check-out e liberar o quarto."
                        : ". Use a aba Reservas para fazer check-in, cancelar ou editar a reserva.")
        );
    }
}
