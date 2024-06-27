package com.guilherme.biblioteca.entities.requests;

import java.util.List;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLivroRequest {
    String titulo;
    String isbn;
    String anoPublicacao;
    @NonNull
    List<Long> autoresIds;
    Long aluguel_id;
}
