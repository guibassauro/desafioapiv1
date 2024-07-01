package com.guilherme.biblioteca.controllers;

import java.util.List;
import java.util.Optional;
import java.time.*;

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
import com.guilherme.biblioteca.entities.Livro;
import com.guilherme.biblioteca.entities.Locatario;
import com.guilherme.biblioteca.entities.requests.CreateAluguelRequest;
import com.guilherme.biblioteca.entities.requests.UpdateAluguelRequest;
import com.guilherme.biblioteca.repositories.AluguelRepository;
import com.guilherme.biblioteca.repositories.LivroRepository;
import com.guilherme.biblioteca.repositories.LocatarioRepository;

import lombok.RequiredArgsConstructor;

@RestController               // Define controlador REST
@RequestMapping("/alugueis")  // Define rota do controlador 
@RequiredArgsConstructor      // Esconder voids de declaração de repositórios
public class AluguelController {
    
    /* Declaração de repositórios */

    final AluguelRepository aluguelRepository;
    final LocatarioRepository locatarioRepository;
    final LivroRepository livroRepository;

    /* --------------------------- */

    /* Listar os Alugueis */

    @GetMapping
    public List<Aluguel> getAlugueis(){
        // Retorna todos os alugueis existentes
        return aluguelRepository.findAll();
    }

    /* Listar os Alugueis de um Locatario */

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAluguelByLocatario(
        @PathVariable Long id
    ) {
        // Variavel para verificar a validade do ID 
        Optional<Locatario> existeLocatario = locatarioRepository.findById(id);

        // Se ID for invalido, encerra a função
        if(existeLocatario.isEmpty()){
            return ResponseEntity.badRequest().body("Id de locatario inválida");
        }

        // Se o ID for valido, retorna a lista  de Alugueis do locatario 
        return ResponseEntity.ok().body(existeLocatario.get().getAlugueis());
    }

    /* Criar Aluguel */

    @PostMapping
    public ResponseEntity<Object> createAluguel(
        // Cria request de corpo JSON para criação do aluguel
        @RequestBody CreateAluguelRequest createAluguel 
    ) {

        // Variavel para verificar a validade do ID
        Optional<Locatario> existingLocatario = locatarioRepository.findById(createAluguel.getLocatario_id());

        // Se ID for inválido, encerra a função
        if(existingLocatario.isEmpty()){
            return ResponseEntity.badRequest().body("Locatário não existe(Registre-o primeiro)");
        }

        // Lista de livros do aluguel criado a partir da lista de livro_IDs da requisição
        List<Livro> livrosRequest = livroRepository.findAllById(createAluguel.getLivros_id());

        // Se a lista estiver vazia, encerra a função
        if(livrosRequest.isEmpty()){
            return ResponseEntity.badRequest().body("O aluguel deve ter livros!");
        }

         // Determina a data de locação como hoje
        LocalDate hoje = LocalDate.now();

        // Criando entidade aluguel a partir da requisição
        Aluguel novoAluguel = new Aluguel( 
            null,
            hoje,
            hoje.plusDays(2),
            existingLocatario.get(),
            livrosRequest
        );

        // Salvar cada livro da requisição na lista do alguel 
        livrosRequest.forEach(livro -> {
            livro.addAluguel(novoAluguel);
        });
        
        // Salvando a entidade no repositório
        return ResponseEntity.ok(aluguelRepository.save(novoAluguel));
    }

    /* Atualizar Aluguel */

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAluguel(
        // Determina qual entidade atualizar baseada no ID
        // Cria requisição do que deve ser atualizado 
        @PathVariable Long id, @RequestBody UpdateAluguelRequest updateAluguel
    ) {
        // Variavel para verificar a validade do ID
        Optional<Aluguel> existeAluguel = aluguelRepository.findById(id);

        // Se a id for inválida, encerra a função
        if(existeAluguel.isEmpty()){
            return ResponseEntity.badRequest().body("ID de aluguel inválida");
        }

        // Var para salvar atualizações
        Aluguel atualizaAluguel = existeAluguel.get();

        // Var para atualizar o locatario
        Optional<Locatario> locatarioUpate = locatarioRepository.
        findById(updateAluguel.getLocatario_id());

        // Lista para atualizar os livros
        List<Livro> livrosUpdate = livroRepository.
        findAllById(updateAluguel.getLivros_id());
        
        // Pega os valores a serem mudados e setta a atualização
        atualizaAluguel.setDataLocacao(updateAluguel.getDataLocacao());
        atualizaAluguel.setDataDevolucao(updateAluguel.getDataDevolucao());
        atualizaAluguel.setLocatario(locatarioUpate.get());
        atualizaAluguel.setLivros(livrosUpdate);

        // Salvar a atualização
        return ResponseEntity.ok(aluguelRepository.save(atualizaAluguel));
    }

    /* Deleta o Aluguel */

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAluguel(
        @PathVariable Long id
    ) {

        /* FUNÇÃO NÃO FUNCIONANDO -> hipotese: o fato de três entidade se relacionarem
         * (Aluguel -> Livro -> Autor) não permite ao banco de dados deletar a entidade
         * pai sem deletar as entidades filhas, pois os Ids nescessitam de exisitr
         * 
         * Cascade = CascadeType.DELETE ou ALL até excluem a entidade, mas deletam as
         * filhas relacionadas a ela(Exclui o Livro e o Autor ligados ao Aluguel)
         */

        // Var para verificar validade do ID
        Optional<Aluguel> existeAluguel = aluguelRepository.findById(id);

        // Se o ID for inválido, encerra a função
        if(existeAluguel.isEmpty()){
            return ResponseEntity.badRequest().body("ID de aluguel inválido");
        }
        
        // Deleta o aluguel do repositório
        aluguelRepository.deleteById(id);

        // Retorna confirmação da exclusão
        return ResponseEntity.ok().body("Aluguel deletado");

    }
}
