package com.hotelmanager.service;

import com.hotelmanager.enums.StatusQuarto;
import com.hotelmanager.exception.RecursoNaoEncontradoException;
import com.hotelmanager.model.Quarto;
import com.hotelmanager.repository.QuartoRepository;
import java.util.List;
import org.springframework.stereotype.Service;//"Spring essa classe é um service."
import org.springframework.transaction.annotation.Transactional;//Controla transações com o banco.

@Service//Diz ao Spring que essa classe pertence à camada de serviço.
@Transactional
public class QuartoService {

    private final QuartoRepository quartoRepository;//Conversar com o banco.

    public QuartoService(QuartoRepository quartoRepository) {//Construtor
        this.quartoRepository = quartoRepository;
    }

    public List<Quarto> listarTodos() {//Listar quartos ATIVOS
        return quartoRepository.findByAtivoTrue();
    }

    public List<Quarto> listarDisponiveis() {//Listar quartos ATIVOS DISPONIVEL
        return quartoRepository.findByAtivoTrueAndStatus(StatusQuarto.DISPONIVEL);
    }

    public Quarto buscarPorId(Long id) {
        return quartoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quarto nao encontrado."));
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
        quarto.setStatus(status);
        return quartoRepository.save(quarto);
    }

    public void inativar(Long id) {
        Quarto quarto = buscarPorId(id);
        quarto.setAtivo(false);
        quartoRepository.save(quarto);
    }
}
