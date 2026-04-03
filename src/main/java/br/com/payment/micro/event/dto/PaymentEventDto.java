package br.com.payment.micro.event.dto;

import br.com.payment.micro.domain.Status;

public record PaymentEventDto(
        String paymentId,
        String clientId,
        String clientCpf,
        String saleId,
        Status status
) {
}
