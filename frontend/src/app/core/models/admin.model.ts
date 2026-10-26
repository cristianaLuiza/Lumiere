export interface RelatorioAdmin {
  totalLivros: number;
  totalUsuario: number;
  novosUsuarios: number;
  logins24h: number;
  trocasConcluidas: number;
}

export interface EstatisticasAdmin extends RelatorioAdmin {
  totalInteresses: number;
  interessesPendentes: number;
}

export interface LivroAdminForm {
  titulo: string;
  autor: string;
  descricaoLivro: string;
  generoLivro: string;
  tipoInteracao: string;
  usuarioId: number | null;
}

export interface UsuarioAdminForm {
  username: string;
  senha: string;
  nomeUsuario: string;
  descricaoUsuario: string;
  tipoUsuario: 'ADMIN' | 'COMUM';
}

export interface LivroAdmin {
  id: number;
  titulo: string;
  autor: string;
  descricaoLivro?: string;
  disponivel: boolean;
  ativo: boolean;
  tipoInteracao: string;
  generoLivro: string;
  usuarioId?: number | null;
  nomeUsuario?: string | null;
}

export interface UsuarioAdmin {
  id: number;
  tipoUsuario: 'ADMIN' | 'COMUM' | string;
  username: string;
  nomeUsuario: string;
  descricaoUsuario?: string;
  fotoUsuario?: string | null;
  dataCadastro?: string | null;
}
