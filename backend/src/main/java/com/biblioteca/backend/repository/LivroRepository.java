package com.biblioteca.backend.repository;


import com.biblioteca.backend.admin.dto.LivroAdminDTO;
import com.biblioteca.backend.model.Livro;
import com.biblioteca.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

        @Query(value = """
                SELECT l.id, l.titulo, l.autor,
                       l.descricao_livro AS descricaoLivro,
                       l.genero_livro AS generoLivro,
                       l.tipo_interacao AS tipoInteracao,
                       l.disponivel, l.ativo,
                       u.id AS usuarioId,
                       u.nome_usuario AS nomeUsuario
                FROM dbo.livros l
                LEFT JOIN dbo.usuarios u ON u.id = l.usuario_id
                ORDER BY l.id DESC
                """, nativeQuery = true)
        List<LivroAdminDTO> listarParaAdmin();

        @Transactional
        @Modifying
        @Query(value = "UPDATE dbo.livros SET disponivel = :disponivel WHERE id = :id AND ativo = 1", nativeQuery = true)
        int alterarDisponibilidade(@Param("id") Long id, @Param("disponivel") boolean disponivel);

        @Transactional
        @Modifying
        @Query(value = "UPDATE dbo.livros SET ativo = 1 WHERE id = :id AND ativo = 0", nativeQuery = true)
        int restaurar(@Param("id") Long id);


    }
