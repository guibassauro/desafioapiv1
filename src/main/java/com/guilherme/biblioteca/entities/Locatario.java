package com.guilherme.biblioteca.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locatario")
public class Locatario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false)
    private String nome;

    private String sexo;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nascimentoData;

    @Column(nullable = false, unique = true)
    private String cpf;

    @OneToMany(mappedBy = "locatario")
    @JsonIgnoreProperties("locatario")
    private List<Aluguel> alugueis;

}
