package com.guilherme.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guilherme.biblioteca.entities.Locatario;

@Repository
public interface LocatarioRepository extends JpaRepository<Locatario, Long>{
    
}
