package com.biblioteca.backend.repository;


import com.biblioteca.backend.model.Livro;
import com.biblioteca.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



    @Repository
    public interface LivroRepository extends JpaRepository<Livro, Long> {

        List<Livro> findByUsuario(Usuario usuario);

        @Query(value = "SELECT * FROM dbo.fn_buscar_livros(:texto)", nativeQuery = true)
        List<Livro> buscarPorTexto(@Param("texto") String texto);

        @Transactional
        @Procedure(procedureName = "dbo.sp_excluir_livro")
        void excluirLivro(@Param("livro_id") Long livroId);


    }
