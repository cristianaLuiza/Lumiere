export interface Livro {
imagemUrl: any;
    id: number;
    titulo: string;
    autor: string;
    descricao: string | null;
    disponivel: boolean | null;
    ativo: boolean;
    tipoInteracao: string | null;
    generoLivro: string | null;
  }
