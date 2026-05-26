package com.hotelmanager.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservaRequestDTO(
        @NotNull(message = "O id do hospede e obrigatorio.")
        Long hospedeId,

        @NotNull(message = "O id do quarto e obrigatorio.")
        Long quartoId,

        @NotNull(message = "A data de entrada e obrigatoria.")
        LocalDate dataEntrada,

        @NotNull(message = "A data de saida e obrigatoria.")
        LocalDate dataSaida,

        String observacoes
) {
}
