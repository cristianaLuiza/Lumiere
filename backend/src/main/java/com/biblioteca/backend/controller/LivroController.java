package com.biblioteca.backend.controller;


import com.biblioteca.backend.model.Livro;
import com.biblioteca.backend.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@CrossOrigin(origins = "http://localhost:4200")
public class LivroController {

    @Autowired
    private LivroService livroService;

    // POST /livros/usuario/1 (Cadastra o livro direto para o usuário dono)
    @PostMapping("/usuario/{id}")
    public Livro cadastrarLivro(@RequestBody Livro livro, @PathVariable long id){
        return livroService.salvar(livro, id);
    }



    // GET /livros
    @GetMapping
    public List<Livro> listarTodosLivros(){
        return livroService.listarTodosLivros();
    }

    // GET /livros/usuario/1 (Lista os livros que pertencem àquele usuário)
    @GetMapping("/usuario/{id}")
    public List<Livro> listarLivrosPorUsuario(@PathVariable long id){
        return livroService.listarLivrosPorUsuario(id);
    }

    //GET/id?=1
    @GetMapping("/{id}")
    public Livro buscarPorId(@PathVariable long id){
        return livroService.buscarPorId(id);
    }

    // GET /livros/filtrar?titulo=Harry
    @GetMapping("/filtrar")
    public List<Livro> filtrarLivrosNome(@RequestParam String titulo){
        return livroService.filtrarLivrosNome(titulo);
    }

    @GetMapping("/filtrar/genero")
    public List<Livro> filtrarLivroGenero(@RequestParam String genero){
        return livroService.filtrarLivroGenero(genero);
    }

    //PUT/id?=1
    @PutMapping("/{id}")
    public Livro atualizarLivro( @PathVariable long id, @RequestBody Livro livroAtualizado){
        return livroService.atualizarLivro(id,livroAtualizado);
    }

    //DELETE/id?=1
    @DeleteMapping("/{id}")
    public String excluirLivro(@PathVariable long id){
        return livroService.excluirLivro(id);
    }
}

