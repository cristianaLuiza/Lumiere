
export interface Livro {

    id: number;
    imagemUrl: any;
    titulo: string;
    autor: string;
    descricao: string;
    disponivel: boolean;
    ativo: boolean;
    tipoInteracao: string;
    generoLivro: string;

    usuarioId: number;
    nomeUsuario: string;
    fotoUsuario: string;
}