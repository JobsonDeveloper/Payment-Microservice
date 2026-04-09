package br.com.payment.micro.event.consumer;

import br.com.payment.micro.domain.Canceled;
import br.com.payment.micro.domain.Payment;
import br.com.payment.micro.domain.Status;
import br.com.payment.micro.event.dto.CancelPaymentEventDto;
import br.com.payment.micro.exception.ErrorCancelingPaymentException;
import br.com.payment.micro.exception.ErrorDeletingPaymentException;
import br.com.payment.micro.exception.PaymentNotFoundException;
import br.com.payment.micro.exception.PermissionDeniedException;
import br.com.payment.micro.repository.ICanceledRepository;
import br.com.payment.micro.repository.IPaymentRepository;
import br.com.payment.micro.service.IPaymentProviderService;
import com.mercadopago.resources.payment.PaymentRefund;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentConsumer {
    private final IPaymentRepository iPaymentRepository;
    private final ICanceledRepository iCanceledRepository;
    private final IPaymentProviderService iPaymentProviderService;

    public PaymentConsumer(
            IPaymentRepository iPaymentRepository,
            ICanceledRepository iCanceledRepository,
            IPaymentProviderService iPaymentProviderService
    ) {
        this.iPaymentRepository = iPaymentRepository;
        this.iCanceledRepository = iCanceledRepository;
        this.iPaymentProviderService = iPaymentProviderService;
    }

    @KafkaListener(
            topics = "sale",
            groupId = "payment-group"
    )
    public void cancelPayment(CancelPaymentEventDto cancelPaymentEventDto) {
        String saleId = cancelPaymentEventDto.id();
        String clientId = cancelPaymentEventDto.clientId();
        Status status = cancelPaymentEventDto.status();

        if (!status.equals(Status.CANCELED)) return;

        Payment payment = iPaymentRepository.findBySaleId(saleId).orElseThrow(PaymentNotFoundException::new);
        String paymentClientId = payment.getClientId();

        if (!paymentClientId.equals(clientId)) throw new PermissionDeniedException();

        Long MPPaymentId = payment.getPayment().getExternalPaymentId();
        PaymentRefund refund = iPaymentProviderService.refundPayment(MPPaymentId);
        payment.getPayment().setExternalStatus(refund.getStatus());

        Canceled canceled = Canceled.builder()
                .saleId(payment.getSaleId())
                .clientId(paymentClientId)
                .payment(payment.getPayment())
                .status(Status.CANCELED)
                .created_at(payment.getCreated_at())
                .updated_at(LocalDateTime.now())
                .build();

        iCanceledRepository.save(canceled);
        iPaymentRepository.deleteById(payment.getId());
    }
}