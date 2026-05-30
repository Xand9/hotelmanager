package com.hotelmanager.repository;

import com.hotelmanager.enums.StatusChamado;
import com.hotelmanager.enums.TipoChamado;
import com.hotelmanager.model.ChamadoInterno;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoInternoRepository extends JpaRepository<ChamadoInterno, Long> {

    long countByStatus(StatusChamado status);

    List<ChamadoInterno> findByDataAberturaBetween(LocalDateTime inicio, LocalDateTime fim);

    List<ChamadoInterno> findByQuartoIdAndStatusInAndTipoIn(
            Long quartoId,
            Collection<StatusChamado> status,
            Collection<TipoChamado> tipos
    );
}
