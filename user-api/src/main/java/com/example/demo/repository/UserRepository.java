package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import com.example.demo.repository.entity.User;

public interface UserRepository extends ListCrudRepository<User, Integer> {
    // JPQL Java Persistence Query Language
    @Query("""
            SELECT u FROM User u
            WHERE u.id = :id
            """)
    User loadById(Integer id);

    Optional<User> findByEmail(String email);

    Optional<User> findByHandle(String handle);

    boolean existsByHandle(String handle);
}
