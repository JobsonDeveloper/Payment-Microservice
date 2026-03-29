package br.com.payment.micro.service;

import br.com.payment.micro.domain.MPPayment;
import br.com.payment.micro.dto.mercadoPago.MercadoPagoRequestPaymentLinkDto;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

public interface IPaymentProviderService {
    public String createPayment(MercadoPagoRequestPaymentLinkDto dto);
    public MPPayment getPaymentDetails(String externalId);
}
