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
@NoArgsConstructor         // Criar getters e setters automaticamente
@Entity                    // Declaração de entidade
@Table(name = "locatario") // Criação de tabela SQL
public class Locatario {
    
    // ID para verificações
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    // String obrigatoria para o nome do locatario
    @Column(nullable = false)
    private String nome;

    // String para o sexo do locatario
    private String sexo;

    // String obrigatoria para o telefone do locatario
    @Column(nullable = false)
    private String telefone;

    // String obrigatoria e unica para o email do locatario
    @Column(nullable = false, unique = true)
    private String email;

    // String unica para o ano de nascimento do locatario
    @Column(nullable = false)
    private String nascimentoData;

    // String obrigatoria e unica para o cpf do locatario
    @Column(nullable = false, unique = true)
    private String cpf;

    // Relacionamento um para muitos, um locatario pode ter muitos alugueis
    @OneToMany(mappedBy = "locatario")
    @JsonIgnoreProperties("locatario")
    private List<Aluguel> alugueis;

}
