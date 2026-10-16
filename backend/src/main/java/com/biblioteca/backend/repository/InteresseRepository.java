package com.biblioteca.backend.repository;


import com.biblioteca.backend.model.Interesse;
import com.biblioteca.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InteresseRepository extends JpaRepository<Interesse, Long> {

    boolean existsByUsuarioInteressadoAndLivroUsuario(Usuario interessado, Usuario donoDoLivro);

    @Query(value = """
            SELECT i.*
            FROM dbo.interesse i
            JOIN dbo.vw_interesses_detalhados v ON v.interesse_id = i.id
            WHERE v.dono_id = :usuarioId
            """, nativeQuery = true)
    List<Interesse> listarRecebidos(@Param("usuarioId") Long usuarioId);

    @Query(value = """
            SELECT i.*
            FROM dbo.interesse i
            JOIN dbo.vw_interesses_detalhados v ON v.interesse_id = i.id
            WHERE v.interessado_id = :usuarioId
            """, nativeQuery = true)
    List<Interesse> listarEnviados(@Param("usuarioId") Long usuarioId);

    Optional<Interesse> findByLivroIdAndUsuarioInteressadoId(
            Long livroId,
            Long usuarioId);

    @Transactional
    @Procedure(procedureName = "dbo.sp_aceitar_interesse")
    void aceitarInteresse(@Param("interesse_id") Long interesseId);

}
