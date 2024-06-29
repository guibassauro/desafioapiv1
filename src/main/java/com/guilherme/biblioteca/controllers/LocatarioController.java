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

@RestController
@RequestMapping("/locatarios")
@RequiredArgsConstructor
public class LocatarioController {
    
    final LocatarioRepository locatarioRepository;

    @GetMapping
    public List<Locatario> getLocatarios(){
        return locatarioRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Object> createLocatario(
        @RequestBody Locatario createLocatario
    ) {
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

        return ResponseEntity.ok(locatarioRepository.save(novoLocatario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateLocatario(
        @PathVariable Long id, @RequestBody Locatario updateLocatario
    ) {
        Optional<Locatario> existeLocatario = locatarioRepository.findById(id);

        if(existeLocatario.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Locatario atualizaLocatario = existeLocatario.get();

        atualizaLocatario.setNome(updateLocatario.getNome());
        atualizaLocatario.setSexo(updateLocatario.getSexo());
        atualizaLocatario.setTelefone(updateLocatario.getTelefone());
        atualizaLocatario.setEmail(updateLocatario.getEmail());
        atualizaLocatario.setNascimentoData(updateLocatario.getNascimentoData());
        atualizaLocatario.setCpf(updateLocatario.getCpf());

        return ResponseEntity.ok(locatarioRepository.save(atualizaLocatario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLocatario(
        @PathVariable Long id
    ) {
        Optional<Locatario> existeLocatario = locatarioRepository.findById(id);

        if(existeLocatario.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if(!existeLocatario.get().getAlugueis().isEmpty()){
            return ResponseEntity.ok().body
            ("Só é possível deletar um locatário que não tenha livros a serem entregues.");
        }

        locatarioRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
