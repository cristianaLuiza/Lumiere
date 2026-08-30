import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Livro } from '../../core/models/livro';
import { LivroService } from '../../core/services/livro.service';
import { UsuarioService } from '../../core/services/usuario.service';
import { DadosUsuario } from '../../core/models/dadosUsuario'
import { HomeService } from '../../core/services/home.service';

@Component({
  selector: 'app-perfil-usuario',
  imports: [CommonModule, RouterModule],
  templateUrl: './perfil-usuario.html',
  styleUrl: './perfil-usuario.css',
})
export class PerfilUsuario implements OnInit {

  livros = signal<Livro[]>([]);
  perfil = signal<DadosUsuario | null>(null);

  constructor(
    private homeService: HomeService,
    private usuarioService: UsuarioService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {

    this.route.paramMap.subscribe(params => {

      const usuarioId = Number(params.get('id'));

      this.dadosPerfil(usuarioId);
      this.carregarLivroDoPerfil(usuarioId);

    });

  }

  dadosPerfil(usuarioId: number): void {

    this.usuarioService.buscarDadosUsuario(usuarioId).subscribe({

      next: (usuario) => {
        console.log("USUARIO", usuario);
        this.perfil.set(usuario);
      },

      error: (erro) => {
        console.error(erro);
      }

    });

  }

  carregarLivroDoPerfil(usuarioId: number): void {

    this.homeService.listarTodos(usuarioId).subscribe({

      next: (dados) => {
        this.livros.set(dados);
      },

      error: (erro) => {
        console.error(erro);
      }

    });

  }

}