package com.hotelmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hospedes")
public class Hospede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do hospede e obrigatorio.")
    private String nome;

    @NotBlank(message = "O documento do hospede e obrigatorio.")
    private String documento;

    private String telefone;

    @Email(message = "Informe um e-mail valido.")
    private String email;

    private String endereco;

    private LocalDateTime dataCadastro;

    private String observacoes;

    private Boolean ativo;
}
