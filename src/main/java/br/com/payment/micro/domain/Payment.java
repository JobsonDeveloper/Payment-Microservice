package br.com.payment.micro.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payment")
public class Payment {
    @Id
    private String id;
    private String clientId;
    private Status status;
    private String saleId;
    private MPPayment payment;

    @CreatedDate
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;
}
