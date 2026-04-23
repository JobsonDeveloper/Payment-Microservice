package br.com.payment.micro.service;

import br.com.payment.micro.domain.Payment;

public interface IPaymentService {
    public String getPaymentLink(Double value, String saleId, String cpf, String userFirstName, String userLastName, String email);
    public void paymentCompleted(String externalId);
    public Payment getPaymentInfoBySaleId(String saleId);
}
