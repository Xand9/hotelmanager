package com.hotelmanager.controller;

import com.hotelmanager.model.Hospede;
import com.hotelmanager.service.HospedeService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hospedes")
public class HospedeController {

    private final HospedeService hospedeService;

    public HospedeController(HospedeService hospedeService) {
        this.hospedeService = hospedeService;
    }

    @GetMapping
    public List<Hospede> listarTodos() {
        return hospedeService.listarTodos();
    }

    @GetMapping("/{id}")
    public Hospede buscarPorId(@PathVariable Long id) {
        return hospedeService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Hospede> cadastrar(@Valid @RequestBody Hospede hospede) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hospedeService.cadastrar(hospede));
    }

    @PutMapping("/{id}")
    public Hospede atualizar(@PathVariable Long id, @Valid @RequestBody Hospede hospede) {
        return hospedeService.atualizar(id, hospede);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        hospedeService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
