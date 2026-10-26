package com.biblioteca.backend.admin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca.backend.admin.dto.AdminLivroForm;
import com.biblioteca.backend.admin.dto.AdminUsuarioDTO;
import com.biblioteca.backend.admin.dto.AdminUsuarioForm;
import com.biblioteca.backend.admin.dto.LivroAdminDTO;
import com.biblioteca.backend.enums.StatusInteresse;
import com.biblioteca.backend.enums.TipoUsuario;
import com.biblioteca.backend.model.Livro;
import com.biblioteca.backend.model.Usuario;
import com.biblioteca.backend.repository.AcessoRepository;
import com.biblioteca.backend.repository.InteresseRepository;
import com.biblioteca.backend.repository.LivroRepository;
import com.biblioteca.backend.repository.UserRepository;
import com.biblioteca.backend.service.LivroService;

@Service
public class AdminService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private InteresseRepository interesseRepository;

    @Autowired
    private AcessoRepository acessoRepository;

    @Autowired
    private LivroService livroService;

    public AdminService(
            LivroRepository livroRepository,
            UserRepository usuarioRepository) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Map<String, Long> obterRelatorios() {
        LocalDateTime agora = LocalDateTime.now();

        return Map.of(
                "totalLivros", livroRepository.count(),
                "totalUsuario", usuarioRepository.count(),
                "novosUsuarios", usuarioRepository.countByDataCadastroAfter(agora.minusDays(7)),
                "logins24h", acessoRepository.countByDataAcessoAfter(agora.minusHours(24)),
                "trocasConcluidas", interesseRepository.contarTrocasConcluidas());
    }

    public void excluirLivro(Long id) {
        livroRepository.excluirLivro(id);
    }

    public void restaurarLivro(Long id) {
        if (livroRepository.restaurar(id) == 0) {
            throw new RuntimeException("Livro não encontrado ou já ativo.");
        }
    }

    public void alterarDisponibilidade(Long id, boolean disponivel) {
        if (livroRepository.alterarDisponibilidade(id, disponivel) == 0) {
            throw new RuntimeException("Livro não encontrado ou removido.");
        }
    }

    public List<LivroAdminDTO> listarLivros() {
        return livroRepository.listarParaAdmin();
    }

    public Livro criarLivro(AdminLivroForm form) {
        if (vazio(form.titulo()) || vazio(form.autor())) {
            throw new RuntimeException("Informe o título e o autor do livro.");
        }

        if (form.generoLivro() == null || form.tipoInteracao() == null || form.usuarioId() == null) {
            throw new RuntimeException("Informe o gênero, a modalidade e o dono do livro.");
        }

        Livro livro = new Livro();
        livro.setTitulo(form.titulo().trim());
        livro.setAutor(form.autor().trim());
        livro.setDescricaoLivro(form.descricaoLivro());
        livro.setGeneroLivro(form.generoLivro());
        livro.setTipoInteracao(form.tipoInteracao());

        return livroService.salvar(livro, form.usuarioId());
    }

    public List<AdminUsuarioDTO> listarUsuario() {
        return usuarioRepository.findAll().stream()
                .map(AdminUsuarioDTO::new)
                .toList();
    }

    public AdminUsuarioDTO criarUsuario(AdminUsuarioForm form) {
        if (vazio(form.senha()) || form.senha().length() < 3) {
            throw new RuntimeException("A senha deve conter no mínimo 3 dígitos.");
        }

        Usuario usuario = new Usuario();
        usuario.setDataCadastro(LocalDateTime.now());

        return salvarUsuario(usuario, form);
    }

    public AdminUsuarioDTO atualizarUsuario(Long id, AdminUsuarioForm form) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        boolean perdeAdmin = usuario.getTipoUsuario() == TipoUsuario.ADMIN
                && form.tipoUsuario() != TipoUsuario.ADMIN;

        if (perdeAdmin && usuarioRepository.countByTipoUsuario(TipoUsuario.ADMIN) <= 1) {
            throw new RuntimeException("A plataforma precisa de ao menos um administrador.");
        }

        if (!vazio(form.senha()) && form.senha().length() < 3) {
            throw new RuntimeException("A senha deve conter no mínimo 3 dígitos.");
        }

        return salvarUsuario(usuario, form);
    }

    private AdminUsuarioDTO salvarUsuario(Usuario usuario, AdminUsuarioForm form) {
        if (vazio(form.username()) || vazio(form.nomeUsuario())) {
            throw new RuntimeException("Informe o nome e o nome de usuário.");
        }

        String username = form.username().trim();
        if (!username.equals(usuario.getUsername()) && usuarioRepository.existsByUsername(username)) {
            throw new RuntimeException("Esse nome de usuário já existe.");
        }

        usuario.setUsername(username);
        usuario.setNomeUsuario(form.nomeUsuario().trim());
        usuario.setDescricaoUsuario(form.descricaoUsuario());
        usuario.setTipoUsuario(form.tipoUsuario() == null ? TipoUsuario.COMUM : form.tipoUsuario());

        if (!vazio(form.senha())) {
            usuario.setSenha(form.senha());
        }

        return new AdminUsuarioDTO(usuarioRepository.save(usuario));
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }

    public void verificarAdministrador(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"));

        if (usuario.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Acesso permitido apenas para administradores");
        }
    }

    public long contarInteresses() {
        return interesseRepository.count();
    }

    public long contarInteressesPendentes() {
        return interesseRepository.countByStatus(StatusInteresse.PENDENTE);
    }
}
