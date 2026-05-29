package com.hotelmanager.repository;

import com.hotelmanager.model.Hospede;
import java.util.List;//List representa uma lista de objetos.
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospedeRepository extends JpaRepository<Hospede, Long> {

    List<Hospede> findByAtivoTrue();//Buscar uma lista de hóspedes onde o campo ativo seja true.
}
