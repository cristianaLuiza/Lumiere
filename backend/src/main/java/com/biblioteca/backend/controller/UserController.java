package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.UsuarioDTO;
import com.biblioteca.backend.model.Usuario;
import com.biblioteca.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Usuario cadastrarUsuario(@RequestBody Usuario usuario) {
        return userService.salvar(usuario);
    }

    @GetMapping("/{usuarioId}")
    public UsuarioDTO buscarUsuario(@PathVariable Long usuarioId) {
        return userService.dadosUsuario(usuarioId);
    }

    @PutMapping("/{id}")
    public Usuario atualizarUsuario(@PathVariable long id, @RequestBody Usuario usuarioAtualizado ){
        return userService.atualizarUsuario(id,usuarioAtualizado);
    }

}