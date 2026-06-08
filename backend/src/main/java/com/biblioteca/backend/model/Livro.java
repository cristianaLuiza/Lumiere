package com.biblioteca.backend.model;

import com.biblioteca.backend.enums.GeneroLivro;
import com.biblioteca.backend.enums.TipoInteracao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "livros")
@SQLDelete(sql = "UPDATE livros SET ativo = false WHERE id = ?")
@SQLRestriction("ativo = true")
@Getter // Substitui o @Data de forma segura para Getters
@Setter // Substitui o @Data de forma segura para Setters
@NoArgsConstructor  // Construtor padrão exigido pelo JPA
@AllArgsConstructor // Construtor com todos os campos (opcional, mas útil)
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private String descricao;
    private Boolean disponivel = true;

    // Boa prática: iniciar como true para novos livros cadastrados
    private boolean ativo = true;

    @Enumerated(EnumType.STRING)
    private TipoInteracao tipoInteracao;

    @Enumerated(EnumType.STRING)
    private GeneroLivro generoLivro;

    // Relacionamento
    @ManyToOne(fetch = FetchType.LAZY) // LAZY evita carregar o usuário sem necessidade (ganho de performance)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;
}