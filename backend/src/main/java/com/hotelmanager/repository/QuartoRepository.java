package com.hotelmanager.repository;

import com.hotelmanager.enums.StatusQuarto;
import com.hotelmanager.model.Quarto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuartoRepository extends JpaRepository<Quarto, Long> {

    List<Quarto> findByAtivoTrue();

    List<Quarto> findByAtivoTrueAndStatus(StatusQuarto status);

    long countByAtivoTrue();

    long countByAtivoTrueAndStatus(StatusQuarto status);
}
