package com.guilherme.biblioteca.entities.requests;

import java.time.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAluguelRequest {
    private LocalDate dataLocacao = LocalDate.now();
    private LocalDate dataDevolucao = dataLocacao.plusDays(2);
    private Long locatario_id;
}
