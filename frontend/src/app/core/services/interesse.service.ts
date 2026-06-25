import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Interesse } from '../models/interesse.ts'


@Injectable({
  providedIn: 'root'
})
export class InteresseService {

  private apiUrl = 'http://localhost:8081/interesse';

  constructor(private http: HttpClient) {}

  salvarInteresse(interesse: any): Observable<Interesse> {
  return this.http.post<Interesse>(
    this.apiUrl,
    interesse
  );
}

buscarPorUsuario(idUsuario: number) {
  return this.http.get<any[]>(
    `${this.apiUrl}/usuario/${idUsuario}`
  );
}
}