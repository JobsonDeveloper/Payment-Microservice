package br.com.payment.micro.service;

import br.com.payment.micro.domain.Payment;
import br.com.payment.micro.repository.IPaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService implements IPaymentService {
    private final IPaymentRepository iPaymentRepository;

    public PaymentService(IPaymentRepository iPaymentRepository) {
        this.iPaymentRepository = iPaymentRepository;
    }

    @Override
    public Payment makePayment(Payment payment) {
        return null;
    }
}
