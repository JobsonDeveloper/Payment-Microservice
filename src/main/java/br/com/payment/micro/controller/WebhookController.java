package br.com.payment.micro.controller;

import br.com.payment.micro.dto.request.PaymentWebhookMessageDto;
import br.com.payment.micro.dto.swagger.DefaultErrorResponseDto;
import br.com.payment.micro.dto.swagger.validation.fields.FieldsErrorDto;
import br.com.payment.micro.service.IPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Webhook", description = "Webhook routes")
public class WebhookController {
    private final IPaymentService iPaymentService;

    public WebhookController(IPaymentService iPaymentService) {
        this.iPaymentService = iPaymentService;
    }

    @PostMapping("/api/payment/events")
    @Operation(
            summary = "Payment events",
            description = "Receives payment events and processes them internally"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Event processed successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Inconsistent request fields",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FieldsErrorDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "503",
            description = "Service Unavailable",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
            )
    )
    public void registerPayment(@Valid @RequestBody PaymentWebhookMessageDto dto) {
        String externalId = dto.data().id();
        iPaymentService.paymentCompleted(externalId);
    }
}
