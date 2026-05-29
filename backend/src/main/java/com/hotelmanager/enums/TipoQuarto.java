package com.hotelmanager.enums;

public enum TipoQuarto {
    SOLTEIRO("Solteiro"),
    CASAL("Casal"),
    LUXO("Luxo"),
    SUITE("Suíte"),
    FAMILIA("Família"),
    EXECUTIVO("Executivo"),
    PRESIDENCIAL("Presidencial");

    private final String descricao;//descição do Enum

    TipoQuarto(String descricao) {//Construtor do enum
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;//Quando alguém pedir a descrição, entregue o texto guardado em descricao.
    }
}
