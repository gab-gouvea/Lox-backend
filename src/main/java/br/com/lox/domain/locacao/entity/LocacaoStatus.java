package br.com.lox.domain.locacao.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LocacaoStatus {
    ativa("ativa"),
    encerrada("encerrada"),
    cancelada("cancelada");

    private final String value;

    LocacaoStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LocacaoStatus fromValue(String value) {
        for (LocacaoStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + value);
    }
}
