package com.guilherme.biblioteca.controllers;

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
import com.guilherme.biblioteca.entities.Livro;
import com.guilherme.biblioteca.entities.requests.CreateLivroRequest;
import com.guilherme.biblioteca.repositories.AutorRepository;
import com.guilherme.biblioteca.repositories.LivroRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    final LivroRepository livroRepository;
    final AutorRepository autorRepository;
    
    @GetMapping
    public List<Livro> getLivros(){
        return livroRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getLivroById(
        @PathVariable Long id
    ) {
        Optional<Livro> existeLivro = livroRepository.findById(id);

        if(existeLivro.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(existeLivro);
    }

    @PostMapping
    public ResponseEntity<Object> createLivro(
        @RequestBody CreateLivroRequest createLivro
    ) {
        List<Autor> autoresRequest = autorRepository.findAllById(createLivro.getAutoresIds());

        Livro novoLivro = new Livro(
            null,
            createLivro.getTitulo(),
            createLivro.getIsbn(),
            createLivro.getAnoPublicacao(),
            autoresRequest
        );

        autoresRequest.forEach(autor -> {
            autor.addLivro(novoLivro);
        });


        return ResponseEntity.ok(livroRepository.save(novoLivro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateLivro(
        @PathVariable Long id, @RequestBody Livro updateLivro
    ) {
        Optional<Livro> existeLivro = livroRepository.findById(id);

        if(existeLivro.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Livro atualizaLivro = existeLivro.get();

        atualizaLivro.setTitulo(updateLivro.getTitulo());
        atualizaLivro.setIsbn(updateLivro.getIsbn());
        atualizaLivro.setAnoPublicacao(updateLivro.getAnoPublicacao());
        atualizaLivro.setAutores(updateLivro.getAutores());

        return ResponseEntity.ok(livroRepository.save(atualizaLivro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLivro(
        @PathVariable Long id
    ) {
        Optional<Livro> existeLivro = livroRepository.findById(id);

        if(existeLivro.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        livroRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
