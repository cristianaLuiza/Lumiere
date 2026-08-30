import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DadosUsuario } from '../models/dadosUsuario';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {

  private apiUrl = 'http://localhost:8081/usuarios';

  constructor(private http: HttpClient) {}

  buscarDadosUsuario(usuarioId: number): Observable<DadosUsuario> {
  return this.http.get<DadosUsuario>(
    `${this.apiUrl}/${usuarioId}`
  );
}

  atualizarPerfil(
    idUsuario: number,
    usuario: DadosUsuario
  ): Observable<DadosUsuario> {

    return this.http.put<DadosUsuario>(
      `${this.apiUrl}/${idUsuario}`,
      usuario
    );
  }

  atualizarFoto(
    idUsuario: number,
    foto: File
  ): Observable<any> {

    const formData = new FormData();

    formData.append('foto', foto);

    return this.http.put(
      `${this.apiUrl}/${idUsuario}/foto`,
      formData
    );
  }

}