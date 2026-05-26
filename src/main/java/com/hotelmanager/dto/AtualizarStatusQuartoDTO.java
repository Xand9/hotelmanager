package com.hotelmanager.dto;

import com.hotelmanager.enums.StatusQuarto;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusQuartoDTO(
        @NotNull(message = "O status do quarto e obrigatorio.")
        StatusQuarto status
) {
}
