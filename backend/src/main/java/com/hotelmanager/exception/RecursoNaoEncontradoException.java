package com.hotelmanager.exception;//avisos de erro do sistema!
//exception = pacote de exceções/erros personalizados
public class RecursoNaoEncontradoException extends RuntimeException {
    //Construtor()
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
//cria um erro personalizado para quando algo não existe.