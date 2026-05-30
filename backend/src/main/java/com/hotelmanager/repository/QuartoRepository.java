package com.hotelmanager.repository;// Buscar/Salvar no banco

import com.hotelmanager.enums.StatusQuarto;//buscar/contar quartos por status
import com.hotelmanager.model.Quarto;//Importa a entidade Quarto
import java.util.List;//Lista de objetos: List<Quarto>
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuartoRepository extends JpaRepository<Quarto, Long> {

    List<Quarto> findByAtivoTrue();//ativo = true

    List<Quarto> findByAtivoTrueAndStatus(StatusQuarto status);

    boolean existsByNumeroAndAtivoTrue(String numero);

    boolean existsByNumeroAndAtivoTrueAndIdNot(String numero, Long id);

    long countByAtivoTrue();

    long countByAtivoTrueAndStatus(StatusQuarto status);
}
