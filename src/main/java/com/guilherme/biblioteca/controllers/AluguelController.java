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

@RestController
@RequestMapping("/alugueis")
@RequiredArgsConstructor
public class AluguelController {
    
    final AluguelRepository aluguelRepository;
    final LocatarioRepository locatarioRepository;
    final LivroRepository livroRepository;

    @GetMapping
    public List<Aluguel> getAlugueis(){
        return aluguelRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAluguelByLocatario(
        @PathVariable Long id
    ) {
        Optional<Locatario> existeLocatario = locatarioRepository.findById(id);

        if(existeLocatario.isEmpty()){
            return ResponseEntity.badRequest().body("Id de locatario inválida");
        }

        return ResponseEntity.ok().body(existeLocatario.get().getAlugueis());
    }

    @PostMapping
    public ResponseEntity<Object> createAluguel(
        @RequestBody CreateAluguelRequest createAluguel
    ) {

        Optional<Locatario> existingLocatario = locatarioRepository.findById(createAluguel.getLocatario_id());

        if(existingLocatario.isEmpty()){
            return ResponseEntity.badRequest().body("Locatário não existe(Registre-o primeiro)");
        }

        List<Livro> livrosRequest = livroRepository.findAllById(createAluguel.getLivros_id());

        if(livrosRequest.isEmpty()){
            return ResponseEntity.badRequest().body("O aluguel deve ter livros!");
        }

        LocalDate hoje = LocalDate.now();

        Aluguel novoAluguel = new Aluguel(
            null,
            hoje,
            hoje.plusDays(2),
            existingLocatario.get(),
            livrosRequest
        );

        livrosRequest.forEach(livro -> {
            livro.addAluguel(novoAluguel);
        });
        
        return ResponseEntity.ok(aluguelRepository.save(novoAluguel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAluguel(
        @PathVariable Long id, @RequestBody UpdateAluguelRequest updateAluguel
    ) {
        Optional<Aluguel> existeAluguel = aluguelRepository.findById(id);

        if(existeAluguel.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Aluguel atualizaAluguel = existeAluguel.get();

        Optional<Locatario> locatarioUpate = locatarioRepository.findById(updateAluguel.getLocatario_id());

        List<Livro> livrosUpdate = livroRepository.findAllById(updateAluguel.getLivros_id());

        atualizaAluguel.setDataLocacao(updateAluguel.getDataLocacao());
        atualizaAluguel.setDataDevolucao(updateAluguel.getDataDevolucao());
        atualizaAluguel.setLocatario(locatarioUpate.get());
        atualizaAluguel.setLivros(livrosUpdate);

        return ResponseEntity.ok(aluguelRepository.save(atualizaAluguel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAluguel(
        @PathVariable Long id
    ) {
        Optional<Aluguel> existeAluguel = aluguelRepository.findById(id);
        Aluguel aluguel = new Aluguel();

        if(existeAluguel.isEmpty()){
            return ResponseEntity.notFound().build();
        } else{
            aluguel = existeAluguel.get();
        }

        aluguel.delLivro(aluguel);
        aluguel.setLocatario(null);
        aluguelRepository.save(aluguel);

        return ResponseEntity.ok().body(aluguel);
        
        //aluguelRepository.deleteById(id);

        //return ResponseEntity.ok().body("Aluguel deletado");

    }
}
