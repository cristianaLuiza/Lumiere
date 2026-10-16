package com.biblioteca.backend.service;

import com.biblioteca.backend.model.Livro;
import com.biblioteca.backend.model.Usuario;
import com.biblioteca.backend.repository.LivroRepository;
import com.biblioteca.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private UserRepository userRepository;



    public Livro salvar(Livro livro, Long id) {
        Optional<Usuario> usuario = userRepository.findById(id);
        if (usuario.isPresent()) {
            livro.setUsuario(usuario.get());
            return livroRepository.save(livro);
        } else {
            throw new RuntimeException("Usuário não encontrado");
        }
    }

    public List<Livro> listarLivrosPorUsuario(Long id) {
        Optional<Usuario> usuario = userRepository.findById(id);
        if (usuario.isPresent()) {
            return livroRepository.findByUsuario(usuario.get());
        } else {
            throw new RuntimeException("Usuário não encontrado, livros não podem ser exibidos");
        }

    }

    public Livro buscarPorId(long id){
        return livroRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro do id não encontrado!"));

    }

    public List<Livro> buscarDadosLivro(String texto) {

        if (texto == null || texto.isBlank()) {
            return livroRepository.findAll();
        }

        return livroRepository.buscarPorTexto(texto.trim());
    }

    public Livro atualizarLivro(long id, Livro livroAtualizado){
        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        livroExistente.setTitulo(livroAtualizado.getTitulo());
        livroExistente.setAutor(livroAtualizado.getAutor());
        livroExistente.setDescricaoLivro(livroAtualizado.getDescricaoLivro());
        livroExistente.setGeneroLivro(livroAtualizado.getGeneroLivro());
        livroExistente.setTipoInteracao(livroAtualizado.getTipoInteracao());



        return livroRepository.save(livroExistente);
    }


    public String excluirLivro(Long id) {
       livroRepository.excluirLivro(id);
       return "Livro excluido com sucesso!";
    }

}