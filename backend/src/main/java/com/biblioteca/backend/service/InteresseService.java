package com.biblioteca.backend.service;
import com.biblioteca.backend.enums.StatusInteresse;
import com.biblioteca.backend.enums.TipoInteracao;
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
    private LivroRepository livroRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InteresseRepository interesseRepository;

    public Interesse salvarInteresse(Interesse interesse) {
        Livro livro = buscarLivro(interesse.getLivro().getId());
        Usuario usuario = buscarUsuario(interesse.getUsuarioInteressado().getId());

        interesse.setLivro(livro);
        interesse.setUsuarioInteressado(usuario);
        interesse.setStatus(StatusInteresse.PENDENTE);
        return interesseRepository.save(interesse);
    }

    public List<Interesse> buscarPorUsuario(Long idUsuario) {
        buscarUsuario(idUsuario); // opcional, para validar se existe
        return interesseRepository.findByUsuarioInteressadoId(idUsuario);
    }

    public Livro buscarLivro(long id){
        return livroRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado!"));
    }
    public Usuario buscarUsuario(long id ){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
    }
    public void validarLivroDisponivel(Livro livro){
        if(!livro.getDisponivel()){
            throw new RuntimeException("Livro Indisponível");
        }
    }

    public Interesse aceitarouRecusar(long id, StatusInteresse status){
        Interesse interesse = interesseRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Interesse não encontrado"));
        interesse.setStatus(status);

        if(status == StatusInteresse.ACEITO){
            Livro livro = interesse.getLivro();
            livro.setDisponivel(false);
            livroRepository.save(livro);
        }
        return interesseRepository.save(interesse);
    }


    public List<Interesse> interessadosNoLivro(long usuarios_id){
    return interesseRepository.findByLivroUsuarioId(usuarios_id);
    }

    public List<Interesse> interessesEnviados(long id){
        return interesseRepository.findByUsuarioInteressadoId(id);
    }

}
