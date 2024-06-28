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
@AllArgsConstructor
@Table(name = "alugueis")
@Entity
public class Aluguel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonFormat(pattern = "dd-MM-YYYY")
    private LocalDate dataLocacao;

    @JsonFormat(pattern = "dd-MM-YYYY")
    private LocalDate dataDevolucao;

    @ManyToOne
    @JoinColumn(name = "locatario_id")
    @JsonIgnoreProperties("alugueis")
    private Locatario locatario;
    
    @OneToMany(mappedBy = "aluguel")
    @JsonIgnoreProperties("aluguel")
    private List<Livro> livros;
}
