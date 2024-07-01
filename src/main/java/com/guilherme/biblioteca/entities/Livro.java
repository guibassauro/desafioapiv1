package com.guilherme.biblioteca.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                // Declaração de entidade
@AllArgsConstructor
@NoArgsConstructor     // Criar getters e setters automaticamente
@Data
@Table(name = "livro") // Criação de tabela SQL
public class Livro {
    
    // ID para verificações
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // String obrigatoria de titulo
    @Column(nullable = false)
    private String titulo;

    // String obrigatória e única de isbn
    @Column(nullable = false, unique = true)
    private String isbn;

    // String obrigatoria de ano de publicação
    @Column(nullable = false)
    private String anoPublicacao;

    // Relacionamento muitos para muitos, muitos livros podem ter muitos autores
    @ManyToMany(mappedBy = "livros", cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties("livros")
    private List<Autor> autores;

    // Relacionamento muitos para um, muitos livros podem ter um aluguel
    @ManyToOne
    @JoinColumn(name = "aluguel_id")
    @JsonIgnoreProperties("livros")
    private Aluguel aluguel;

    // Void para anexar livro ao aluguel
    public void addAluguel(final Aluguel aluguel){
        this.aluguel = aluguel;
    }
    
}
