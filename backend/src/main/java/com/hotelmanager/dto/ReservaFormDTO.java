package com.hotelmanager.dto;//dados vindos da tela/formulário.

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ReservaFormDTO(
        Long id,

        @NotNull(message = "O hospede e obrigatorio.")
        Long hospedeId,

        @NotNull(message = "O quarto e obrigatorio.")
        Long quartoId,

        @NotNull(message = "A data de entrada e obrigatoria.")
        LocalDate dataEntrada,

        @NotNull(message = "A data de saida e obrigatoria.")
        LocalDate dataSaida,

        @Size(max = 210, message = "As observacoes devem ter no maximo 210 caracteres.")
        String observacoes
) {
}
