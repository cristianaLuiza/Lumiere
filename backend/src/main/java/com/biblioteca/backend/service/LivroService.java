package com.biblioteca.backend.service;

import com.biblioteca.backend.enums.GeneroLivro;
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

    public List<Livro> listarTodosLivros() {
        return livroRepository.findAll();
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

    public List<Livro> filtrarLivrosNome(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) { //trim remove espaços em branco e isEmpty chega se o tamanhoo da string é igual a zero
            return livroRepository.findAll();
        }
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public Livro atualizarLivro(long id, Livro livroAtualizado){
        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        livroExistente.setTitulo(livroAtualizado.getTitulo());
        livroExistente.setAutor(livroAtualizado.getAutor());
        livroExistente.setDescricao(livroAtualizado.getDescricao());

        return livroRepository.save(livroExistente);
    }

    public List<Livro> filtrarLivroGenero(String genero){

        if(genero == null || genero.trim().isEmpty()){
            return livroRepository.findAll();
        }

        GeneroLivro generoLivro = GeneroLivro.valueOf(genero);

        return livroRepository.findByGeneroLivro(generoLivro);
    }
    public String excluirLivro(Long id) {
       if(!livroRepository.existsById(id)){
           throw new RuntimeException("O livro com o ID:" + id + " não foi encontrado");
       }
       livroRepository.deleteById(id);
       return "Livro excluido com sucesso!";
    }

}