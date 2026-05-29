package com.hotelmanager.service;

import com.hotelmanager.dto.ChamadoInternoFormDTO;
import com.hotelmanager.enums.StatusChamado;
import com.hotelmanager.enums.StatusQuarto;
import com.hotelmanager.exception.RecursoNaoEncontradoException;
import com.hotelmanager.model.ChamadoInterno;
import com.hotelmanager.model.Quarto;
import com.hotelmanager.repository.ChamadoInternoRepository;
import com.hotelmanager.repository.QuartoRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChamadoInternoService {

    private final ChamadoInternoRepository chamadoInternoRepository;//salvar, buscar, listar e excluir chamados
    private final QuartoRepository quartoRepository;//buscar o quarto relacionado e alterar o status dele.

    public ChamadoInternoService(//injeção de dependência
            ChamadoInternoRepository chamadoInternoRepository,
            QuartoRepository quartoRepository
    ) {
        this.chamadoInternoRepository = chamadoInternoRepository;
        this.quartoRepository = quartoRepository;
    }

    public List<ChamadoInterno> listarTodos() {
        return chamadoInternoRepository.findAll();
    }

    public ChamadoInterno buscarPorId(Long id) {
        return chamadoInternoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado interno nao encontrado."));
    }

    public ChamadoInterno salvar(ChamadoInternoFormDTO dto) {
        Quarto quarto = quartoRepository.findById(dto.quartoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quarto nao encontrado."));

        ChamadoInterno chamado = dto.id() == null ? new ChamadoInterno() : buscarPorId(dto.id());
        chamado.setQuarto(quarto);
        chamado.setTipo(dto.tipo());
        chamado.setDescricao(dto.descricao());
        chamado.setStatus(dto.status() == null ? StatusChamado.ABERTO : dto.status());

        if (chamado.getDataAbertura() == null) {
            chamado.setDataAbertura(LocalDateTime.now());
        }

        atualizarConclusao(chamado);
        atualizarStatusDoQuarto(chamado);

        return chamadoInternoRepository.save(chamado);
    }

    public ChamadoInterno alterarStatus(Long id, StatusChamado status) {
        ChamadoInterno chamado = buscarPorId(id);
        chamado.setStatus(status);
        atualizarConclusao(chamado);
        atualizarStatusDoQuarto(chamado);
        return chamadoInternoRepository.save(chamado);
    }

    public void excluir(Long id) {
        chamadoInternoRepository.delete(buscarPorId(id));
    }

    public ChamadoInternoFormDTO criarFormulario() {
        return new ChamadoInternoFormDTO(null, null, null, null, StatusChamado.ABERTO);
    }

    public ChamadoInternoFormDTO criarFormulario(ChamadoInterno chamado) {
        return new ChamadoInternoFormDTO(
                chamado.getId(),
                chamado.getQuarto().getId(),
                chamado.getTipo(),
                chamado.getDescricao(),
                chamado.getStatus()
        );
    }

    private void atualizarConclusao(ChamadoInterno chamado) {
        if (chamado.getStatus() == StatusChamado.CONCLUIDO && chamado.getDataConclusao() == null) {
            chamado.setDataConclusao(LocalDateTime.now());
        }

        if (chamado.getStatus() != StatusChamado.CONCLUIDO) {
            chamado.setDataConclusao(null);
        }
    }

    private void atualizarStatusDoQuarto(ChamadoInterno chamado) {
        if (chamado.getStatus() == StatusChamado.CONCLUIDO || chamado.getStatus() == StatusChamado.CANCELADO) {
            return;
        }

        Quarto quarto = chamado.getQuarto();
        switch (chamado.getTipo()) {
            case LIMPEZA -> quarto.setStatus(StatusQuarto.EM_LIMPEZA);
            case MANUTENCAO -> quarto.setStatus(StatusQuarto.EM_MANUTENCAO);
            default -> {
            }
        }
        quartoRepository.save(quarto);
    }
}
