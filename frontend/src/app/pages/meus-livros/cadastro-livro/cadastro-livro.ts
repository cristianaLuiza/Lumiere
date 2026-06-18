import { Component } from '@angular/core';
import { Livro } from '../../../core/models/livro'
import { LivroService } from '../../../core/services/livro.service';
import { LivroCadastrado } from '../../../core/models/livroCadastrado'
import { FormsModule } from '@angular/forms';
import { Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-cadastro-livro',
  imports: [FormsModule],
  templateUrl: './cadastro-livro.html',
  styleUrl: './cadastro-livro.css',
})
export class CadastroLivro {
  @Output()
  fechar = new EventEmitter<void>();
livros: Livro[] = [];


  constructor(private livroService: LivroService) {}

  novoLivro: LivroCadastrado = {
  titulo: '',
  autor: '',
  descricao: '',
  generoLivro: '',
  tipoInteracao: '',
  imagemUrl: ''
  }


 salvarLivro(): void {

  const idUsuario = 1;

  this.livroService
    .criarLivro(idUsuario, this.novoLivro)
    .subscribe({
      next: (livro) => {
        console.log('Livro cadastrado:', livro);

        this.novoLivro = {
          titulo: '',
          autor: '',
          descricao: '',
          generoLivro: '',
          tipoInteracao: '',
          imagemUrl: ''
        };
        this.fecharModal();
      },
      error: (erro) => {
        console.error('Erro ao cadastrar livro', erro);
      }
    });
}

fecharModal(): void {
  this.fechar.emit();
}



}
