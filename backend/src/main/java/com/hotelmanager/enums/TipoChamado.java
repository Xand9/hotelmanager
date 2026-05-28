package com.hotelmanager.enums;

public enum TipoChamado {
    LIMPEZA("Limpeza"),
    MANUTENCAO("Manutenção"),
    SOLICITACAO_DO_HOSPEDE("Solicitação do hóspede"),
    OUTRO("Outro");

    private final String descricao;

    TipoChamado(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
