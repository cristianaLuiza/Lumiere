package com.biblioteca.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nomeUsuario;
    private String fotoUsuario;
    private String descricaoUsuario;

}