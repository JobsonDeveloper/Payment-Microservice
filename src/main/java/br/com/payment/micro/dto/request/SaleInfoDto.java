package br.com.payment.micro.dto.request;

import br.com.payment.micro.dto.request.user.SaleUserDataDto;
import br.com.payment.micro.dto.request.product.ProductIdentifiersDto;

import java.util.List;

public record SaleInfoDto(
        String id,
        SaleUserDataDto user,
        List<ProductIdentifiersDto> items,
        Double totalValue
) {
}
