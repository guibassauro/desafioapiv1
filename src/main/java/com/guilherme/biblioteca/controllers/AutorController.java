package com.guilherme.biblioteca.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guilherme.biblioteca.entities.Autor;
import com.guilherme.biblioteca.repositories.AutorRepository;
import com.guilherme.biblioteca.repositories.LivroRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    final AutorRepository autorRepository;
    final LivroRepository livroRepository;

    @GetMapping
    public List<Autor> listAutores(){
        return autorRepository.findAll();
    }

    @GetMapping("/{nome}")
    public ResponseEntity<Object> listAutorByName(
        @PathVariable String nome
    ) {
        Autor autorExiste = autorRepository.findByNome(nome);

        if(autorExiste == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(autorExiste);

    }

    @PostMapping
    public ResponseEntity<Object> createAutor(
        @RequestBody Autor createAutor
    ) {
        Autor novAutor = new Autor(
            null,
            createAutor.getNome(),
            createAutor.getSexo(),
            createAutor.getAnoNascimento(),
            createAutor.getCpf(),
            Collections.emptyList()
        );

        return ResponseEntity.ok(autorRepository.save(novAutor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAutor(
        @PathVariable Long id, @RequestBody Autor updateAutor
    ) {
        Optional<Autor> existeAutor = autorRepository.findById(id);

        if(existeAutor.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Autor atualizaAutor = existeAutor.get();

        atualizaAutor.setNome(updateAutor.getNome());
        atualizaAutor.setSexo(updateAutor.getSexo());
        atualizaAutor.setAnoNascimento(updateAutor.getAnoNascimento());
        atualizaAutor.setCpf(updateAutor.getCpf());

        return ResponseEntity.ok(autorRepository.save(atualizaAutor));
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAutor(
        @PathVariable Long id
    ) {
        
        Optional<Autor> existeAutor = autorRepository.findById(id);

        if(existeAutor.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        autorRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
    
}
