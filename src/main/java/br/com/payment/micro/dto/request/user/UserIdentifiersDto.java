package br.com.payment.micro.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record UserIdentifiersDto(
        @NotNull(message = "The user first name is required!") @Size(min = 1, message = "The user first name must be valid!") String firstName,
        @NotNull(message = "The user last name is required!") @Size(min = 1, message = "The user last name must be valid!") String lastName,
        @NotNull(message = "The user cpf is required!") @CPF(message = "The user cpf must be valid!") String cpf,
        @NotNull(message = "The user email is required!") @Email(message = "The user email must be valid!")String email
        ) {
}
