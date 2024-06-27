package com.guilherme.biblioteca.entities.requests;

import java.time.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAluguelRequest {
    private LocalDate dataLocacao;
    private LocalDate dataDevolucao;
    private Long locatario_id;
}
