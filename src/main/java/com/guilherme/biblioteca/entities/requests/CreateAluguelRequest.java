package com.guilherme.biblioteca.entities.requests;

import java.time.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAluguelRequest {

    /* Classe para auxiliar na criação do aluguel */
    
    private LocalDate dataLocacao;
    private LocalDate dataDevolucao;
    private Long locatario_id;
    private List<Long> livros_id;
}
