package br.com.payment.micro.dto.response;

public record PaymentLinkGeneratedDto(
        String message,
        String saleId,
        String link
) {
}
