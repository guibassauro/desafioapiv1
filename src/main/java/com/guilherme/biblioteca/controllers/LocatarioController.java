package com.guilherme.biblioteca.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guilherme.biblioteca.entities.Locatario;
import com.guilherme.biblioteca.repositories.LocatarioRepository;

import lombok.RequiredArgsConstructor;

@RestController                  // Define controlador REST
@RequestMapping("/locatarios")   // Define rota do controlador 
@RequiredArgsConstructor         // Esconder voids de declaração de repositórios
public class LocatarioController {

    /* Declaração de repositórios */
    
    final LocatarioRepository locatarioRepository;

    /* --------------------------- */

    /* Listar todos os Locatarios */

    @GetMapping
    public List<Locatario> getLocatarios(){
        // Retorna todos os locatarios existentes
        return locatarioRepository.findAll();
    }

    /* Criar Locatario */

    @PostMapping
    public ResponseEntity<Object> createLocatario(
        // Cria request de corpo JSON para criação do aluguel
        @RequestBody Locatario createLocatario
    ) {

        // Criando entidade aluguel a partir da requisição
        Locatario novoLocatario = new Locatario(
            null,
            createLocatario.getNome(),
            createLocatario.getSexo(),
            createLocatario.getTelefone(),
            createLocatario.getEmail(),
            createLocatario.getNascimentoData(),
            createLocatario.getCpf(),
            Collections.emptyList()
        );

        // Salvando a entidade no repositório
        return ResponseEntity.ok(locatarioRepository.save(novoLocatario));
    }

    /* Atualizar Aluguel */

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateLocatario(
        // Determina qual entidade atualizar baseada no ID
        // Cria requisição do que deve ser atualizado 
        @PathVariable Long id, @RequestBody Locatario updateLocatario
    ) {
        // Variavel para verificar a validade do ID
        Optional<Locatario> existeLocatario = locatarioRepository.findById(id);

        // Se o ID for invalido, encerra a função
        if(existeLocatario.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        // Var para salvar atualizações
        Locatario atualizaLocatario = existeLocatario.get();

        // Pega os valores a serem mudados e setta a atualização
        atualizaLocatario.setNome(updateLocatario.getNome());
        atualizaLocatario.setSexo(updateLocatario.getSexo());
        atualizaLocatario.setTelefone(updateLocatario.getTelefone());
        atualizaLocatario.setEmail(updateLocatario.getEmail());
        atualizaLocatario.setNascimentoData(updateLocatario.getNascimentoData());
        atualizaLocatario.setCpf(updateLocatario.getCpf());

        // Salvar a atualização
        return ResponseEntity.ok(locatarioRepository.save(atualizaLocatario));
    }

    /* Deleta locatario */

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLocatario(
        @PathVariable Long id
    ) {

        // Var para verificar validade do ID
        Optional<Locatario> existeLocatario = locatarioRepository.findById(id);

        // Se o ID for inválido, encerra a função
        if(existeLocatario.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        // Se o locatario tiver livros alugados, impede a exclusão
        if(!existeLocatario.get().getAlugueis().isEmpty()){
            return ResponseEntity.ok().body
            ("Só é possível deletar um locatário que não tenha livros a serem entregues.");
        }

        // Deleta o locatario do repositório
        locatarioRepository.deleteById(id);

        // Retorna confirmação de exclusão da entidade
        return ResponseEntity.ok().body("Locatario deletado");
    }
}
