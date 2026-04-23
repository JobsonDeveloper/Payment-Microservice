package br.com.payment.micro.event.dto;

import br.com.payment.micro.domain.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CancelPaymentEventDto(
        String id,
        String userId,
        Status status
) {
}
