package br.com.lox.domain.property.dto;

import java.util.List;
import java.util.Map;

public record UpdatePropertyData(
        String title,
        String address,
        Map<Integer , String> photos,
        List<String> componentsId,
        String notes,
        String conciergeCode,
        String doorCode
) {
}
