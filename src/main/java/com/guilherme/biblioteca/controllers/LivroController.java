package com.guilherme.biblioteca.controllers;

import java.util.ArrayList;
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

@RestController               // Define controlador REST
@RequestMapping("/livros")    // Define rota do controlador 
@RequiredArgsConstructor      // Esconder voids de declaração de repositórios
public class LivroController {

    /* Declaração de repositórios */

    final LivroRepository livroRepository;
    final AutorRepository autorRepository;
    final AluguelRepository aluguelRepository;
    
    /* -------------------------  */
    
    /* Listar Livros */

    @GetMapping
    public List<Livro> getLivros(){
        // Retorna todos os livros existentes
        return livroRepository.findAll();
    }

    /* Listar livro por id */

    @GetMapping("/{id}")
    public ResponseEntity<Object> getLivroById(
        @PathVariable Long id
    ) {
        // Variavel para verificar a validade do ID 
        Optional<Livro> existeLivro = livroRepository.findById(id);

        // Se ID for invalido, encerra a função
        if(existeLivro.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        // Se o ID for valido, retorna a lista  de Alugueis do locatario 
        return ResponseEntity.ok(existeLivro);
    }

    /* Listar Livros disponíveis para aluguel */

    @GetMapping("/disponiveis")
    public ResponseEntity<Object> getDisponiveis(){

        // Lista com todos os livros
        List<Livro> livros = livroRepository.findAll();
        
        // Lista para salvar apenas os sem aluguel
        List<Livro> livrosDisponiveis = new ArrayList<Livro>();

        // Verifica livro a livro se o aluguel está vazio, se for o caso,
        // o adiciona à lista de livros disponíveis
        livros.forEach(livro -> {
            if(livro.getAluguel() == null){
                livrosDisponiveis.add(livro);
            }
        });

        // Retorna a lista de livros disponiveis
        return ResponseEntity.ok().body(livrosDisponiveis);
        
    }

    /* Criar Livro */

    @PostMapping
    public ResponseEntity<Object> createLivro(
        // Cria request de corpo JSON para criação do livro
        @RequestBody CreateLivroRequest createLivro
    ) {
        
        // Variavel para verificar se o autor existe
        List<Autor> autoresRequest = autorRepository.findAllById(createLivro.getAutoresIds());

        // Se não existir autor, encerra a função
        if(autoresRequest.isEmpty()){
            return ResponseEntity.badRequest().body("Um livro precisa de um autor(registre-o primeiro)");
        }

         // Criando entidade livro a partir da requisição
        Livro novoLivro = new Livro(
            null,
            createLivro.getTitulo(),
            createLivro.getIsbn(),
            createLivro.getAnoPublicacao(),
            autoresRequest,
            null
        );

        // Salvar cada livro da requisição na lista do autor 
        autoresRequest.forEach(autor -> {
            autor.addLivro(novoLivro);
        });


        // Salvando a entidade no repositório
        return ResponseEntity.ok(livroRepository.save(novoLivro));
    }

     /* Atualizar o Aluguel */

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateLivro(
        // Determina livro a ser atualizado e cria requisição de atuializações
        @PathVariable Long id, @RequestBody UpdateLivroRequest updateLivro
    ) {
        // Variavel para verificar a validade do ID
        Optional<Livro> existeLivro = livroRepository.findById(id);

        // Se a id for inválida, encerra a função
        if(existeLivro.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        // Var para salvar atualizações
        Livro atualizaLivro = existeLivro.get();

        // Var para atualizar o aluguel
        Optional<Aluguel> aluguelUpdate = aluguelRepository.findById(updateLivro.getAluguel_id());

        // Var solida para atualização do aluguel
        Aluguel atualizaAluguel = aluguelUpdate.get();

        // Lista para atualizar os autores do livro
        List<Autor> autoresUpdate = autorRepository.findAllById(updateLivro.getAutoresIds());

        // Pega os valores a serem mudados e setta a atualização
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

        // Salvar a atualização
        return ResponseEntity.ok(livroRepository.save(atualizaLivro));
    }

    /* Deleta Livro */

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLivro(
        @PathVariable Long id
    ) {

        // Variável para verificar se id é válida
        Optional<Livro> existeLivro = livroRepository.findById(id);

        // Se id for inválida, encerra a função
        if(existeLivro.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        
        // Se o livro estiver alugado, impossibilita a exclusão
        if(existeLivro.get().getAluguel() != null){
            return ResponseEntity.ok().body("Não é possível deletar um lívro alugado");
        }
        
        // Deleta o Livro do repositório
        livroRepository.deleteById(id);

        // Retorna a confirmação de que o livro foi deletado
        return ResponseEntity.ok().body("O livro foi deletado");
    }
}
