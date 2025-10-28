package com.example.demo.controller.dto;

/*
 * -- C -> Mother of All Languages
 * -- Turing Complete Language
 * -- LINGUAGEM DECLARATIVA (linguagem de pedidos)
 * CREATE TABLE users (
 *      name VARCHAR(20) NOT NULL CHECK LEN(name) >= 3 
 * )
 */

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.example.demo.domain.Profile;

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
