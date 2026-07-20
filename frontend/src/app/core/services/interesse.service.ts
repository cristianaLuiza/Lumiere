import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Interesse } from '../models/interesse.ts'


@Injectable({
  providedIn: 'root'
})
export class InteresseService {

  private apiUrl = 'http://localhost:8081/interesse';
  interesses$: any;

  constructor(private http: HttpClient) {}

criarInteresse(interesse: Interesse) {
  return this.http.post<Interesse>(
    this.apiUrl,
    interesse
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