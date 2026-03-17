package br.com.lox.domain.property.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PropertyType {
    apartamento("apartamento"),
    casa("casa"),
    cobertura("cobertura"),
    studio("studio"),
    chale("chalé"),
    flat("flat"),
    outro("outro");

    private final String value;

    PropertyType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PropertyType fromValue(String value) {
        for (PropertyType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de propriedade inválido: " + value);
    }
}
