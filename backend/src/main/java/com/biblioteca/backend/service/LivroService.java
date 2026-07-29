package com.biblioteca.backend.service;

import com.biblioteca.backend.enums.GeneroLivro;
import com.biblioteca.backend.model.Livro;
import com.biblioteca.backend.model.Usuario;
import com.biblioteca.backend.repository.LivroRepository;
import com.biblioteca.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        List<Livro> resultado = new ArrayList<>();

        adicionarTitulos(resultado, texto);
        adicionarAutores(resultado, texto);
        adicionarGeneros(resultado, texto);

        return resultado;
    }
    private void adicionarTitulos(List<Livro> resultado, String texto) {

        livroRepository.findByTituloContainingIgnoreCase(texto)
                .forEach(livro -> adicionarSeNaoExiste(resultado, livro));

    }

    private void adicionarAutores(List<Livro> resultado, String texto) {

        livroRepository.findByAutorContainingIgnoreCase(texto)
                .forEach(livro -> adicionarSeNaoExiste(resultado, livro));

    }

    private void adicionarGeneros(List<Livro> resultado, String texto) {

        try {

            GeneroLivro genero = GeneroLivro.valueOf(texto.toUpperCase());

            livroRepository.findByGeneroLivro(genero)
                    .forEach(livro -> adicionarSeNaoExiste(resultado, livro));

        } catch (IllegalArgumentException e) {
            // não era um gênero válido
        }

    }
    private void adicionarSeNaoExiste(List<Livro> resultado, Livro livro) {

        boolean existe = resultado.stream()
                .anyMatch(l -> l.getId().equals(livro.getId()));

        if (!existe) {
            resultado.add(livro);
        }
    }

    public Livro atualizarLivro(long id, Livro livroAtualizado){
        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        livroExistente.setTitulo(livroAtualizado.getTitulo());
        livroExistente.setAutor(livroAtualizado.getAutor());
        livroExistente.setDescricao(livroAtualizado.getDescricao());
        livroExistente.setGeneroLivro(livroAtualizado.getGeneroLivro());
        livroExistente.setTipoInteracao(livroAtualizado.getTipoInteracao());



        return livroRepository.save(livroExistente);
    }


    public String excluirLivro(Long id) {
       if(!livroRepository.existsById(id)){
           throw new RuntimeException("O livro com o ID:" + id + " não foi encontrado");
       }
       livroRepository.deleteById(id);
       return "Livro excluido com sucesso!";
    }

}