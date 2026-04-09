package br.com.payment.micro.service;

import br.com.payment.micro.domain.MPPayment;
import br.com.payment.micro.dto.mercadoPago.MercadoPagoRequestPaymentLinkDto;
import com.mercadopago.resources.payment.PaymentRefund;

public interface IPaymentProviderService {
    public String createPayment(MercadoPagoRequestPaymentLinkDto dto);
    public MPPayment getPaymentDetails(String externalId);
    public PaymentRefund refundPayment(Long paymentId);
}
