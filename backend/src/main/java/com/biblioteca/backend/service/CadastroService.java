package com.biblioteca.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.backend.dto.CadastroDto;
import com.biblioteca.backend.dto.UsuarioDTO;
import com.biblioteca.backend.model.Usuario;
import com.biblioteca.backend.repository.UserRepository;

@Service 
public class CadastroService {
    
    @Autowired 
    private UserRepository userRepository;


    public UsuarioDTO criarCadastro( CadastroDto dto){

        Long id = userRepository.cadastrarUsuario(dto.getUsername(), dto.getSenha(), dto.getNomeUsuario());

        Usuario usuarioSalvo = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

         
    return new UsuarioDTO(
        usuarioSalvo.getId(),
        usuarioSalvo.getNomeUsuario(),
        usuarioSalvo.getDescricaoUsuario(),
        usuarioSalvo.getFotoUsuario()
    );
      
    }
    
}
