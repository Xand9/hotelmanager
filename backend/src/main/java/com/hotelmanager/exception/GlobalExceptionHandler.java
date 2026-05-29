package com.hotelmanager.exception;

import com.hotelmanager.dto.ErroResposta;

import java.time.LocalDateTime;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice//Esta classe vai tratar erros globalmente nos controllers REST.
public class GlobalExceptionHandler {//Tratador Global de Exceções

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException exception) {//retorna resposta HTTP 
        return criarResposta(HttpStatus.NOT_FOUND, List.of(exception.getMessage()));
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResposta> tratarRegraDeNegocio(RegraDeNegocioException exception) {
        return criarResposta(HttpStatus.BAD_REQUEST, List.of(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)//Falha de validação de campo
    public ResponseEntity<ErroResposta> tratarValidacao(MethodArgumentNotValidException exception) {
        List<String> mensagens = exception.getBindingResult()//armazena lista de erro dentro de "mensagens"
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())//transforma cada erro em uma mensagem de texto
                .toList();

        return criarResposta(HttpStatus.BAD_REQUEST, mensagens);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> tratarJsonInvalido(HttpMessageNotReadableException exception) {
        return criarResposta(HttpStatus.BAD_REQUEST, List.of("JSON invalido ou valor de enum nao reconhecido."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> tratarErroInesperado(Exception exception) {
        return criarResposta(HttpStatus.INTERNAL_SERVER_ERROR, List.of("Erro interno no servidor."));
    }

    private ResponseEntity<ErroResposta> criarResposta(HttpStatus status, List<String> mensagens) {
        ErroResposta resposta = new ErroResposta(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagens
        );

        return ResponseEntity.status(status).body(resposta);
    }
}
