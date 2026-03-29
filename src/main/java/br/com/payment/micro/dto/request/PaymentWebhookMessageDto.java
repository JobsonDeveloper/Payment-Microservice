package br.com.payment.micro.dto.request;

import jakarta.validation.constraints.NotNull;

public record PaymentWebhookMessageDto(
        @NotNull PaymentWebhookDataDto data
) {
}
