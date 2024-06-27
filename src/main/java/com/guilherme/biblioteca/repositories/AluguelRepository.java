package com.guilherme.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guilherme.biblioteca.entities.Aluguel;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long>{
    
}
