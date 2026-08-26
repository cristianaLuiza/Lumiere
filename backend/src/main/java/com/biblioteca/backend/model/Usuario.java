package com.biblioteca.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String senha;
    private String nomeUsuario;

    @Lob
    @Column(name = "foto_usuario")
    private byte[] fotoUsuario;

    private String descricaoUsuario;

}