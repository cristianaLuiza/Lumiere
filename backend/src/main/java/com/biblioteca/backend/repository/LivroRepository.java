package com.biblioteca.backend.repository;


import com.biblioteca.backend.enums.GeneroLivro;
import com.biblioteca.backend.model.Livro;
import com.biblioteca.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



    @Repository
    public interface LivroRepository extends JpaRepository<Livro, Long> {

        List<Livro> findByUsuario(Usuario usuario);

        List<Livro>findByTituloContainingIgnoreCase(String titulo);

        List<Livro>findByAutorContainingIgnoreCase(String autor);

        List<Livro>findByGeneroLivro(GeneroLivro generoLivro);

        List<Livro> findByUsuarioIdNot(Long usuarioId);


    }
