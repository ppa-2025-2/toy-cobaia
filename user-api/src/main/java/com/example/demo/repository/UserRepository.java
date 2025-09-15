package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import com.example.demo.repository.entity.User;

public interface UserRepository extends ListCrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByHandle(String handle);

    boolean existsByHandle(String handle);
}
