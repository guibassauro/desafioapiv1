package com.guilherme.biblioteca.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "livro")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String anoPublicacao;

    @ManyToMany(mappedBy = "livros", cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties("livros")
    private List<Autor> autores;

    @ManyToOne
    @JoinColumn(name = "aluguel_id")
    @JsonIgnoreProperties("livros")
    private Aluguel aluguel;

    public void addAluguel(final Aluguel aluguel){
        this.aluguel = aluguel;
    }
    
}
