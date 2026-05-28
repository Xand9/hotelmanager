package com.hotelmanager.dto;

import com.hotelmanager.enums.StatusChamado;
import com.hotelmanager.enums.TipoChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChamadoInternoFormDTO(
        Long id,

        @NotNull(message = "O quarto do chamado e obrigatorio.")
        Long quartoId,

        @NotNull(message = "O tipo do chamado e obrigatorio.")
        TipoChamado tipo,

        @NotBlank(message = "A descricao do chamado e obrigatoria.")
        String descricao,

        StatusChamado status
) {
}
