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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentConsumer {
    private final IPaymentRepository iPaymentRepository;
    private final ICanceledRepository iCanceledRepository;

    public PaymentConsumer(IPaymentRepository iPaymentRepository, ICanceledRepository iCanceledRepository) {
        this.iPaymentRepository = iPaymentRepository;
        this.iCanceledRepository = iCanceledRepository;
    }

    @KafkaListener(
            topics = "sale",
            groupId = "payment-group"
    )
    public void cancelPayment(CancelPaymentEventDto cancelPaymentEventDto) {
        String saleId = cancelPaymentEventDto.id();
        String clientId = cancelPaymentEventDto.clientId();
        Status status = cancelPaymentEventDto.status();

        if (status.equals(Status.CANCELED)) {
            Optional<Payment> registeredPayment = iPaymentRepository.findBySaleId(saleId);

            if (!registeredPayment.isPresent()) {
                throw new PaymentNotFoundException();
            }
            Payment payment = registeredPayment.get();
            String paymentClientId = payment.getClientId();

            if (!paymentClientId.equals(clientId)) {
                throw new PermissionDeniedException();
            }

            if (payment.getStatus().equals(Status.PAID)) {
                Canceled canceled = Canceled.builder()
                        .saleId(payment.getSaleId())
                        .clientId(paymentClientId)
                        .payment(payment.getPayment())
                        .status(Status.CANCELED)
                        .created_at(payment.getCreated_at())
                        .updated_at(LocalDateTime.now())
                        .build();

                Canceled canceledPayment = iCanceledRepository.save(canceled);

                if(canceledPayment.getId() == null) {
                    throw new ErrorCancelingPaymentException();
                }
            }

            iPaymentRepository.deleteById(payment.getId());
            Optional<Payment> paymentDeleted = iPaymentRepository.findBySaleId(saleId);

            if (paymentDeleted.isPresent()) {
                throw new ErrorDeletingPaymentException();
            }
        }
    }
}
