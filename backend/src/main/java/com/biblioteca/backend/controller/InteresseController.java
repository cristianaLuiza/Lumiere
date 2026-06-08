package com.biblioteca.backend.controller;


import com.biblioteca.backend.enums.StatusInteresse;
import com.biblioteca.backend.model.Interesse;
import com.biblioteca.backend.service.InteresseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interesse")
public class InteresseController {

    @Autowired
    private InteresseService interesseService;

    @PostMapping()
    public String salvarInteresse(@RequestBody Interesse interesse){
        return interesseService.salvarInteresse(interesse);
    }

    @GetMapping("/recebidos/usuarios/{usuarios_id}")
    public List<Interesse> interessadosNoLivro(@PathVariable long usuarios_id){
        return interesseService.interessadosNoLivro(usuarios_id);
    }

    @PutMapping("/{id}/aceitar")
    public Interesse aceitarInteresse(@PathVariable long id){
        return interesseService.aceitarouRecusar(id, StatusInteresse.ACEITO);
    }

    @PutMapping("/{id}/recusar")
    public Interesse recusarInteresse(@PathVariable long id){
        return interesseService.aceitarouRecusar(id, StatusInteresse.RECUSADO);
    }

    @GetMapping("/enviados/{id}")
    public List<Interesse> interessesEnviados(@PathVariable Long usuarios_id){
        return interesseService.interessesEnviados(usuarios_id);
    }
}
//TESTAR ROTA interessesEnviados E AS DE RECUSA E ACEITE DE INTERESSE