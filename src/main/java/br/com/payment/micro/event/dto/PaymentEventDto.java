package br.com.payment.micro.event.dto;

import br.com.payment.micro.domain.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public record PaymentEventDto(
        String paymentId,
        String userId,
        String userCpf,
        String saleId,
        Status status
) {
}
