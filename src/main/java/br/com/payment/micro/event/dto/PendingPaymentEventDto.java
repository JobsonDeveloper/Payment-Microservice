package br.com.payment.micro.event.dto;

import br.com.payment.micro.domain.Status;

public record PendingPaymentEventDto(
        String saleId,
        Status status
) {
}
