package com.hotelmanager.repository;

import com.hotelmanager.model.LimpezaDiaria;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LimpezaDiariaRepository extends JpaRepository<LimpezaDiaria, Long> {

    Optional<LimpezaDiaria> findByReservaIdAndDataLimpeza(Long reservaId, LocalDate dataLimpeza);
}
