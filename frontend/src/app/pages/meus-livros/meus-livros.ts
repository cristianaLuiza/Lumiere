import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { LivroService } from '../../core/services/livro.service';
import { Livro } from '../../core/models/livro';

@Component({
  selector: 'app-meus-livros',
  imports: [CommonModule,FormsModule, RouterModule],
  templateUrl: './meus-livros.html',
  styleUrl: './meus-livros.css',
})
export class MeusLivros implements OnInit{
meusLivros: Livro[] = [];

constructor(
  private livroService: LivroService,
  private cdr: ChangeDetectorRef

) {}

  ngOnInit(): void {
    // Chama a função assim que a tela carrega
    this.carregarLivrosDoUsuario();
  }

  carregarLivrosDoUsuario(): void {
    const idDoUsuarioLogado = 1; // Substitua pelo ID do usuário simulado ou logado

    this.livroService.listarLivroId(idDoUsuarioLogado).subscribe({
      next: (dados) => {
        this.meusLivros = dados;
        console.log("DADOS:",dados)
        this.cdr.detectChanges();
      },
      error: (erro) => {
        console.error('Erro ao carregar livros do usuário:', erro);
      }
    });
  }

  abrirModalCadastro() { }
}

