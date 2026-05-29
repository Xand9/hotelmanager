package com.hotelmanager.enums;

public enum StatusQuarto {
    DISPONIVEL("Disponível"),
    OCUPADO("Ocupado"),
    EM_LIMPEZA("Em limpeza"),
    EM_MANUTENCAO("Em manutenção");

    private final String descricao;//Descrição enum

    StatusQuarto(String descricao) {//Construtor do enum
        this.descricao = descricao;
    }

    public String getDescricao() {//Quando alguém pedir a descrição, entregue o texto guardado em descricao.
        return descricao;
    }
}
