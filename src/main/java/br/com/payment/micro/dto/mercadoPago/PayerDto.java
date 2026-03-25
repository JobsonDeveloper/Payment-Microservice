package br.com.payment.micro.dto.mercadoPago;


public record PayerDto(
        String name,
        String surname,
        String cpf,
        String email
) {
}
