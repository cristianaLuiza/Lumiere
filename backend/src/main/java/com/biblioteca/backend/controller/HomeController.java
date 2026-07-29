package com.biblioteca.backend.controller;


import com.biblioteca.backend.dto.LivroHomeDTO;
import com.biblioteca.backend.model.Livro;
import com.biblioteca.backend.service.HomeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/home")
@CrossOrigin(origins = "http://localhost:4200")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/{usuarioId}")
    public List<LivroHomeDTO> listarLivros(@PathVariable Long usuarioId) {
        return homeService.listarLivros(usuarioId);
    }
}
