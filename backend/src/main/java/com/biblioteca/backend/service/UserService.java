package com.biblioteca.backend.service;

import com.biblioteca.backend.dto.EstatisticasUsuarioDTO;
import com.biblioteca.backend.dto.UsuarioDTO;
import com.biblioteca.backend.model.Usuario;
import com.biblioteca.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Usuario salvar(Usuario usuario) {
        return userRepository.save(usuario);
    }

    public EstatisticasUsuarioDTO estatisticas(Long usuarioId) {
        return jdbcTemplate.query(
                """
                SELECT v.*, dbo.fn_qtd_interesses_pendentes(v.usuario_id) AS interesses_pendentes
                FROM dbo.vw_estatisticas_usuario v
                WHERE v.usuario_id = ?
                """,
                (rs, i) -> new EstatisticasUsuarioDTO(
                        rs.getLong("usuario_id"),
                        rs.getString("nome_usuario"),
                        rs.getInt("livros_cadastrados"),
                        rs.getInt("livros_disponiveis"),
                        rs.getInt("livros_negociados"),
                        rs.getInt("interesses_enviados"),
                        rs.getInt("interesses_recebidos"),
                        rs.getInt("interesses_aceitos"),
                        rs.getInt("interesses_pendentes")
                ),
                usuarioId
        ).stream().findFirst().orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    public UsuarioDTO dadosUsuario(Long usuarioId) {
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNomeUsuario(),
                usuario.getDescricaoUsuario(),
                usuario.getFotoUsuario()
        );
    }

    public Usuario atualizarUsuario(long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        usuarioExistente.setNomeUsuario(usuarioAtualizado.getNomeUsuario());
        usuarioExistente.setDescricaoUsuario(usuarioAtualizado.getDescricaoUsuario());

        return userRepository.save(usuarioExistente);
    }

    public UsuarioDTO atualizarFoto(Long id, MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new RuntimeException("Arquivo de foto não informado.");
        }

        Usuario usuario = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        try {
            // Pasta onde as fotos dos usuários serão armazenadas
            Path pasta = Paths.get("uploads/usuarios").toAbsolutePath().normalize();
            if (!Files.exists(pasta) && Files.exists(Paths.get("backend/uploads/usuarios"))) {
                pasta = Paths.get("backend/uploads/usuarios").toAbsolutePath().normalize();
            }

            // Cria a pasta caso ela ainda não exista
            Files.createDirectories(pasta);

            // Remove a foto antiga se existir
            if (usuario.getFotoUsuario() != null && !usuario.getFotoUsuario().isBlank()) {
                Path fotoAntiga = pasta.resolve(usuario.getFotoUsuario());
                Files.deleteIfExists(fotoAntiga);
            }

            // Obtém a extensão original (.png, .jpg, etc)
            String originalName = arquivo.getOriginalFilename();
            String extensao = "";
            if (originalName != null && originalName.contains(".")) {
                extensao = originalName.substring(originalName.lastIndexOf("."));
            }

            // Gera nome único para evitar colisão e problemas de caracteres
            String nomeArquivo = UUID.randomUUID().toString() + extensao;
            Path caminhoArquivo = pasta.resolve(nomeArquivo);

            // Salva a imagem na pasta
            Files.copy(arquivo.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);

            // Salva somente o nome da imagem no banco
            usuario.setFotoUsuario(nomeArquivo);
            userRepository.save(usuario);

            return new UsuarioDTO(
                    usuario.getId(),
                    usuario.getNomeUsuario(),
                    usuario.getDescricaoUsuario(),
                    usuario.getFotoUsuario()
            );

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a foto do usuário", e);
        }
    }
}
