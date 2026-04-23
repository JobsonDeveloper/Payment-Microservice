package br.com.payment.micro.service.imp;

import br.com.payment.micro.domain.MPPayment;
import br.com.payment.micro.domain.Payment;
import br.com.payment.micro.domain.Status;
import br.com.payment.micro.dto.mercadoPago.MercadoPagoRequestPaymentLinkDto;
import br.com.payment.micro.dto.mercadoPago.PayerDto;
import br.com.payment.micro.dto.mercadoPago.PaymentConfigDto;
import br.com.payment.micro.dto.request.SaleInfoDto;
import br.com.payment.micro.dto.request.product.ProductIdentifiersDto;
import br.com.payment.micro.dto.response.GetSaleInfoDto;
import br.com.payment.micro.event.dto.PaymentEventDto;
import br.com.payment.micro.event.producer.PaymentEventProducer;
import br.com.payment.micro.exception.PaymentAlreadyMadeException;
import br.com.payment.micro.exception.PaymentNotFoundException;
import br.com.payment.micro.exception.ServiceUnavailableException;
import br.com.payment.micro.exception.sale.ErrorRetrievingSaleInfoException;
import br.com.payment.micro.exception.sale.InconsistentValueException;
import br.com.payment.micro.exception.sale.SaleNotFoundException;
import br.com.payment.micro.repository.IPaymentRepository;
import br.com.payment.micro.service.IPaymentProviderService;
import br.com.payment.micro.service.IPaymentService;
import br.com.payment.micro.service.ISalePayment;
import feign.FeignException;
import feign.RetryableException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService implements IPaymentService {
    private final IPaymentRepository iPaymentRepository;
    private final PaymentEventProducer paymentEventProducer;
    private final ISalePayment iSalePayment;
    private final IPaymentProviderService iPaymentProviderService;

    public PaymentService(
            IPaymentRepository iPaymentRepository,
            PaymentEventProducer paymentEventProducer,
            ISalePayment iSalePayment,
            IPaymentProviderService iPaymentProviderService
    ) {
        this.iPaymentRepository = iPaymentRepository;
        this.paymentEventProducer = paymentEventProducer;
        this.iSalePayment = iSalePayment;
        this.iPaymentProviderService = iPaymentProviderService;
    }

    @Override
    public String getPaymentLink(
            Double value,
            String saleId,
            String cpf,
            String userFirstName,
            String userLastName,
            String userEmail
    ) {
        boolean payment = iPaymentRepository.existsBySaleId(saleId);
        if (payment) throw new PaymentAlreadyMadeException();

        GetSaleInfoDto saleData = null;

        try {
            saleData = iSalePayment.getSaleInfo(saleId);
        } catch (RetryableException e) {
            throw new ServiceUnavailableException("Sale Microservice or User Microservice");
        } catch (FeignException.NotFound e) {
            throw new SaleNotFoundException();
        } catch (FeignException e) {
            throw new ErrorRetrievingSaleInfoException();
        }

        if (!value.equals(saleData.sale().totalValue())) throw new InconsistentValueException();

        BigDecimal price = new BigDecimal(value);
        List<ProductIdentifiersDto> items = saleData.sale().items();
        PayerDto payer = new PayerDto(userFirstName, userLastName, cpf, userEmail);
        PaymentConfigDto paymentConfig = new PaymentConfigDto(12);

        String link = iPaymentProviderService.createPayment(
                new MercadoPagoRequestPaymentLinkDto(
                        saleId,
                        items,
                        payer,
                        paymentConfig
                )
        );

        PaymentEventDto event = new PaymentEventDto(
                null,
                saleData.sale().user().id(),
                cpf,
                saleId,
                Status.PENDING_PAYMENT
        );

        paymentEventProducer.setPaymentEvent(event);

        return link;
    }


    @Override
    public void paymentCompleted(String externalId) {
        MPPayment newPayment = iPaymentProviderService.getPaymentDetails(externalId);
        if (!newPayment.getExternalStatus().equals("approved")) return;

        String saleId = newPayment.getExternalReference();
        SaleInfoDto sale = null;

        boolean storedPayment = iPaymentRepository.existsBySaleId(saleId);
        if (storedPayment) return;

        try {
            sale = iSalePayment.getSaleInfo(saleId).sale();
        } catch (RetryableException e) {
            throw new ServiceUnavailableException("Sale Microservice");
        } catch (FeignException.NotFound e) {
            throw new SaleNotFoundException();
        } catch (FeignException e) {
            throw new ErrorRetrievingSaleInfoException();
        }

        String userId = sale.user().id();
        Payment payment = Payment.builder()
                .saleId(saleId)
                .userId(userId)
                .status(Status.PAID)
                .payment(newPayment)
                .created_at(LocalDateTime.now())
                .build();

        Payment registeredPayment = iPaymentRepository.save(payment);

        paymentEventProducer.setPaymentEvent(new PaymentEventDto(
                registeredPayment.getId(),
                userId,
                newPayment.getPayerCpf(),
                saleId,
                Status.PAID
        ));
    }

    @Override
    public Payment getPaymentInfoBySaleId(String saleId) {
        return iPaymentRepository.findBySaleId(saleId).orElseThrow(PaymentNotFoundException::new);
    }
}
