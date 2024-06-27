package com.guilherme.biblioteca.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String sexo;

    @Column(nullable = false)
    private String anoNascimento;

    @Column(nullable = false, unique = true)
    private String cpf;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="autor_livro",
        joinColumns = @JoinColumn(name = "autor_id"),
        inverseJoinColumns = @JoinColumn(name = "livro_id")
    )
    @JsonIgnoreProperties("autores")
    private List<Livro> livros;
}
