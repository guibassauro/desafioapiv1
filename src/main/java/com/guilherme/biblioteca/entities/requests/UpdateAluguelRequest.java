package com.guilherme.biblioteca.entities.requests;

import java.time.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAluguelRequest {

    /* Classe para auxiliar na atualização do aluguel */
    
    private LocalDate dataLocacao;
    private LocalDate dataDevolucao;
    private Long locatario_id;
    private List<Long> livros_id;
}
