package br.com.payment.micro.service.imp;

import br.com.payment.micro.dto.mercadoPago.MercadoPagoRequestPaymentLinkDto;
import br.com.payment.micro.exception.mercadoPago.ErrorGettingPaymentLinkException;
import br.com.payment.micro.exception.mercadoPago.MercadoPagoException;
import br.com.payment.micro.service.IPaymentProviderService;
import br.com.payment.micro.exception.JsonProcessingErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class PaymentProviderService implements IPaymentProviderService {
    private static final Logger log = LoggerFactory.getLogger(PaymentProviderService.class);
    @Value("${application.currency.id}")
    private String currencyId;

    @Value("${mercado-pago.payment.back.url.success}")
    private String backUrlSuccess;

    @Value("${mercado-pago.payment.back.url.pending}")
    private String backUrlPending;

    @Value("${mercado-pago.payment.back.url.failure}")
    private String backUrlFailure;

    @Value("${mercado-pago.payment.notification.url}")
    private String notificationUrl;

    @Override
    public String createPayment(MercadoPagoRequestPaymentLinkDto dto) {
        try {
            String payerName = dto.payerDto().name();
            String payerSurname = dto.payerDto().surname();
            String payerCpf = dto.payerDto().cpf();
            String payerEmail = dto.payerDto().email();

            List<PreferenceItemRequest> items = dto.items().stream()
                    .map(item -> PreferenceItemRequest.builder()
                            .title(item.name())
                            .description(item.description())
                            .quantity(item.quantity())
                            .currencyId(currencyId)
                            .unitPrice(BigDecimal.valueOf(item.value()))
                            .build()
                    ).toList();

            IdentificationRequest identification = IdentificationRequest.builder()
                    .type("CPF")
                    .number(payerCpf)
                    .build();

            PreferencePayerRequest payer = PreferencePayerRequest.builder()
                    .name(payerName)
                    .surname(payerSurname)
                    .identification(identification)
                    .email(payerEmail)
                    .build();

            PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                    .installments(dto.paymentConfig().installments())
                    .build();

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(backUrlSuccess)
                    .pending(backUrlPending)
                    .failure(backUrlFailure)
                    .build();

            PreferenceRequest request = PreferenceRequest.builder()
                    .items(items)
                    .payer(payer)
                    .paymentMethods(paymentMethods)
                    .backUrls(backUrls)
                    .notificationUrl(notificationUrl)
                    .autoReturn("approved")
                    .externalReference(dto.saleId())
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(request);

            return preference.getInitPoint();
        } catch (MPApiException e) {
            String content = e.getApiResponse().getContent();

            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> json = mapper.readValue(
                        content, Map.class
                );

                log.error("Error: {}", json.get("message"));
            } catch (JsonProcessingException ex) {
                throw new JsonProcessingErrorException("It was not possible to process the Mercado Pago error JSON!");
            }

            throw new MercadoPagoException("The payment details was not provided correctly!");
        } catch (MPException e) {
            throw new ErrorGettingPaymentLinkException("We were unable to generate the payment link!");
        }
    }
}
