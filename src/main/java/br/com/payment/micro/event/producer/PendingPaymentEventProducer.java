package br.com.payment.micro.event.producer;

import br.com.payment.micro.event.dto.PendingPaymentEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PendingPaymentEventProducer {
    private final KafkaTemplate<String, PendingPaymentEventDto> kafkaTemplate;

    public PendingPaymentEventProducer(KafkaTemplate<String, PendingPaymentEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void pendingPaymentEvent(PendingPaymentEventDto paymentEventDto) {
        kafkaTemplate.send("payment-pending", paymentEventDto);
    }
}
