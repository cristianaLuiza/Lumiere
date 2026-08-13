import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Livro } from '../../../core/models/livro';
import { LivroCadastrado } from '../../../core/models/livroCadastrado';
import { LivroService } from '../../../core/services/livro.service';

@Component({
  selector: 'app-cadastro-livro',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './cadastro-livro.html',
  styleUrl: './cadastro-livro.css',
})
export class CadastroLivro implements OnInit {

  @Output()
  fechar = new EventEmitter<void>();

  @Input()
  livro?: Livro;

  modoEdicao = false;
  idLivroEdicao = 0;

  novoLivro: LivroCadastrado = {
    titulo: '',
    autor: '',
    descricao: '',
    generoLivro: '',
    tipoInteracao: '',
    //imagemUrl: ''
  };

  constructor(private livroService: LivroService) {}

  ngOnInit(): void {

    if (this.livro) {

      this.modoEdicao = true;
      this.idLivroEdicao = this.livro.id;

      this.novoLivro = {
        titulo: this.livro.titulo,
        autor: this.livro.autor,
        descricao: this.livro.descricaoLivro ?? '',
        generoLivro: this.livro.generoLivro ?? '',
        tipoInteracao: this.livro.tipoInteracao ?? '',
      //  imagemUrl: this.livro.imagemUrl ?? ''
      };
    }
  }

  salvarLivro(): void {

    const idUsuario = 1;

    this.livroService.criarLivro(idUsuario, this.novoLivro)
      .subscribe({
        next: (livro) => {

          console.log('Livro cadastrado:', livro);

          this.fecharModal();

        },
        error: (erro) => {
          console.error('Erro ao cadastrar livro', erro);
        }
      });
  }

  atualizarLivro(): void {

    this.livroService
      .atualizarLivro(this.idLivroEdicao, this.novoLivro)
      .subscribe({
        next: (resposta) => {

          console.log('Livro atualizado com sucesso!', resposta);

          this.fecharModal();

        },
        error: (erro) => {
          console.error('Erro ao atualizar', erro);
        }
      });
  }

  fecharModal(): void {
    this.fechar.emit();
  }
}