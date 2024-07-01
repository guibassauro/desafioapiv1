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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guilherme.biblioteca.entities.Autor;
import com.guilherme.biblioteca.repositories.AutorRepository;
import com.guilherme.biblioteca.repositories.LivroRepository;

import lombok.RequiredArgsConstructor;

@RestController               // Define controlador REST
@RequestMapping("/autores")   // Define rota do controlador 
@RequiredArgsConstructor      // Esconder voids de declaração de repositórios
public class AutorController {

    /* Declaração de repositórios */

    final AutorRepository autorRepository;
    final LivroRepository livroRepository;

    /* --------------------------- */

    /* Listar os Autores */

    @GetMapping
    public List<Autor> listAutores(){
        // Retorna todos os autores existentes
        return autorRepository.findAll();
    }

     /* Listar os livros de um Autor */

    @GetMapping("/{nome}")
    public ResponseEntity<Object> listAutorByName(
        // Recebe String de nome do autor
        @RequestParam String nome
    ) {
        // Verifica se o autor existe através de uma função de autoresRepository
        Autor autorExiste = autorRepository.findByNome(nome);

        // Se autor não existir no repositório, encerra a função
        if(autorExiste == null){
            return ResponseEntity.badRequest().body("Autor não existe");
        }

        // Retorna o autor
        return ResponseEntity.ok(autorExiste);

    }

    /* Lista livros de um autor */

    @GetMapping("/livros/{id}")
    public ResponseEntity<Object> livrosAutores(
        @PathVariable Long id
    ) {
        // Var para validação de autor
        Optional<Autor> autorExiste = autorRepository.findById(id);

        // Se autor não exisitir, encerra a função
        if(autorExiste.isEmpty()){
            return ResponseEntity.badRequest().body("Id de autor invalido");
        } 

        // Retorna os livros do autor
        return ResponseEntity.ok().body(autorExiste.get().getLivros());
    }

    /* Criação de um autor */

    @PostMapping
    public ResponseEntity<Object> createAutor(
        // Requisição de corpo JSON para criação do autor
        @RequestBody Autor createAutor
    ) {
        
        // Pega os dados da requisição e constrói um novo autor
        Autor novAutor = new Autor(
            null,
            createAutor.getNome(),
            createAutor.getSexo(),
            createAutor.getAnoNascimento(),
            createAutor.getCpf(),
            Collections.emptyList()
        );

        // Salva o novo autor no repositório
        return ResponseEntity.ok(autorRepository.save(novAutor));
    }

    /* Atualiza o Autor */

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAutor(
        // Recebe id do autor e requisição de atualizações
        @PathVariable Long id, @RequestBody Autor updateAutor
    ) {

        // Var para verificar validação do autor
        Optional<Autor> existeAutor = autorRepository.findById(id);

        // Se o Id não for valido, encerra a função
        if(existeAutor.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        // Var para atualização do autor
        Autor atualizaAutor = existeAutor.get();

        // Setting das atualizações da entidade
        atualizaAutor.setNome(updateAutor.getNome());
        atualizaAutor.setSexo(updateAutor.getSexo());
        atualizaAutor.setAnoNascimento(updateAutor.getAnoNascimento());
        atualizaAutor.setCpf(updateAutor.getCpf());

        // Salva o autor com suas devidas atualizações no repositorio
        return ResponseEntity.ok(autorRepository.save(atualizaAutor));
        
    }

    /* Deleta Autor */

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAutor(
        @PathVariable Long id
    ) {
        
        // Var para verificar validação do autor
        Optional<Autor> existeAutor = autorRepository.findById(id);

        // Se o id não for válido, encerra a função
        if(existeAutor.isEmpty()){
            return ResponseEntity.badRequest().body("Autor não encontrado");
        }

        // Se o autor tiver livros, impossibilita a exclusão
        if(!existeAutor.get().getLivros().isEmpty()){
            return ResponseEntity.ok().body("Só é possível deletar um autor que não tenha livros");
        }

        // Exclui o Autor do repositório
        autorRepository.deleteById(id);

        // Retorna mensagem de confirmação da exclusão
        return ResponseEntity.ok().body("Autor deletado");
    }
    
}
