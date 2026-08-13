package com.biblioteca.backend.service;
import com.biblioteca.backend.dto.LivroHomeDTO;
import com.biblioteca.backend.dto.UsuarioDTO;
import com.biblioteca.backend.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import com.biblioteca.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

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
                usuario.getFotoUsuario(),
                usuario.getDescricaoUsuario()
        );
    }

    public Usuario atualizarUsuario(long id , Usuario usuarioAtualizado){
        Usuario usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado!"));
        usuarioExistente.setFotoUsuario(usuarioAtualizado.getFotoUsuario());
        usuarioExistente.setNomeUsuario(usuarioAtualizado.getNomeUsuario());
        usuarioExistente.setDescricaoUsuario(usuarioAtualizado.getDescricaoUsuario());

        return userRepository.save(usuarioExistente);
    }


}
