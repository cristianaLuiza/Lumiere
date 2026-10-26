package com.biblioteca.backend.admin.dto;

import com.biblioteca.backend.enums.GeneroLivro;
import com.biblioteca.backend.enums.TipoInteracao;

public record AdminLivroForm(
        String titulo,
        String autor,
        String descricaoLivro,
        GeneroLivro generoLivro,
        TipoInteracao tipoInteracao,
        Long usuarioId) {
}
