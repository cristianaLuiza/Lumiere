import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { InteresseService } from '../../core/services/interesse.service';
import {Interesse } from '../../core/models/interesse.ts'
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-interesses',
  imports: [CommonModule, RouterModule],
  templateUrl: './interesses.html',
  styleUrl: './interesses.css',
})
export class Interesses implements OnInit{

  interesses: Interesse[] = [];

  constructor(
    private interesseService: InteresseService,
     private cdr: ChangeDetectorRef,
  ) {}


  ngOnInit(): void {
    this.carregarInteresses();
  }

  carregarInteresses(): void {

    const idUsuario = 1; // depois pegar do usuário logado

    this.interesseService.listarInteressesRecebidos(idUsuario).subscribe({
    next: (dados) => {
      console.log("Recebi:", dados);
      this.interesses = dados;
         this.cdr.detectChanges();
      console.log("Quantidade:", this.interesses.length);
    },
    error: (erro) => console.error(erro)
  });

  }
  recusar(id:number):void{
    this.interesseService.recusarInteresse(id)
      .subscribe({
        next:() => {
          this.carregarInteresses();
        },
        error:erro => console.error(erro, "Erro:  ao recusar")
      })
  }
  aceitar(id:number):void{
    this.interesseService.aceitarInteresse(id)
    .subscribe({
      next:() => {
        this.carregarInteresses();
      },
      error:erro => console.error(erro, "Erro:  ao aceitar")
    })
  }

}
