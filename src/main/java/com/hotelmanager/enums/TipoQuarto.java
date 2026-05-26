package com.hotelmanager.enums;

public enum TipoQuarto {
    SOLTEIRO("Solteiro"),
    CASAL("Casal"),
    LUXO("Luxo"),
    SUITE("Suíte"),
    FAMILIA("Família"),
    EXECUTIVO("Executivo"),
    PRESIDENCIAL("Presidencial");

    private final String descricao;

    TipoQuarto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
