package com.example.demo.dto;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.example.demo.repository.entity.Profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewUserDTO(
        @NotNull(message = "O nome é obrigatório")
        @NotBlank(message = "Não pode ser composta apenas de espaços")
        @Length(min = 3, message = "O nome de ter no mínimo 3 caracteres")
        String name,
        String handle,
        @NotNull(message = "O e-mail é obrigatório")
        @Email(message = "Deve ser um e-mail válido")
        String email,
        @NotNull(message = "A senha é obrigatória")
        String password,
        String company,
        Profile.AccountType type,
        List<String> roles
)  {

}
