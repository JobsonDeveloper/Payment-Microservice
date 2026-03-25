package br.com.payment.micro.dto.request.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record ClientIdentifiersDto(
        @NotNull(message = "The client first name is required!") @Size(min = 1, message = "The client first name must be valid!") String firstName,
        @NotNull(message = "The client last name is required!") @Size(min = 1, message = "The client last name must be valid!") String lastName,
        @NotNull(message = "The client cpf is required!") @CPF(message = "The client cpf must be valid!") String cpf,
        @NotNull(message = "The client email is required!") @Email(message = "The client email must be valid!")String email
        ) {
}
