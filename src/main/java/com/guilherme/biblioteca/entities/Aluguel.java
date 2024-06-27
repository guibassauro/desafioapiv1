package com.guilherme.biblioteca.entities;

import java.util.Date;

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

    @JsonFormat(pattern = "HH:mm:ss dd-MM-YYYY")
    private Date dataLocacao;

    @JsonFormat(pattern = "HH:mm:ss dd-MM-YYYY")
    private Date dataDevolucao;

    @ManyToOne
    @JoinColumn(name = "locatarioAluguel_id")
    @JsonIgnoreProperties("alugueis")
    private Locatario locatario;
    
    
}
