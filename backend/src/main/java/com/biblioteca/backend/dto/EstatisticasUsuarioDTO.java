package com.biblioteca.backend.dto;

public record EstatisticasUsuarioDTO(

        Long usuarioId,
        String nomeUsuario,
        int livrosCadastrados,
        int livrosDisponiveis,
        int livrosNegociados,
        int interessesEnviados,
        int interessesRecebidos,
        int interessesAceitos,
        int interessesPendentes

) {}
