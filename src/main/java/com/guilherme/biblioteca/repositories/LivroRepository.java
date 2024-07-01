package com.guilherme.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guilherme.biblioteca.entities.Livro;

/* Repositorio para salvar os livros */

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long>{
    
}
