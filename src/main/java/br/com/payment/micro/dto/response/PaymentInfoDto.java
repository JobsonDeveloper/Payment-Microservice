package br.com.payment.micro.dto.response;

import br.com.payment.micro.domain.Payment;

public record PaymentInfoDto(
        String message,
        Payment payment
) {
}
