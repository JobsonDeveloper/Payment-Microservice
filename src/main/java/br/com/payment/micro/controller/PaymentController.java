package br.com.payment.micro.controller;

import br.com.payment.micro.dto.request.GetPaymentLinkDto;
import br.com.payment.micro.dto.response.PaymentLinkGeneratedDto;
import br.com.payment.micro.service.IPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Payment", description = "Payment operations")
public class PaymentController {
    private final IPaymentService iPaymentService;

    public PaymentController(IPaymentService iPaymentService) {
        this.iPaymentService = iPaymentService;
    }

    @PostMapping("/api/payment/link")
    @Operation(
            summary = "Create a payment link",
            description = "Create a link to make a payment",
            tags = {"Payment"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Payment link returned successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentLinkGeneratedDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"error\": \"Validation failed\", \"errors\": \"[...]\" }"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sale not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"status\": \"NOT_FOUND\", \"message\": \"Sale not found!\" }"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "It was not possible to get sale info!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"status\": \"INTERNAL_SERVER_ERROR\", \"message\": \"It was not possible to get sale info!\" }"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "502",
                            description = "",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Service unavailable!",
                                                    value = "{ \"status\": \"BAD_GATEWAY\", \"message\": \"Service 'Sale Microservice' is unavailable!\" }"
                                            ),
                                            @ExampleObject(
                                                    name = "Error when try to get payment link!",
                                                    value = "{ \"status\": \"BAD_GATEWAY\", \"message\": \"It was not possible to get payment link!\" }"
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<PaymentLinkGeneratedDto> getPaymentLink(
            @Valid @RequestBody
            GetPaymentLinkDto getPaymentLinkDto
    ) {
        String saleId = getPaymentLinkDto.saleId();
        Double value = getPaymentLinkDto.value();

        String paymentLink = iPaymentService.getPaymentLink(value, saleId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new PaymentLinkGeneratedDto(
                "Payment link returned successfully!",
                saleId,
                paymentLink
        ));
    }
}
