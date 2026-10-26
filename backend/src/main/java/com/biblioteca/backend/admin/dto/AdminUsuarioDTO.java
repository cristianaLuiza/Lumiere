package com.biblioteca.backend.admin.dto;

import java.time.LocalDateTime;

import com.biblioteca.backend.enums.TipoUsuario;
import com.biblioteca.backend.model.Usuario;

public record AdminUsuarioDTO(
        Long id,
        String username,
        String nomeUsuario,
        String descricaoUsuario,
        String fotoUsuario,
        TipoUsuario tipoUsuario,
        LocalDateTime dataCadastro) {

    public AdminUsuarioDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNomeUsuario(),
                usuario.getDescricaoUsuario(),
                usuario.getFotoUsuario(),
                usuario.getTipoUsuario(),
                usuario.getDataCadastro());
    }
}
