package br.com.payment.micro.controller;

import br.com.payment.micro.domain.Payment;
import br.com.payment.micro.dto.request.GetPaymentLinkDto;
import br.com.payment.micro.dto.request.PaymentWebhookMessageDto;
import br.com.payment.micro.dto.response.PaymentCompletedDto;
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
                                    examples = {
                                            @ExampleObject(
                                                    name = "Invalid fields",
                                                    value = "{ \"error\": \"Validation failed\", \"errors\": \"[...]\" }"
                                            ),
                                            @ExampleObject(
                                                    name = "Data incompatible with linked database",
                                                    value = "{ \"status\": \"BAD_REQUEST\", \"message\": \"The payment details was not provided correctly!\" }"
                                            )
                                    }
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
                            responseCode = "409",
                            description = "Amount to be paid is incompatible",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"status\": \"CONFLICT\", \"message\": \"The amount to be paid is inconsistent!\" }"
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
                            description = "Services unavailable!",
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
                                            ),
                                            @ExampleObject(
                                                    name = "Communication error with the payment intermediary",
                                                    value = "{ \"status\": \"BAD_GATEWAY\", \"message\": \"We were unable to generate the payment link!\" }"
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
        String cpf = getPaymentLinkDto.client().cpf();
        String clientFirstName = getPaymentLinkDto.client().firstName();
        String clientLastName = getPaymentLinkDto.client().lastName();
        String clientEmail = getPaymentLinkDto.client().email();

        String paymentLink = iPaymentService.getPaymentLink(
                value,
                saleId,
                cpf,
                clientFirstName,
                clientLastName,
                clientEmail
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(new PaymentLinkGeneratedDto(
                "Payment link returned successfully!",
                saleId,
                paymentLink
        ));
    }

    @PostMapping("/api/payment/completed")
    @Operation(
            summary = "Mark payment as completed",
            description = "Route to mark a payment as completed",
            tags = {"Payment"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment registered successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentCompletedDto.class)
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
                            description = "Services unavailable!",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Service unavailable!",
                                                    value = "{ \"status\": \"BAD_GATEWAY\", \"message\": \"Service 'Sale Microservice' is unavailable!\" }"
                                            )
                                    }
                            )
                    )
            }
    )
    public void registerPayment(@Valid @RequestBody PaymentWebhookMessageDto dto) {
        String externalId = dto.data().id();
        iPaymentService.paymentCompleted(externalId);
    }
}
