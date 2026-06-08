import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'
import { LivroService } from '../../core/services/livro.service';
import { Livro } from '../../core/models/livro';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  
  livros: Livro[] = [];
  tituloBusca = '';
  generoBusca = '';

  constructor(private livroService: LivroService) {}

carregarLivros(): void {

  this.livroService.listarTodos().subscribe({
    next: (dados) => {
      this.livros = dados;
    },
    error: (erro) => {
      console.error(erro);
    }
  });
}

  ngOnInit(): void {
  this.carregarLivros();
  }

 buscarLivroTitulo(): void {

  if (!this.tituloBusca.trim()) {
    this.carregarLivros();
    return;
  }

  this.livroService
    .filtrarPorTitulo(this.tituloBusca)
    .subscribe({
      next: (dados) => {
        this.livros = dados;
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

  this.livroService
    .filtrarPorGenero(this.generoBusca)
    .subscribe({
      next: (dados) => {
        this.livros = dados;
      },
      error: (erro) => {
        console.error(erro);
      }
    });
}

}
