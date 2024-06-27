package com.guilherme.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guilherme.biblioteca.entities.Autor;


@Repository
public interface AutorRepository extends JpaRepository<Autor, Long>{
    Autor findByNome(String nome);
}
