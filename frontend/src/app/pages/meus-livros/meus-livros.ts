import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { LivroService } from '../../core/services/livro.service';
import { Livro } from '../../core/models/livro';
import { CadastroLivro } from '../meus-livros/cadastro-livro/cadastro-livro';
import { LivroCadastrado } from '../../core/models/livroCadastrado';
import { Input } from '@angular/core';
@Component({
  selector: 'app-meus-livros',
  imports: [CommonModule,FormsModule, RouterModule, CadastroLivro],
  templateUrl: './meus-livros.html',
  styleUrl: './meus-livros.css',
})
export class MeusLivros implements OnInit{
meusLivros: Livro[] = [];
modalAberta = false;
cardExpandidoId: number | null= null;
livroSelecionado?: Livro;


constructor(
  private livroService: LivroService,
  private cdr: ChangeDetectorRef,

) {}

  ngOnInit(): void {
    // Chama a função assim que a tela carrega
    this.carregarLivrosDoUsuario();
  }

  carregarLivrosDoUsuario(): void {
    const idDoUsuarioLogado = 1;

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

  abrirModalCadastro() {
    this.modalAberta = true;
   }

   fecharModalCadastro(): void {
  this.modalAberta = false;

  // Atualiza a lista para aparecer o livro recém-cadastrado
  this.carregarLivrosDoUsuario();
}

toggleLivro(id:number):void{
  if(this.cardExpandidoId === id){
    this.cardExpandidoId = null;
  }else{
    this.cardExpandidoId = id;
  }
}

excluirLivro(id: number): void {
  this.livroService.excluirLivro(id).subscribe({
    next: () => {
      this.carregarLivrosDoUsuario();
    },
    error: (erro) => {
      console.error("Erro ao excluir!",erro);
    }

  });
}

editarLivro(livro: Livro): void {

  this.livroSelecionado = livro;

  this.modalAberta = true;
}

}

