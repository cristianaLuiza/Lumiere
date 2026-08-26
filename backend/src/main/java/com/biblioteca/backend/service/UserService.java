package com.biblioteca.backend.service;
import com.biblioteca.backend.dto.LivroHomeDTO;
import com.biblioteca.backend.dto.UsuarioDTO;
import com.biblioteca.backend.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import com.biblioteca.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Usuario salvar(Usuario usuario) {
        return userRepository.save(usuario);
    }

    public UsuarioDTO dadosUsuario(Long usuarioId) {

        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow();

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNomeUsuario(),
                "http://localhost:8081/usuarios/" + usuario.getId() + "/foto",
                usuario.getDescricaoUsuario()
        );
    }

    public Usuario atualizarUsuario(long id, Usuario usuarioAtualizado) {

        Usuario usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado!"));


        usuarioExistente.setNomeUsuario(usuarioAtualizado.getNomeUsuario());
        usuarioExistente.setDescricaoUsuario(usuarioAtualizado.getDescricaoUsuario());

        return userRepository.save(usuarioExistente);
    }

    public Usuario atualizarFoto(Long id, MultipartFile foto) {

        Usuario usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado!"));

        try {
            usuarioExistente.setFotoUsuario(foto.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a imagem!", e);
        }

        return userRepository.save(usuarioExistente);
    }

    public byte[] buscarFoto(Long id) {

        Usuario usuario = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado!"));

        if (usuario.getFotoUsuario() == null) {
            throw new RuntimeException("Usuário não possui foto!");
        }

        return usuario.getFotoUsuario();
    }
}
