package com.guilherme.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guilherme.biblioteca.entities.Autor;

/* Repositorio para salvar os autores */

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long>{
    // Método para encontrar autor pelo nome
    Autor findByNome(String nome);
}
