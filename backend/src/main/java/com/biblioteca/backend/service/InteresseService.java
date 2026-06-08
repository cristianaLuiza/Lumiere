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

    public String salvarInteresse(Interesse interesse){

        Livro livroDesejado = interesse.getLivro();

        if(!livroDesejado.getDisponivel()){
            throw new RuntimeException("Livro indisponível!");
        }

        interesse.setStatus(StatusInteresse.PENDENTE);

        interesseRepository.save(interesse);

        Usuario usuarioInteressado = interesse.getUsuarioInteressado();
        Usuario usuarioDono = livroDesejado.getUsuario();

        if(livroDesejado.getTipoInteracao() == TipoInteracao.DOACAO){
            return "Sucesso! O dono do livro será notificado.";
        }

        if(livroDesejado.getTipoInteracao() == TipoInteracao.TROCA){

            boolean existeInteresseMutuo =
                    interesseRepository.existsByUsuarioInteressadoAndLivroUsuario(
                            usuarioDono,
                            usuarioInteressado
                    );

            if(existeInteresseMutuo){
                return "É um Match de Troca!";
            }

            return "Interesse registrado! Se o dono gostar de algum livro seu, o match acontece.";
        }

        return "Interesse registrado!";
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
