package com.biblioteca.backend.repository;


import com.biblioteca.backend.model.Interesse;
import com.biblioteca.backend.model.Livro;
import com.biblioteca.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InteresseRepository extends JpaRepository<Interesse, Long> {

    boolean existsByUsuarioInteressadoAndLivroUsuario(Usuario interessado, Usuario donoDoLivro);

    List<Interesse> findByLivroUsuarioId(Long usuarios_id);

    List<Interesse> findByUsuarioInteressadoId(Long id);

    Optional<Interesse> findByLivroIdAndUsuarioInteressadoId(
            Long livroId,
            Long usuarioId);

}
