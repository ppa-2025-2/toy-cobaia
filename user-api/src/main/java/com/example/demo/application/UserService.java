package com.example.demo.application;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.demo.application.exceptions.NotFoundException;
import com.example.demo.controller.dto.NewUserDTO;
import com.example.demo.domain.Island;
import com.example.demo.domain.Islands;
import com.example.demo.domain.Profile;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.domain.Workstation;
import com.example.demo.repository.IslandRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;

// Spring -> possui um container de Injeção de Dependências

// estereótipo
@Service // Domain, DomainService, Service, UseCase
@Validated
public class UserService {

    private final IslandRepository islandRepository;
    
    @SuppressWarnings("unused")
    private final EntityManager em;
    
    private final BCryptPasswordEncoder passwordEncoder = 
        new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Set<String> defaultRoles;

    public UserService(
        IslandRepository islandRepository,
        EntityManager em,
        UserRepository userRepository,
        RoleRepository roleRepository,
        @Value("${app.user.default.roles}")
        Set<String> defaultRoles
    ) {
        this.islandRepository = islandRepository;
        this.em = em;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.defaultRoles = defaultRoles;
    }

    // Application Service Method (router, similar ao controller)
    public void alocarWorkstationDisponivel(@NonNull Integer userId) {
        
        final var user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException());

        final Islands islands = Islands.of(islandRepository.
                        findIslandWithAvailableWorkstations()
        );
        
        if (islands.isEmpty()) {
            throw new IllegalStateException("Workstations not available");
        }
        
        // a lógica está nas classes de domínio Islands e Island
        islandRepository.save(islands
            .assignUserToIslandWithFewerWorkstations(user));
    }

    // cadastrar usuário é um use case (é uma feature)
    public void cadastrarUsuario(@Valid NewUserDTO newUser) {
        // FIXME: tornar esse Transaction Script
        // Domain Model
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
        
        // Unit of Work
        // se não houvesse repositório, usaríamos o EntityManager
        // em.persist(user);
        // em.flush();
        
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
