import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, map, Observable } from 'rxjs';
import {
  EstatisticasAdmin,
  LivroAdmin,
  LivroAdminForm,
  RelatorioAdmin,
  UsuarioAdmin,
  UsuarioAdminForm
} from '../models/admin.model';
import { UsuarioService } from './usuario.service';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = 'http://localhost:8081/admin';

  constructor(
    private http: HttpClient,
    private usuarioService: UsuarioService
  ) {}

  private get opcoes() {
    return { headers: { 'X-Usuario-Id': String(this.usuarioService.obterIdUsuarioLogado()) } };
  }

  obterRelatorios(): Observable<RelatorioAdmin> {
    return this.http.get<RelatorioAdmin>(`${this.apiUrl}/relatorios`, this.opcoes);
  }

  contarInteresses(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/relatorios/interesses`, this.opcoes);
  }

  contarInteressesPendentes(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/relatorios/interesses/pendentes`, this.opcoes);
  }

  listarLivros(): Observable<LivroAdmin[]> {
    return this.http.get<LivroAdmin[]>(`${this.apiUrl}/livros`, this.opcoes);
  }

  criarLivro(livro: LivroAdminForm): Observable<unknown> {
    return this.http.post(`${this.apiUrl}/livros`, livro, this.opcoes);
  }

  alterarLivro(id: number, acao: 'suspender' | 'reativar' | 'restaurar'): Observable<unknown> {
    return this.http.patch(`${this.apiUrl}/livros/${id}/${acao}`, null, this.opcoes);
  }

  excluirLivro(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/livros/${id}`, { ...this.opcoes, responseType: 'text' });
  }

  listarUsuarios(): Observable<UsuarioAdmin[]> {
    return this.http.get<UsuarioAdmin[]>(`${this.apiUrl}/usuarios`, this.opcoes);
  }

  salvarUsuario(usuario: UsuarioAdminForm, id?: number): Observable<UsuarioAdmin> {
    return id
      ? this.http.put<UsuarioAdmin>(`${this.apiUrl}/usuarios/${id}`, usuario, this.opcoes)
      : this.http.post<UsuarioAdmin>(`${this.apiUrl}/usuarios`, usuario, this.opcoes);
  }

  obterEstatisticasCompletas(): Observable<EstatisticasAdmin> {
    return forkJoin({
      relatorio: this.obterRelatorios(),
      interesses: this.contarInteresses(),
      pendentes: this.contarInteressesPendentes()
    }).pipe(
      map(({ relatorio, interesses, pendentes }) => ({
        totalLivros: relatorio.totalLivros ?? 0,
        totalUsuario: relatorio.totalUsuario ?? 0,
        novosUsuarios: relatorio.novosUsuarios ?? 0,
        logins24h: relatorio.logins24h ?? 0,
        trocasConcluidas: relatorio.trocasConcluidas ?? 0,
        totalInteresses: interesses ?? 0,
        interessesPendentes: pendentes ?? 0
      }))
    );
  }
}
