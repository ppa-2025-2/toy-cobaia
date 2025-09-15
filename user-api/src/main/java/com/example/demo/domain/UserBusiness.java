package com.example.demo.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;

import com.example.demo.controller.dto.NewUserDTO;
import com.example.demo.domain.stereotype.Business;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.Profile;
import com.example.demo.repository.entity.Role;
import com.example.demo.repository.entity.User;

import jakarta.validation.Valid;

// Spring -> possui um container de Injeção de Dependências

// estereótipo
@Business // Domain, DomainService, Service, UseCase
@Validated
public class UserBusiness {

    private final BCryptPasswordEncoder passwordEncoder = 
        new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Set<String> defaultRoles;

    public UserBusiness(
        UserRepository userRepository,
        RoleRepository roleRepository,
        @Value("${app.user.default.roles}")
        Set<String> defaultRoles
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.defaultRoles = defaultRoles;
    }
    
    // cadastrar usuário é um use case (é uma feature)
    public void cadastrarUsuario(@Valid NewUserDTO newUser) {
        // if (newUser.email() == null || newUser.password() == null) {
        //     throw new IllegalArgumentException("Email e senha são obrigatórios");
        // }

        // if (newUser.email().isEmpty() || newUser.password().isEmpty()) {
        //     throw new IllegalArgumentException("Email e senha não podem estar vazios");
        // }

        // if (!newUser.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        //     throw new IllegalArgumentException("Email não é válido");
        // }

        if (!newUser.password().matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres e conter pelo menos uma letra e um número");
        }
        
        userRepository.findByEmail(newUser.email())
            .ifPresent(user -> {
                throw new IllegalArgumentException("Usuário com o email " + newUser.email() + " já existe");
            });

        userRepository.findByHandle(newUser.handle())
            .ifPresent(user -> {
                throw new IllegalArgumentException("Usuário com o nome " + newUser.handle() + " já existe");
            });

        User user = new User();
        
        user.setEmail(newUser.email());
        user.setHandle(newUser.handle() != null ? newUser.handle() : generateHandle(newUser.email()));
        user.setPassword(passwordEncoder.encode(newUser.password()));
        
        Set<Role> roles = new HashSet<>();
        
        roles.addAll(roleRepository.findByNameIn(defaultRoles));

        Set<Role> additionalRoles = roleRepository.findByNameIn(newUser.roles());
        if (additionalRoles.size() != newUser.roles().size()) {
            throw new IllegalArgumentException("Alguns papéis não existem");
        }

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("O usuário deve ter pelo menos um papel");
        }

        user.setRoles(roles);

        Profile profile = new Profile();
        
        profile.setName(newUser.name());
        profile.setCompany(newUser.company());
        profile.setType(newUser.type() != null ? newUser.type() : Profile.AccountType.FREE);

        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user); 
    }

    private String generateHandle(String email) {
        String[] parts = email.split("@");
        String handle = parts[0];
        int i = 1;
        while (userRepository.existsByHandle(handle)) {
            handle = parts[0] + i++;
        }
        return handle;
    }
}
