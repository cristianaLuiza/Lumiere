import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Interesse } from '../models/interesse.ts';
import { CriarInteresse } from '../models/criarInteresse';



@Injectable({
  providedIn: 'root'
})
export class InteresseService {

  private apiUrl = 'http://localhost:8081/interesse';
  interesses$: any;

  constructor(private http: HttpClient) {}

criarInteresse(interesse: CriarInteresse) {
  return this.http.post<Interesse>(
    this.apiUrl,
    interesse
  );
}

cancelarInteresse(livroId: number, usuarioId: number) {
  return this.http.delete<void>(
    `${this.apiUrl}/${livroId}/${usuarioId}`
  );
}


aceitarInteresse(interesseId: number){
  return this.http.post(
    `${this.apiUrl}/${interesseId}/criarChat`,
    {}
  );
}

recusarInteresse(interesseId: number){
  return this.http.patch<Interesse>(
      `${this.apiUrl}/${interesseId}/recusar`,
      {}
  )
}

listarInteressesEnviados(idUsuario: number){
  return this.http.get<Interesse[]>(
    `${this.apiUrl}/${idUsuario}/enviados`
  )
}

listarInteressesRecebidos(idUsuario: number){
  return this.http.get<Interesse[]>(
    `${this.apiUrl}/${idUsuario}/recebidos`
  )
}

}