package br.com.payment.micro.service;

import br.com.payment.micro.domain.Canceled;
import br.com.payment.micro.domain.Payment;
import br.com.payment.micro.domain.Status;

public interface IPaymentService {
    public String getPaymentLink(Double value, String saleId, String cpf, String clientFirstName, String clientLastName, String email);
    public Payment makePayment(Payment payment);
    public void paymentCompleted(String externalId);
}
