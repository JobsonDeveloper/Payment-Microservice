package br.com.payment.micro.service;

import br.com.payment.micro.domain.Payment;

public interface IPaymentService {
    public Payment makePayment(Payment payment);
}
