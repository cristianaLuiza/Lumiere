package com.biblioteca.backend.dto;

import com.biblioteca.backend.enums.GeneroLivro;
import com.biblioteca.backend.enums.TipoInteracao;

public record LivroHomeDTO(

        Long id,
        String titulo,
        String autor,
        String descricao,
        Boolean disponivel,
        Boolean ativo,
        TipoInteracao tipoInteracao,
        GeneroLivro generoLivro,

        Long usuarioId,
        String nomeUsuario,
        String fotoUsuario

) {}