package br.com.payment.micro.dto.response;

import br.com.payment.micro.dto.request.SaleInfoDto;

public record GetSaleInfoDto(
        String message,
        SaleInfoDto sale
) {
}
