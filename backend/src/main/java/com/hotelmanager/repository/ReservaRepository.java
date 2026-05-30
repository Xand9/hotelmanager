package com.hotelmanager.repository;

import com.hotelmanager.enums.StatusReserva;
import com.hotelmanager.model.Reserva;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByQuartoIdAndStatus(Long quartoId, StatusReserva status);

    List<Reserva> findByQuartoIdAndStatusIn(Long quartoId, List<StatusReserva> status);

    List<Reserva> findByHospedeIdAndStatusIn(Long hospedeId, List<StatusReserva> status);

    List<Reserva> findByStatusOrderByDataEntradaAsc(StatusReserva status);

    @Query("""
            select reserva
            from Reserva reserva
            where reserva.quarto.id = :quartoId
              and reserva.status in :status
              and (:reservaIgnoradaId is null or reserva.id <> :reservaIgnoradaId)
              and :dataEntrada < reserva.dataSaida
              and :dataSaida > reserva.dataEntrada
            """)
    List<Reserva> buscarReservasConflitantes(
            @Param("quartoId") Long quartoId,
            @Param("status") List<StatusReserva> status,
            @Param("dataEntrada") LocalDate dataEntrada,
            @Param("dataSaida") LocalDate dataSaida,
            @Param("reservaIgnoradaId") Long reservaIgnoradaId
    );

    long countByStatus(StatusReserva status);

    long countByStatusAndDataEntrada(StatusReserva status, LocalDate dataEntrada);

    long countByStatusAndDataSaida(StatusReserva status, LocalDate dataSaida);
}
