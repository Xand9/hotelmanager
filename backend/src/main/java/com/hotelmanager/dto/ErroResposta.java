package com.hotelmanager.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResposta(
        LocalDateTime dataHora,
        Integer status,
        String erro,
        List<String> mensagens
) {
}
