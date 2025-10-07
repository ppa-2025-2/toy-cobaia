package com.example.demo.repository;

import org.springframework.data.repository.ListCrudRepository;

import com.example.demo.repository.entity.Island;

public interface IslandRepository 
    extends ListCrudRepository<Island, Long> {
    
}
