package com.example.demo.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.demo.controller.dto.NewUserDTO;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.repository.IslandRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.Island;
import com.example.demo.repository.entity.Profile;
import com.example.demo.repository.entity.Role;
import com.example.demo.repository.entity.User;
import com.example.demo.repository.entity.Workstation;

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

    // Application Service, interface entre o serviço e o domínio
    // Domain Service, ele é o próprio domínio

    public void alocarWorkstationDisponivel(@NonNull Integer userId) {
        
        final var user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException());

        // POO: agregação vs composição (aggregation vs composition)
        final var islands = islandRepository.findIslandWithAvailableWorkstations();

        if (islands.isEmpty()) {
            throw new IllegalStateException("Workstations not available");
        }

        // island 0 square 2/4
        // island 1 triangular 2/3
        // island 3 rectangular 1/6

        // busca ilhas começando por uma ws livre, depois duas, ...
        Island freeIsland = islands.getFirst(); 
        for (int slots = 1; slots < Island.Disposition.CIRCULAR.getPlacements(); slots++) {
            final int positions = slots;
            var possibleIsland = islands.stream()
                .filter(i -> i.getWorkstations().stream()
                            .map(Workstation::getUser)
                            .filter(Objects::nonNull)
                            .count() == positions)
                .findFirst();
            if (possibleIsland.isPresent()) {
                freeIsland = possibleIsland.get();
                break;
            }
        }

        // primeira workstation livre e seta o usuário
        freeIsland.getWorkstations().stream()
            .filter(ws -> ws.getUser() == null)
            .findFirst()
            .ifPresent(ws -> ws.setUser(user));

        islandRepository.save(freeIsland);
    }
    
    // cadastrar usuário é um use case (é uma feature)
    public void cadastrarUsuario(@Valid NewUserDTO newUser) {
 
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
