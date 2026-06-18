import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Livro } from '../models/livro';
import { LivroCadastrado } from '../../core/models/livroCadastrado'

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

listarLivroId(idUsuario: number):Observable<Livro[]>{
  return this.http.get<Livro[]>(`${this.apiUrl}/usuario/${idUsuario}`)
}
  
criarLivro(idUsuario: number, livro: LivroCadastrado): Observable<Livro> {
  return this.http.post<Livro>(
    `${this.apiUrl}/usuario/${idUsuario}`,
    livro
  );
}

excluirLivro(id: number): Observable<string> {
  return this.http.delete(
    `${this.apiUrl}/${id}`,
    { responseType: 'text' }
  );
}
}
