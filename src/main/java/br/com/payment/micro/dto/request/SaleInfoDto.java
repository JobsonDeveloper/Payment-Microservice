package br.com.payment.micro.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public record SaleInfoDto(
        String id,
        ClientDataDto client,
        Double totalValue
) {
}
