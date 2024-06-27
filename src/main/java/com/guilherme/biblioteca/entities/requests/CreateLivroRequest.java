package com.guilherme.biblioteca.entities.requests;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLivroRequest {
    String titulo;
    String isbn;
    String anoPublicacao;
    List<Long> autoresIds;
}
