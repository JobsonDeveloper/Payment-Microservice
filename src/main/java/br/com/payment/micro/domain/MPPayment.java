package br.com.payment.micro.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MPPayment {
    private Long externalPaymentId; // id do Mercado Pago
    private String externalReference;
    private BigDecimal transactionAmount;
    private BigDecimal netAmount;
    private String externalStatus;        // approved, pending, rejected
    private String statusDetail;  // detalhamento
    private String paymentMethodId; // visa, pix, boleto
    private String paymentTypeId;   // credit_card, pix, etc
    private String payerEmail;
    private String payerCpf;
    private Instant dateCreated;
    private Instant dateApproved;
    private Instant dateLastUpdated;
}
