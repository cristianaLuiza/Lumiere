package com.biblioteca.backend.admin.dto;

import com.biblioteca.backend.enums.TipoUsuario;

public record AdminUsuarioForm(
        String username,
        String senha,
        String nomeUsuario,
        String descricaoUsuario,
        TipoUsuario tipoUsuario) {
}
