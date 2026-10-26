package com.biblioteca.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import com.biblioteca.backend.dto.LoginDto;
import com.biblioteca.backend.dto.UsuarioDTO;
import com.biblioteca.backend.enums.TipoUsuario;
import com.biblioteca.backend.model.Acesso;
import com.biblioteca.backend.model.Usuario;
import com.biblioteca.backend.repository.AcessoRepository;
import com.biblioteca.backend.repository.UserRepository;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcessoRepository acessoRepository;

public UsuarioDTO fazerLogin(LoginDto loginDto) {

    Usuario usuario = userRepository
            .findByUsername(loginDto.getUsername())
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuário ou senha inválidos"
            ));

    if (!usuario.getSenha().equals(loginDto.getSenha())) {
        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Usuário ou senha inválidos"
        );
    }

    if (userRepository.countByTipoUsuario(TipoUsuario.ADMIN) == 0) {
        usuario.setTipoUsuario(TipoUsuario.ADMIN);
        userRepository.save(usuario);
    }

    Acesso acesso = new Acesso();
    acesso.setUsuario(usuario);
    acesso.setDataAcesso(LocalDateTime.now());
    acessoRepository.save(acesso);

    return new UsuarioDTO(
            usuario.getId(),
            usuario.getNomeUsuario(),
            usuario.getDescricaoUsuario(),
            usuario.getFotoUsuario(),
            usuario.getTipoUsuario()
    );
}
}