package br.com.payment.micro.dto.request;

import br.com.payment.micro.dto.request.client.SaleClientDataDto;
import br.com.payment.micro.dto.request.product.ProductIdentifiersDto;

import java.util.List;

public record SaleInfoDto(
        String id,
        SaleClientDataDto client,
        List<ProductIdentifiersDto> items,
        Double totalValue
) {
}
