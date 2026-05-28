package com.hotelmanager.controller;

import com.hotelmanager.dto.ReservaRequestDTO;
import com.hotelmanager.model.Reserva;
import com.hotelmanager.service.ReservaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public List<Reserva> listarTodas() {
        return reservaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Reserva buscarPorId(@PathVariable Long id) {
        return reservaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Reserva> cadastrarReserva(@Valid @RequestBody ReservaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.cadastrarReserva(dto));
    }

    @PutMapping("/{id}")
    public Reserva atualizar(@PathVariable Long id, @Valid @RequestBody ReservaRequestDTO dto) {
        return reservaService.atualizar(id, dto);
    }

    @PatchMapping("/{id}/cancelar")
    public Reserva cancelar(@PathVariable Long id) {
        return reservaService.cancelar(id);
    }

    @PatchMapping("/{id}/checkin")
    public Reserva realizarCheckin(@PathVariable Long id) {
        return reservaService.realizarCheckin(id);
    }

    @PatchMapping("/{id}/checkout")
    public Reserva realizarCheckout(@PathVariable Long id) {
        return reservaService.realizarCheckout(id);
    }
}
