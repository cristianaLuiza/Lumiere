package com.biblioteca.backend.service;
import com.biblioteca.backend.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import com.biblioteca.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Usuario salvar(Usuario usuario){
       return this.userRepository.save(usuario);
    }
}
