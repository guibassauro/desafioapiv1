package com.guilherme.biblioteca.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor     // Criar getters e setters automaticamente
@Entity                // Declaração de entidade
@Table(name = "autor") // Criação de tabela SQL
public class Autor {
    
    // ID para verificações
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // String obrigatoria para o nome do autor
    @Column(nullable = false)
    private String nome;

    // String para o sexo do autor
    private String sexo;

    // String obrigatória para o ano de nascimento do autor
    @Column(nullable = false)
    private String anoNascimento;

    // String obrigatória e unica para o cpf do autor
    @Column(nullable = false, unique = true)
    private String cpf;

    // Relacionamento muitos para muitos, muitos autores podem ter muitos livros
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="autor_livro",
        joinColumns = @JoinColumn(name = "autor_id"),
        inverseJoinColumns = @JoinColumn(name = "livro_id")
    )
    @JsonIgnoreProperties("autores")
    private List<Livro> livros;

    // Void para anexar o autor ao livro
    public void addLivro(final Livro livro){
        this.livros.add(livro);
    }
}
