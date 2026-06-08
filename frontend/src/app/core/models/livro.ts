export interface Livro {
    id: number;
    titulo: string;
    autor: string;
    descricao: string | null;
    disponivel: boolean | null;
    ativo: boolean;
    tipoInteracao: string | null;
    generoLivro: string | null;
  }
