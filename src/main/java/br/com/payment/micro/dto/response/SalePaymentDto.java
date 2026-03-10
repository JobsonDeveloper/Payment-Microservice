package br.com.payment.micro.dto.response;

import br.com.payment.micro.dto.request.SaleInfoDto;

public record SalePaymentDto(
        String message,
        SaleInfoDto sale
) {
}
