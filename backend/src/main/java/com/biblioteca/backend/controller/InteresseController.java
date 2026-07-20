package com.biblioteca.backend.controller;
import com.biblioteca.backend.model.Interesse;
import com.biblioteca.backend.service.InteresseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/interesse")
@CrossOrigin(origins = "http://localhost:4200")
public class InteresseController {

    @Autowired
    private InteresseService interesseService;

    @PostMapping
    public Interesse criarInteresse(@RequestBody Interesse interesse){
        return interesseService.criarInteresse(interesse);
    }

    @PostMapping("/{interesseId}/criarChat")
    public Interesse aceitarInteresse(@PathVariable Long interesseId){
        return interesseService.aceitarInteresse(interesseId);
    }

    @PatchMapping("/{interesseId}/recusar")
    public Interesse recusarInteresse(@PathVariable Long interesseId){
        return interesseService.recusarInteresse(interesseId);
    }

    @GetMapping("{usuarioId}/enviados")
    public List<Interesse> listarInteressesEnviados(@PathVariable Long usuarioId){
        return interesseService.listarInteressesEnviados(usuarioId);
    }

    @GetMapping("{usuarioId}/recebidos")
    public List<Interesse> listarInteressesRecebidos(@PathVariable Long usuarioId){
        return interesseService.listarInteressesRecebidos(usuarioId);
    }
}
