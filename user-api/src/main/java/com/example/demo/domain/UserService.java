package com.example.demo.domain;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.demo.controller.dto.NewUserDTO;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.stereotype.Business;
import com.example.demo.repository.IslandRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.Island;
import com.example.demo.repository.entity.Profile;
import com.example.demo.repository.entity.Role;
import com.example.demo.repository.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;

// Spring -> possui um container de Injeção de Dependências

// estereótipo
@Service // Domain, DomainService, Service, UseCase
@Validated
public class UserService {

    private final IslandRepository islandRepository;
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

    // Application Service, interface entre o serviço e o domínio
    // Domain Service, ele é o próprio domínio

    public void alocarWorkstationDisponivel(@NonNull Integer userId) {
        
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException());

        // POO: agregação vs composição (aggregation vs composition)
        final List<Island> islands = islandRepository.findIslandWithAvailableWorkstations();

        if (islands.isEmpty()) {
            throw new IllegalStateException("Workstations not available");
        }

        // i1 square 2/4
        // i2 triangular 2/3
        // i3 rectangular 1/6

        // busca ilhas começando por uma ws livre, depois duas, ...
        Island livre; 
        for (int lugares = 1; ; lugares++) {
            final int positions = lugares;
            Optional<Island> possibleIsland =  islands.stream().filter(i -> i.getWorkstations()
                    .stream()
                    .filter(w -> w.getUser() == null)
                    .count() == positions).findFirst();
            if (possibleIsland.isPresent()) {
                livre = possibleIsland.get();
                break;
            }
        }
        // primeira workstation livre e seta o usuário
        livre.getWorkstations().stream().filter(w -> w.getUser() == null)
            .findFirst().ifPresent(w -> w.setUser(user));

        islandRepository.save(livre);

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
