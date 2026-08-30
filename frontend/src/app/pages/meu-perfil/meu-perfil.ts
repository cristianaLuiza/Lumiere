import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Livro } from '../../core/models/livro';
import { DadosUsuario } from '../../core/models/dadosUsuario';
import { HomeService } from '../../core/services/home.service';
import { UsuarioService } from '../../core/services/usuario.service';

@Component({
  selector: 'app-meu-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './meu-perfil.html',
   styleUrl: './meu-perfil.css',

})export class MeuPerfil implements OnInit {

  livros = signal<Livro[]>([]);
  perfil = signal<DadosUsuario | null>(null);

  // Temporário: usuário logado
  usuarioId = 1;

  editando = false;

  dadosEdicao: DadosUsuario = {
    usuarioId: 0,
    nomeUsuario: '',
    descricaoUsuario: '',
    fotoUsuario: ''
  };

  constructor(
    private homeService: HomeService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    this.dadosPerfil();
    this.carregarLivroDoPerfil();
  }

  dadosPerfil(): void {

    this.usuarioService.buscarDadosUsuario(this.usuarioId).subscribe({

      next: (usuario) => {

        console.log('USUARIO:', usuario);

        this.perfil.set(usuario);

        this.dadosEdicao = {
          ...usuario
        };
      },

      error: (erro) => {
        console.error('Erro ao buscar usuário:', erro);
      }

    });
  }

  carregarLivroDoPerfil(): void {

    this.homeService.listarTodos(this.usuarioId).subscribe({

      next: (dados) => {
        this.livros.set(dados);
      },

      error: (erro) => {
        console.error('Erro ao carregar livros:', erro);
      }

    });
  }

  iniciarEdicao(): void {

    const usuario = this.perfil();

    if (!usuario) {
      return;
    }

    this.dadosEdicao = {
      ...usuario
    };

    this.editando = true;
  }

  cancelarEdicao(): void {

    const usuario = this.perfil();

    if (usuario) {
      this.dadosEdicao = {
        ...usuario
      };
    }

    this.editando = false;
  }

  salvarPerfil(): void {

    this.usuarioService
      .atualizarPerfil(this.usuarioId, this.dadosEdicao)
      .subscribe({

        next: (usuarioAtualizado) => {

          console.log('Perfil atualizado:', usuarioAtualizado);

          this.perfil.set(usuarioAtualizado);

          this.editando = false;
        },

        error: (erro) => {

          console.error('Erro ao atualizar perfil:', erro);

        }

      });
  }

  selecionarFoto(event: any): void {

  const arquivo = event.target.files[0];

  if (!arquivo) {
    return;
  }

  this.usuarioService.atualizarFoto(this.usuarioId, arquivo)
    .subscribe({

      next: () => {
        console.log('Foto atualizada!');

        this.dadosPerfil();
      },

      error: (erro) => {
        console.error('Erro ao atualizar foto:', erro);
      }

    });
}
}
