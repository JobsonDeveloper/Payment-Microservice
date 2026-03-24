package br.com.payment.micro.service;

import br.com.payment.micro.domain.Payment;
import br.com.payment.micro.domain.Status;
import br.com.payment.micro.dto.response.GetSaleInfoDto;
import br.com.payment.micro.event.dto.PaymentEventDto;
import br.com.payment.micro.event.producer.PaymentEventProducer;
import br.com.payment.micro.exception.ErrorChangingPaymentStatusException;
import br.com.payment.micro.exception.ServiceUnavailableException;
import br.com.payment.micro.exception.sale.ErrorRetrievingSaleInfoException;
import br.com.payment.micro.exception.sale.InconsistentValueException;
import br.com.payment.micro.exception.sale.SaleNotFoundException;
import br.com.payment.micro.repository.IPaymentRepository;
import feign.FeignException;
import feign.RetryableException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService implements IPaymentService {
    private final IPaymentRepository iPaymentRepository;
    private final PaymentEventProducer paymentEventProducer;
    private final ISalePayment iSalePayment;

    public PaymentService(
            IPaymentRepository iPaymentRepository,
            PaymentEventProducer paymentEventProducer, ISalePayment iSalePayment
    ) {
        this.iPaymentRepository = iPaymentRepository;
        this.paymentEventProducer = paymentEventProducer;
        this.iSalePayment = iSalePayment;
    }

    @Override
    public String getPaymentLink(Double value, String saleId) {
        try {
            GetSaleInfoDto response = iSalePayment.getSaleInfo(saleId);

            if (!value.equals(response.sale().totalValue())) {
                throw new InconsistentValueException();
            }

            // Call to mercado pago to get payment link

            PaymentEventDto event = new PaymentEventDto(
                    saleId,
                    Status.PENDING_PAYMENT
            );

            paymentEventProducer.setPaymentEvent(event);

            return "Link of payment";
        } catch (RetryableException e) {
            throw new ServiceUnavailableException("Sale Microservice");
        } catch (FeignException.NotFound e) {
            throw new SaleNotFoundException();
        } catch (FeignException e) {
            throw new ErrorRetrievingSaleInfoException();
        }
    }

    @Override
    public Payment makePayment(Payment payment) {
        return null;
    }

    @Override
    public Payment paymentCompleted(String saleId) {
        try {
            GetSaleInfoDto response = iSalePayment.getSaleInfo(saleId);
            String clientId = response.sale().client().id();
            Double value = response.sale().totalValue();

            Payment newPayment = Payment.builder()
                    .saleId(saleId)
                    .clientId(clientId)
                    .value(value)
                    .status(Status.PAID)
                    .created_at(LocalDateTime.now())
                    .build();

            Payment payment = iPaymentRepository.save(newPayment);

            if (payment.getId() == null) {
                throw new ErrorChangingPaymentStatusException();
            }

            paymentEventProducer.setPaymentEvent(new PaymentEventDto(
                    saleId,
                    Status.PAID
            ));

            return payment;
        } catch (RetryableException e) {
            throw new ServiceUnavailableException("Sale Microservice");
        } catch (FeignException.NotFound e) {
            throw new SaleNotFoundException();
        } catch (FeignException e) {
            throw new ErrorRetrievingSaleInfoException();
        }
    }
}
