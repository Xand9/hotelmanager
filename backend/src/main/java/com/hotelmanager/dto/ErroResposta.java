        package com.hotelmanager.dto;

        import java.time.LocalDateTime;
        import java.util.List;

        public record ErroResposta(//guardar dados de forma simples "record"
                LocalDateTime dataHora,
                Integer status,//Guarda o código HTTP do erro.
                String erro,
                List<String> mensagens//lista de mensagens explicando o erro
        ) {
        }
