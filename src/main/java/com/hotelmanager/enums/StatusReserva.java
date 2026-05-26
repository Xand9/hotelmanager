package com.hotelmanager.enums;

public enum StatusReserva {
    RESERVADA("Reservada"),
    CHECKIN_REALIZADO("Check-in realizado"),
    CHECKOUT_REALIZADO("Check-out realizado"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusReserva(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
