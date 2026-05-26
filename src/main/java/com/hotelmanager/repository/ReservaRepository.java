package com.hotelmanager.repository;

import com.hotelmanager.enums.StatusReserva;
import com.hotelmanager.model.Reserva;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByQuartoIdAndStatus(Long quartoId, StatusReserva status);

    long countByStatus(StatusReserva status);

    long countByStatusAndDataEntrada(StatusReserva status, LocalDate dataEntrada);

    long countByStatusAndDataSaida(StatusReserva status, LocalDate dataSaida);
}
