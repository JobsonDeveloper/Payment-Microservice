package br.com.payment.micro.event.dto;

import br.com.payment.micro.domain.Status;

public record PaymentEventDto(
        String saleId,
        Status status
) {
}
