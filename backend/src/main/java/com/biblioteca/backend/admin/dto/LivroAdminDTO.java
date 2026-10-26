package com.biblioteca.backend.admin.dto;

public interface LivroAdminDTO {

    Long getId();

    String getTitulo();

    String getAutor();

    String getDescricaoLivro();

    String getGeneroLivro();

    String getTipoInteracao();

    Boolean getDisponivel();

    Boolean getAtivo();

    Long getUsuarioId();

    String getNomeUsuario();
}
