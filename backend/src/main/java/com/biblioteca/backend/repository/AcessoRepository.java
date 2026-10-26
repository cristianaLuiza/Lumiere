package com.biblioteca.backend.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.backend.model.Acesso;

@Repository
public interface AcessoRepository extends JpaRepository<Acesso, Long> {

    long countByDataAcessoAfter(LocalDateTime dataAcesso);
}
