package com.hotelmanager.service;

import com.hotelmanager.enums.StatusReserva;
import com.hotelmanager.exception.RecursoNaoEncontradoException;
import com.hotelmanager.exception.RegraDeNegocioException;
import com.hotelmanager.model.Hospede;
import com.hotelmanager.model.Reserva;
import com.hotelmanager.repository.HospedeRepository;
import com.hotelmanager.repository.ReservaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service//Essa classe é um componente de serviço. Pode criar um objeto dela e gerenciar para mim
@Transactional//Se um método alterar dados no banco, faça isso dentro de uma transação segura.
public class HospedeService {

    private final HospedeRepository hospedeRepository;
    private final ReservaRepository reservaRepository;

    public HospedeService(HospedeRepository hospedeRepository, ReservaRepository reservaRepository) {
        this.hospedeRepository = hospedeRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<Hospede> listarTodos() {
        return hospedeRepository.findByAtivoTrue();
    }

    public Hospede buscarPorId(Long id) {
        return hospedeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Hospede nao encontrado."));
    }

    public Hospede cadastrar(Hospede hospede) {
        hospede.setAtivo(true);
        hospede.setDataCadastro(LocalDateTime.now());
        return hospedeRepository.save(hospede);
    }

    public Hospede atualizar(Long id, Hospede hospedeAtualizado) {
        Hospede hospede = buscarPorId(id);

        hospede.setNome(hospedeAtualizado.getNome());
        hospede.setDocumento(hospedeAtualizado.getDocumento());
        hospede.setTelefone(hospedeAtualizado.getTelefone());
        hospede.setEmail(hospedeAtualizado.getEmail());
        hospede.setEndereco(hospedeAtualizado.getEndereco());
        hospede.setObservacoes(hospedeAtualizado.getObservacoes());

        return hospedeRepository.save(hospede);
    }

    public void inativar(Long id) {
        Hospede hospede = buscarPorId(id);
        validarReservaAtiva(hospede);

        hospede.setAtivo(false);
        hospedeRepository.save(hospede);
    }

    private void validarReservaAtiva(Hospede hospede) {
        List<Reserva> reservasAtivas = reservaRepository.findByHospedeIdAndStatusIn(
                hospede.getId(),
                List.of(StatusReserva.RESERVADA, StatusReserva.CHECKIN_REALIZADO)
        );

        if (reservasAtivas.isEmpty()) {
            return;
        }

        Reserva reserva = reservasAtivas.get(0);
        throw new RegraDeNegocioException(
                "Nao e possivel excluir este hospede porque ele possui uma reserva ativa no quarto "
                        + reserva.getQuarto().getNumero()
                        + ". Cancele a reserva ou finalize o check-out antes de excluir o hospede."
        );
    }
}
