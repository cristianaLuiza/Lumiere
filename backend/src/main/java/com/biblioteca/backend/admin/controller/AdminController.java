package com.biblioteca.backend.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.backend.admin.dto.AdminLivroForm;
import com.biblioteca.backend.admin.dto.AdminUsuarioDTO;
import com.biblioteca.backend.admin.dto.AdminUsuarioForm;
import com.biblioteca.backend.admin.dto.LivroAdminDTO;
import com.biblioteca.backend.admin.service.AdminService;
import com.biblioteca.backend.model.Livro;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final String USUARIO = "X-Usuario-Id";

    @Autowired
    private AdminService adminService;

    @GetMapping("/relatorios")
    public Map<String, Long> obterRelatorio(@RequestHeader(USUARIO) Long usuarioId) {
        adminService.verificarAdministrador(usuarioId);
        return adminService.obterRelatorios();
    }

    @GetMapping("/livros")
    public List<LivroAdminDTO> listarLivros(@RequestHeader(USUARIO) Long usuarioId) {
        adminService.verificarAdministrador(usuarioId);
        return adminService.listarLivros();
    }

    @PostMapping("/livros")
    public Livro criarLivro(@RequestHeader(USUARIO) Long usuarioId, @RequestBody AdminLivroForm form) {
        adminService.verificarAdministrador(usuarioId);
        return adminService.criarLivro(form);
    }

    @PatchMapping("/livros/{id}/suspender")
    public void suspenderLivro(@RequestHeader(USUARIO) Long usuarioId, @PathVariable long id) {
        adminService.verificarAdministrador(usuarioId);
        adminService.alterarDisponibilidade(id, false);
    }

    @PatchMapping("/livros/{id}/reativar")
    public void reativarLivro(@RequestHeader(USUARIO) Long usuarioId, @PathVariable long id) {
        adminService.verificarAdministrador(usuarioId);
        adminService.alterarDisponibilidade(id, true);
    }

    @DeleteMapping("/livros/{id}")
    public void excluirLivro(@RequestHeader(USUARIO) Long usuarioId, @PathVariable long id) {
        adminService.verificarAdministrador(usuarioId);
        adminService.excluirLivro(id);
    }

    @PatchMapping("/livros/{id}/restaurar")
    public void restaurarLivro(@RequestHeader(USUARIO) Long usuarioId, @PathVariable long id) {
        adminService.verificarAdministrador(usuarioId);
        adminService.restaurarLivro(id);
    }

    @GetMapping("/usuarios")
    public List<AdminUsuarioDTO> listarUsuarios(@RequestHeader(USUARIO) Long usuarioId) {
        adminService.verificarAdministrador(usuarioId);
        return adminService.listarUsuario();
    }

    @PostMapping("/usuarios")
    public AdminUsuarioDTO criarUsuario(@RequestHeader(USUARIO) Long usuarioId, @RequestBody AdminUsuarioForm form) {
        adminService.verificarAdministrador(usuarioId);
        return adminService.criarUsuario(form);
    }

    @PutMapping("/usuarios/{id}")
    public AdminUsuarioDTO atualizarUsuario(
            @RequestHeader(USUARIO) Long usuarioId,
            @PathVariable long id,
            @RequestBody AdminUsuarioForm form) {
        adminService.verificarAdministrador(usuarioId);
        return adminService.atualizarUsuario(id, form);
    }

    @GetMapping("/relatorios/interesses")
    public long contarInteresses(@RequestHeader(USUARIO) Long usuarioId) {
        adminService.verificarAdministrador(usuarioId);
        return adminService.contarInteresses();
    }

    @GetMapping("/relatorios/interesses/pendentes")
    public long contarInteressesPendentes(@RequestHeader(USUARIO) Long usuarioId) {
        adminService.verificarAdministrador(usuarioId);
        return adminService.contarInteressesPendentes();
    }
}
