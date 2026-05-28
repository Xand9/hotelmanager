package com.hotelmanager.repository;

import com.hotelmanager.model.Hospede;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospedeRepository extends JpaRepository<Hospede, Long> {

    List<Hospede> findByAtivoTrue();
}
