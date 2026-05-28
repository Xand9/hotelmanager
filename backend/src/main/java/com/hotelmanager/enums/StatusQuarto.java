package com.hotelmanager.enums;

public enum StatusQuarto {
    DISPONIVEL("Disponível"),
    OCUPADO("Ocupado"),
    EM_LIMPEZA("Em limpeza"),
    EM_MANUTENCAO("Em manutenção");

    private final String descricao;

    StatusQuarto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
