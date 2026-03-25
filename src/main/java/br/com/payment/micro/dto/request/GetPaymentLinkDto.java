package br.com.payment.micro.dto.request;

import br.com.payment.micro.dto.request.client.ClientIdentifiersDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GetPaymentLinkDto(
        @NotNull(message = "Sale id is required!") @Size(min = 1, message = "Sale id must be valid!") String saleId,
        @NotNull(message = "The value to pay is required!") @DecimalMin(value = "0.00", message = "The value to pay mus be valid!") Double value,
        @Valid ClientIdentifiersDto client
) {
}
