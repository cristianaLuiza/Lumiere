
package com.biblioteca.backend.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.biblioteca.backend.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios", schema = "dbo")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'COMUM'")
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario = TipoUsuario.COMUM;

    private String username;
    private String senha;
    private String nomeUsuario;
    private String descricaoUsuario;
    private String fotoUsuario;
    private LocalDateTime dataCadastro;
}
