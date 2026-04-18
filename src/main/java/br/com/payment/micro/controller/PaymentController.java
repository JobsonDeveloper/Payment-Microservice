package br.com.payment.micro.controller;

import br.com.payment.micro.domain.Payment;
import br.com.payment.micro.dto.request.GetPaymentLinkDto;
import br.com.payment.micro.dto.response.PaymentInfoDto;
import br.com.payment.micro.dto.response.PaymentLinkGeneratedDto;
import br.com.payment.micro.dto.swagger.DefaultErrorResponseDto;
import br.com.payment.micro.dto.swagger.validation.fields.FieldsErrorDto;
import br.com.payment.micro.service.IPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
            summary = "Get payment link",
            description = "Create a link to make a payment",
            tags = {"Payment"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Payment link returned successfully",
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
                                    schema = @Schema(implementation = FieldsErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sale not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Amount to be paid is incompatible",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "502",
                            description = "Gateway Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
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

    @GetMapping("/api/payment/{saleId}/info")
    @Operation(
            summary = "Get payment information",
            description = "Return information of a payment by sale id",
            tags = {"Payment"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment information returned successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentInfoDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Payment not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<PaymentInfoDto> getPaymentInfo(
            @Parameter(description = "Id of the sale", required = true)
            @PathVariable String saleId
    ) {
        Payment payment = iPaymentService.getPaymentInfoBySaleId(saleId);
        return ResponseEntity.status(HttpStatus.OK).body(new PaymentInfoDto("Payment info returned successfully!", payment));
    }
}
