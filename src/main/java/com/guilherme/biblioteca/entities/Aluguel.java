package com.guilherme.biblioteca.entities;

import java.time.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor       // Criar getters e setters automaticamente
@Table(name = "alugueis") // Criação de tabela SQL
@Entity                   // Declaração de entidade
public class Aluguel {
    
    // ID para verificações
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Data de aluguel, com padrao "dia/mês/ano"
    @JsonFormat(pattern = "dd-MM-YYYY")
    private LocalDate dataLocacao;

    // Data de devolução, com padrao "dia/mês/ano"
    @JsonFormat(pattern = "dd-MM-YYYY")
    private LocalDate dataDevolucao;

    // Relacionamento muitos para um, muitos alugeis para um locatario
    @ManyToOne
    @JoinColumn(name = "locatario_id")
    @JsonIgnoreProperties("alugueis")
    private Locatario locatario;      
    
    // Relacionamento um para muitos, um aluguel pode ter muitos livros
    @OneToMany(mappedBy = "aluguel", orphanRemoval = true, cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties("aluguel")
    private List<Livro> livros;
}
