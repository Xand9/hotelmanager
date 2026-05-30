package com.hotelmanager.dto;

import java.time.LocalDate;

public record LimpezaDiariaQuartoDTO(
        Long reservaId,
        Long quartoId,
        LocalDate dataLimpeza,
        Boolean realizada
) {
}
