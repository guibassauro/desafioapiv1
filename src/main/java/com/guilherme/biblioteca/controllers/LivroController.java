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

import com.guilherme.biblioteca.entities.Aluguel;
import com.guilherme.biblioteca.entities.Autor;
import com.guilherme.biblioteca.entities.Livro;
import com.guilherme.biblioteca.entities.requests.CreateLivroRequest;
import com.guilherme.biblioteca.entities.requests.UpdateLivroRequest;
import com.guilherme.biblioteca.repositories.AluguelRepository;
import com.guilherme.biblioteca.repositories.AutorRepository;
import com.guilherme.biblioteca.repositories.LivroRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    final LivroRepository livroRepository;
    final AutorRepository autorRepository;
    final AluguelRepository aluguelRepository;
    
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
            autoresRequest,
            null
        );

        autoresRequest.forEach(autor -> {
            autor.addLivro(novoLivro);
        });


        return ResponseEntity.ok(livroRepository.save(novoLivro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateLivro(
        @PathVariable Long id, @RequestBody UpdateLivroRequest updateLivro
    ) {
        Optional<Livro> existeLivro = livroRepository.findById(id);

        if(existeLivro.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Livro atualizaLivro = existeLivro.get();

        Optional<Aluguel> aluguelUpdate = aluguelRepository.findById(updateLivro.getAluguel_id());

        Aluguel atualizaAluguel = aluguelUpdate.get();

        List<Autor> autoresUpdate = autorRepository.findAllById(updateLivro.getAutoresIds());


        atualizaLivro.setTitulo(updateLivro.getTitulo());
        atualizaLivro.setIsbn(updateLivro.getIsbn());
        atualizaLivro.setAnoPublicacao(updateLivro.getAnoPublicacao());
        atualizaLivro.setAutores(autoresUpdate);
        atualizaLivro.setAluguel(atualizaAluguel);

        /* Bug em atualização de autores -> Não é possível adicionar autores na atualização,
         * caso o autor seja removido, o livro fica sem autor obrigatoriamente, obrigado o
         * recadastro.
         * Repetir o médodo usado para salvar o autor na "List<Autor> autores" usado no 
         * @PostMapping gera outro bug, de clonar o autor caso ele não seja modificado.
         */

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
