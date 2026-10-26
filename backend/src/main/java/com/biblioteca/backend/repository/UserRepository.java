package com.biblioteca.backend.repository;

import com.biblioteca.backend.enums.TipoUsuario;
import com.biblioteca.backend.model.Usuario;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

     boolean existsByUsername(String username);

     Optional<Usuario> findByUsername(String username);

     long countByTipoUsuario(TipoUsuario tipoUsuario);

     long countByDataCadastroAfter(LocalDateTime dataCadastro);

     @Transactional
     @Procedure(procedureName = "dbo.sp_cadastrar_usuario", outputParameterName = "usuario_id")
     Long cadastrarUsuario(
             @Param("username") String username,
             @Param("senha") String senha,
             @Param("nome_usuario") String nomeUsuario
     );
}