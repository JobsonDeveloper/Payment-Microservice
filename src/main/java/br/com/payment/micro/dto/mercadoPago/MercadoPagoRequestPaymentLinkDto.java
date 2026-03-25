package br.com.payment.micro.dto.mercadoPago;

import br.com.payment.micro.dto.request.product.ProductIdentifiersDto;

import java.util.List;

public record MercadoPagoRequestPaymentLinkDto(
        String saleId,
        List<ProductIdentifiersDto> items,
        PayerDto payerDto,
        PaymentConfigDto paymentConfig
) {
}
