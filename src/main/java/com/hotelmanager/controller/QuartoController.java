package com.hotelmanager.controller;

import com.hotelmanager.dto.AtualizarStatusQuartoDTO;
import com.hotelmanager.model.Quarto;
import com.hotelmanager.service.QuartoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quartos")
public class QuartoController {

    private final QuartoService quartoService;

    public QuartoController(QuartoService quartoService) {
        this.quartoService = quartoService;
    }

    @GetMapping
    public List<Quarto> listarTodos() {
        return quartoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Quarto buscarPorId(@PathVariable Long id) {
        return quartoService.buscarPorId(id);
    }

    @GetMapping("/disponiveis")
    public List<Quarto> listarDisponiveis() {
        return quartoService.listarDisponiveis();
    }

    @PostMapping
    public ResponseEntity<Quarto> cadastrar(@Valid @RequestBody Quarto quarto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quartoService.cadastrar(quarto));
    }

    @PutMapping("/{id}")
    public Quarto atualizar(@PathVariable Long id, @Valid @RequestBody Quarto quarto) {
        return quartoService.atualizar(id, quarto);
    }

    @PatchMapping("/{id}/status")
    public Quarto alterarStatus(@PathVariable Long id, @Valid @RequestBody AtualizarStatusQuartoDTO dto) {
        return quartoService.alterarStatus(id, dto.status());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        quartoService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
