package br.com.payment.micro.service;

import br.com.payment.micro.domain.Payment;
import br.com.payment.micro.domain.Status;

public interface IPaymentService {
    public String getPaymentLink(Double value, String saleId);
    public Payment makePayment(Payment payment);
}
