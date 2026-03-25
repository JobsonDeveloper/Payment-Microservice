package br.com.payment.micro.dto.request.product;

public record ProductIdentifiersDto(
        String id,
        String name,
        Long barCode,
        String description,
        Integer quantity,
        Double value
) {
}
