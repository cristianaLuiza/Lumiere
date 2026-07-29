import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Livro } from '../models/livro';
import { LivroCadastrado } from '../../core/models/livroCadastrado'

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  private apiUrl = 'http://localhost:8081/home';

  constructor(private http: HttpClient) {}


listarTodos(idUsuario: number): Observable<Livro[]> {
  return this.http.get<Livro[]>(`${this.apiUrl}/${idUsuario}`);
}
}