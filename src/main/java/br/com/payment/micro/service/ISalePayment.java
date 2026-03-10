package br.com.payment.micro.service;

import br.com.payment.micro.dto.response.SalePaymentDto;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "sale-microservice",
        url = "${sale.micro.url}"
)
public interface ISalePayment {

    @GetMapping("/api/sale/{id}/info")
    SalePaymentDto getSaleInfo(
            @Parameter(description = "Sale id", required = true)
            @PathVariable String id
    );
}
