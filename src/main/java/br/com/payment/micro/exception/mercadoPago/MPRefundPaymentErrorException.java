package br.com.payment.micro.exception.mercadoPago;

public class MPRefundPaymentErrorException extends RuntimeException {
  public  MPRefundPaymentErrorException() {super("It was not possible refund the payment on Mercado Pago!");}
    public MPRefundPaymentErrorException(String message) {
        super(message);
    }
}
