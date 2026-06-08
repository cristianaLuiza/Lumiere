package com.biblioteca.backend.model;

import com.biblioteca.backend.enums.StatusInteresse;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="interesse")
@Data
public class Interesse {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn (name="usuario_interessado_id")
    private Usuario usuarioInteressado;

    @Enumerated(EnumType.STRING)
    private StatusInteresse status;

    @ManyToOne
    @JoinColumn(name = "livro_id")
    private Livro livro;
}
