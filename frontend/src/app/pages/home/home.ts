import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LivroService } from '../../core/services/livro.service';
import { Livro } from '../../core/models/livro';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { InteresseService } from '../../core/services/interesse.service'
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {

  livros = signal<Livro[]>([]);

  tituloBusca = '';
  generoBusca = '';

  matchIds = signal<Set<number>>(new Set());

  constructor(
    private livroService: LivroService,
    private interesseService: InteresseService
  ) {}

  ngOnInit(): void {
    this.carregarLivros();
    this.carregarMeusInteresses();
  }

  carregarMeusInteresses(): void {
    this.interesseService.buscarPorUsuario(1)
      .subscribe({
        next: (interesses) => {

          const ids = interesses
            .filter(i => i.status === 'PENDENTE')
            .map(i => i.livro.id);

          this.matchIds.set(new Set(ids));
        },
        error: (erro) => {
          console.error(erro);
        }
      });
  }

  carregarLivros(): void {
    this.livroService.listarTodos().subscribe({
      next: (dados) => {
        this.livros.set(dados);
      },
      error: (erro) => {
        console.error(erro);
      }
    });
  }

  buscarLivroTitulo(): void {
    if (!this.tituloBusca.trim()) {
      this.carregarLivros();
      return;
    }

    this.livroService.filtrarPorTitulo(this.tituloBusca).subscribe({
      next: (dados) => {
        this.livros.set(dados);
      },
      error: (erro) => {
        console.error(erro);
      }
    });
  }

  buscarLivroGenero(): void {
    if (!this.generoBusca.trim()) {
      this.carregarLivros();
      return;
    }

    this.livroService.filtrarPorGenero(this.generoBusca).subscribe({
      next: (dados) => {
        this.livros.set(dados);
      },
      error: (erro) => {
        console.error(erro);
      }
    });
  }

  toggleMatch(id: number): void {
    const matches = new Set(this.matchIds());

    if (matches.has(id)) {
      matches.delete(id);
    } else {
      matches.add(id);
    }

    this.matchIds.set(matches);
  }

  temMatch(id: number): boolean {
    return this.matchIds().has(id);
  }

  demonstrarInteresse(idLivro: number): void {

    const interesse = {
      livro: {
        id: idLivro
      },
      usuarioInteressado: {
        id: 1
      }
    };

    this.interesseService.salvarInteresse(interesse)
      .subscribe({

        next: (interesseSalvo) => {

          console.log('INTERESSE SALVO', interesseSalvo);

          if (interesseSalvo.status === 'PENDENTE') {
            this.toggleMatch(idLivro);
          }

        },

        error: (erro) => {
          console.error(erro);
        }

      });
  }
}