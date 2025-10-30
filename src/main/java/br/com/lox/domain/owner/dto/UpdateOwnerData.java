package br.com.lox.domain.owner.dto;

import java.util.List;

public record UpdateOwnerData(
        String email,
        String name,
        String phone,
        List<String> propertiesId
) {
}
