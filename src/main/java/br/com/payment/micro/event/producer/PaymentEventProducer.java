package br.com.payment.micro.event.producer;

import br.com.payment.micro.event.dto.PaymentEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventProducer {
    private final KafkaTemplate<String, PaymentEventDto> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, PaymentEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void setPaymentEvent(PaymentEventDto paymentEventDto) {
        kafkaTemplate.send("payment", paymentEventDto);
    }
}
