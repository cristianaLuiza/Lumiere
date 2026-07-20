package com.biblioteca.backend.service;

import com.biblioteca.backend.enums.StatusInteresse;
import com.biblioteca.backend.model.Interesse;
import com.biblioteca.backend.model.Livro;
import com.biblioteca.backend.model.Usuario;
import com.biblioteca.backend.repository.InteresseRepository;
import com.biblioteca.backend.repository.LivroRepository;
import com.biblioteca.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteresseService {

    @Autowired
    private InteresseRepository interesseRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UserRepository userRepository;


    public Interesse criarInteresse(Interesse interesse) {

        Livro livro = buscarLivroPorId(interesse.getLivro().getId());
        Usuario usuario = buscarUsuarioPorId(interesse.getUsuarioInteressado().getId());

        interesse.setLivro(livro);
        interesse.setUsuarioInteressado(usuario);
        interesse.setStatus(StatusInteresse.PENDENTE);

        return interesseRepository.save(interesse);
    }

    public Livro buscarLivroPorId(Long livroId) {
        return livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado."));
    }

    public Usuario buscarUsuarioPorId(Long usuarioId) {

        return userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }


    public Interesse aceitarInteresse(Long interesseId) {

        Interesse interesse = buscarInteressePorId(interesseId);

        interesse.setStatus(StatusInteresse.ACEITO);

        Livro livro = interesse.getLivro();
        livro.setDisponivel(false);

        livroRepository.save(livro);

        // TODO: Criar chat entre os usuários

        return interesseRepository.save(interesse);
    }


    public Interesse recusarInteresse(Long interesseId) {

        Interesse interesse = buscarInteressePorId(interesseId);

        interesse.setStatus(StatusInteresse.RECUSADO);

        return interesseRepository.save(interesse);
    }


    public Interesse buscarInteressePorId(Long interesseId) {

        return interesseRepository.findById(interesseId)
                .orElseThrow(() -> new RuntimeException("Interesse não encontrado."));
    }


    public List<Interesse> listarInteressesEnviados(Long usuarioId) {

        buscarUsuarioPorId(usuarioId);

        return interesseRepository.findByUsuarioInteressadoId(usuarioId);
    }

    public List<Interesse> listarInteressesRecebidos(Long usuarioId) {

        buscarUsuarioPorId(usuarioId);

        return interesseRepository.findByLivroUsuarioId(usuarioId);
    }

}