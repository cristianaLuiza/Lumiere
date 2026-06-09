import { Component, OnInit, signal } from '@angular/core'; // 1. Importe o 'signal'
import { CommonModule } from '@angular/common';
import { LivroService } from '../../core/services/livro.service';
import { Livro } from '../../core/models/livro';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  
  // 2. Transforme a propriedade em um Signal inicializado com array vazio
  livros = signal<Livro[]>([]); 
  tituloBusca = '';
  generoBusca = '';

  constructor(private livroService: LivroService) {}

  ngOnInit(): void {
    this.carregarLivros();
  }

  carregarLivros(): void {
    this.livroService.listarTodos().subscribe({
      next: (dados) => {
        // 3. Atualize o Signal usando o método .set()
        this.livros.set(dados); 
      },
      error: (erro) => { console.error(erro); }
    });
  }

  buscarLivroTitulo(): void {
    if (!this.tituloBusca.trim()) {
      this.carregarLivros();
      return;
    }
    this.livroService.filtrarPorTitulo(this.tituloBusca).subscribe({
      next: (dados) => {
        this.livros.set(dados); // Atualiza com .set()
      },
      error: (erro) => { console.error(erro); }
    });
  }

  buscarLivroGenero(): void {
    if (!this.generoBusca.trim()) {
      this.carregarLivros();
      return;
    }
    this.livroService.filtrarPorGenero(this.generoBusca).subscribe({
      next: (dados) => {
        this.livros.set(dados); // Atualiza com .set()
      },
      error: (erro) => { console.error(erro); }
    });
  }
}