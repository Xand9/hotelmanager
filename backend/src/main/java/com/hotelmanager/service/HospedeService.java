package com.hotelmanager.service;

import com.hotelmanager.exception.RecursoNaoEncontradoException;
import com.hotelmanager.model.Hospede;
import com.hotelmanager.repository.HospedeRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HospedeService {

    private final HospedeRepository hospedeRepository;

    public HospedeService(HospedeRepository hospedeRepository) {
        this.hospedeRepository = hospedeRepository;
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
        hospede.setAtivo(false);
        hospedeRepository.save(hospede);
    }
}
