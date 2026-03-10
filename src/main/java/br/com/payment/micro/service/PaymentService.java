package br.com.payment.micro.service;

import br.com.payment.micro.domain.Payment;
import br.com.payment.micro.domain.Status;
import br.com.payment.micro.dto.response.SalePaymentDto;
import br.com.payment.micro.event.dto.PendingPaymentEventDto;
import br.com.payment.micro.event.producer.PendingPaymentEventProducer;
import br.com.payment.micro.exception.ServiceUnavailableException;
import br.com.payment.micro.exception.sale.ErrorRetrievingSaleInfoException;
import br.com.payment.micro.exception.sale.SaleNotFoundException;
import br.com.payment.micro.repository.IPaymentRepository;
import feign.FeignException;
import feign.RetryableException;
import org.springframework.stereotype.Service;

@Service
public class PaymentService implements IPaymentService {
    private final IPaymentRepository iPaymentRepository;
    private final PendingPaymentEventProducer paymentEventProducer;
    private final ISalePayment iSalePayment;

    public PaymentService(
            IPaymentRepository iPaymentRepository,
            PendingPaymentEventProducer paymentEventProducer, ISalePayment iSalePayment
    ) {
        this.iPaymentRepository = iPaymentRepository;
        this.paymentEventProducer = paymentEventProducer;
        this.iSalePayment = iSalePayment;
    }

    @Override
    public String getPaymentLink(Double value, String saleId) {
        try {
            SalePaymentDto sale = iSalePayment.getSaleInfo(saleId);

            // Call to mercado pago to get payment link

            PendingPaymentEventDto event = new PendingPaymentEventDto(
                    saleId,
                    Status.PENDING_PAYMENT
            );

            paymentEventProducer.pendingPaymentEvent(event);

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
}
