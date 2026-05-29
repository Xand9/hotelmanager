package com.hotelmanager.exception;//sistema encontra o recurso, mas a ação não pode ser feita por causa de uma regra

public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
