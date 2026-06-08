import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Livro } from '../models/livro';

@Injectable({
  providedIn: 'root'
})
export class LivroService {

  private apiUrl = 'http://localhost:8081/livros';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<Livro[]> {
    return this.http.get<Livro[]>(this.apiUrl);
  }

filtrarPorTitulo(titulo: string): Observable<Livro[]> {

  const params = new HttpParams()
    .set('titulo', titulo);

  return this.http.get<Livro[]>(
    `${this.apiUrl}/filtrar`,
    { params }
  );
}
filtrarPorGenero(genero: string): Observable<Livro[]> {

  const params = new HttpParams()
    .set('genero', genero);

  return this.http.get<Livro[]>(
    `${this.apiUrl}/filtrar/genero`,
    { params }
  );
}
  
}
