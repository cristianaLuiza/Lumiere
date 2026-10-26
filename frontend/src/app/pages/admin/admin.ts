import { Component, OnInit, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AdminService } from '../../core/services/admin.service';
import { UsuarioService } from '../../core/services/usuario.service';
import {
  EstatisticasAdmin,
  LivroAdmin,
  LivroAdminForm,
  UsuarioAdmin,
  UsuarioAdminForm
} from '../../core/models/admin.model';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class AdminComponent implements OnInit {
  // Estado dos Dados
  estatisticas = signal<EstatisticasAdmin>({
    totalLivros: 0,
    totalUsuario: 0,
    novosUsuarios: 0,
    logins24h: 0,
    trocasConcluidas: 0,
    totalInteresses: 0,
    interessesPendentes: 0
  });

  readonly generos = ['Romance', 'Fantasia', 'Ficcao', 'Cientifica', 'Suspense', 'Policial', 'Terror', 'Historica'];
  readonly modalidades = ['TROCA', 'DOACAO'];

  pessoaEmEdicao = signal<UsuarioAdmin | 'nova' | null>(null);
  abaPessoa = signal<'perfil' | 'acesso'>('perfil');
  formPessoa: UsuarioAdminForm = this.pessoaVazia();

  livroEmCriacao = signal<boolean>(false);
  abaLivro = signal<'obra' | 'classificacao'>('obra');
  formLivro: LivroAdminForm = this.livroVazio();

  salvando = signal<boolean>(false);

  livros = signal<LivroAdmin[]>([]);
  usuarios = signal<UsuarioAdmin[]>([]);

  // Estados de Controle de Interface
  carregando = signal<boolean>(true);
  abaAtiva = signal<'visao-geral' | 'livros' | 'usuarios' | 'relatorios'>('visao-geral');

  // Filtros de Livros
  buscaLivro = signal<string>('');
  filtroGenero = signal<string>('TODOS');
  filtroTipoInteracao = signal<string>('TODOS');
  filtroDisponibilidade = signal<string>('TODOS');

  // Filtros de Usuários
  buscaUsuario = signal<string>('');
  filtroTipoUsuario = signal<string>('TODOS');

  // Modais e Ações
  livroParaExcluir = signal<LivroAdmin | null>(null);
  livroDetalhes = signal<LivroAdmin | null>(null);
  usuarioDetalhes = signal<UsuarioAdmin | null>(null);
  excluindo = signal<boolean>(false);

  // Alertas / Mensagens
  mensagemSucesso = signal<string | null>(null);
  mensagemErro = signal<string | null>(null);

  // Livros Filtrados (Computado)
  livrosFiltrados = computed(() => {
    const termo = this.buscaLivro().trim().toLowerCase();
    const genero = this.filtroGenero();
    const tipo = this.filtroTipoInteracao();
    const disponibilidade = this.filtroDisponibilidade();

    return this.livros().filter(livro => {
      const atendeBusca = !termo ||
        livro.titulo?.toLowerCase().includes(termo) ||
        livro.autor?.toLowerCase().includes(termo) ||
        livro.id.toString().includes(termo);

      const atendeGenero = genero === 'TODOS' || livro.generoLivro === genero;
      const atendeTipo = tipo === 'TODOS' || livro.tipoInteracao === tipo;
      const atendeDisponibilidade =
        disponibilidade === 'TODOS' ||
        (disponibilidade === 'DISPONIVEL' && livro.disponivel) ||
        (disponibilidade === 'INDISPONIVEL' && !livro.disponivel);

      return atendeBusca && atendeGenero && atendeTipo && atendeDisponibilidade;
    });
  });

  // Usuários Filtrados (Computado)
  usuariosFiltrados = computed(() => {
    const termo = this.buscaUsuario().trim().toLowerCase();
    const tipo = this.filtroTipoUsuario();

    return this.usuarios().filter(u => {
      const atendeBusca = !termo ||
        u.nomeUsuario?.toLowerCase().includes(termo) ||
        u.username?.toLowerCase().includes(termo) ||
        u.id.toString().includes(termo);

      const atendeTipo = tipo === 'TODOS' || u.tipoUsuario === tipo;

      return atendeBusca && atendeTipo;
    });
  });

  // Gêneros Únicos para Filtro
  generosDisponiveis = computed(() => {
    const set = new Set<string>();
    this.livros().forEach(l => {
      if (l.generoLivro) set.add(l.generoLivro);
    });
    return Array.from(set).sort();
  });

  // Tipos de Interação Únicos para Filtro
  tiposInteracaoDisponiveis = computed(() => {
    const set = new Set<string>();
    this.livros().forEach(l => {
      if (l.tipoInteracao) set.add(l.tipoInteracao);
    });
    return Array.from(set).sort();
  });

  // Métricas Derivadas
  totalLivrosDisponiveis = computed(() => this.livros().filter(l => l.disponivel).length);
  totalLivrosIndisponiveis = computed(() => this.livros().filter(l => !l.disponivel).length);
  totalAdmins = computed(() => this.usuarios().filter(u => u.tipoUsuario === 'ADMIN').length);
  totalComuns = computed(() => this.usuarios().filter(u => u.tipoUsuario !== 'ADMIN').length);

  taxaInteressesPendentes = computed(() => {
    const total = this.estatisticas().totalInteresses;
    const pend = this.estatisticas().interessesPendentes;
    if (!total || total === 0) return 0;
    return Math.round((pend / total) * 100);
  });

  constructor(
    private adminService: AdminService,
    public usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    const idUsuario = this.usuarioService.obterIdUsuarioLogado();
    if (idUsuario) {
      this.usuarioService.carregarUsuarioLogado(idUsuario).subscribe({
        error: () => {}
      });
    }
    this.carregarTodosDados();
  }

  carregarTodosDados(): void {
    this.carregando.set(true);
    this.limparMensagens();

    // Carrega estatísticas do backend
    this.adminService.obterEstatisticasCompletas().subscribe({
      next: (stats) => {
        this.estatisticas.set(stats);
      },
      error: (err) => {
        console.error('Erro ao carregar estatísticas:', err);
        this.mensagemErro.set('Não foi possível carregar as métricas do painel.');
      }
    });

    // Carrega livros
    this.adminService.listarLivros().subscribe({
      next: (lista) => {
        this.livros.set(lista || []);
      },
      error: (err) => {
        console.error('Erro ao listar livros:', err);
      }
    });

    // Carrega usuários
    this.adminService.listarUsuarios().subscribe({
      next: (lista) => {
        this.usuarios.set(lista || []);
        this.carregando.set(false);
      },
      error: (err) => {
        console.error('Erro ao listar usuários:', err);
        this.carregando.set(false);
      }
    });
  }

  trocarAba(aba: 'visao-geral' | 'livros' | 'usuarios' | 'relatorios'): void {
    this.abaAtiva.set(aba);
    this.limparMensagens();
  }

  // Ações de Livro
  abrirModalExcluirLivro(livro: LivroAdmin): void {
    this.livroParaExcluir.set(livro);
  }

  cancelarExclusaoLivro(): void {
    this.livroParaExcluir.set(null);
  }

  confirmarExclusaoLivro(): void {
    const livro = this.livroParaExcluir();
    if (!livro) return;

    this.excluindo.set(true);
    this.adminService.excluirLivro(livro.id).subscribe({
      next: () => {
        this.excluindo.set(false);
        this.livroParaExcluir.set(null);
        this.carregarTodosDados();
        this.mostrarMensagemSucesso(`Livro "${livro.titulo}" removido do catálogo.`);
      },
      error: (err) => {
        this.excluindo.set(false);
        console.error('Erro ao excluir livro:', err);
        this.mostrarMensagemErro(`Falha ao excluir o livro "${livro.titulo}". Verifique se há vínculos pendentes.`);
      }
    });
  }

  alterarLivro(livro: LivroAdmin, acao: 'suspender' | 'reativar' | 'restaurar'): void {
    const mensagens = {
      suspender: 'suspenso',
      reativar: 'reativado',
      restaurar: 'restaurado'
    };

    this.adminService.alterarLivro(livro.id, acao).subscribe({
      next: () => {
        this.carregarTodosDados();
        this.mostrarMensagemSucesso(`Livro "${livro.titulo}" ${mensagens[acao]}.`);
      },
      error: (err) => this.mostrarMensagemErro(err.error || 'Não foi possível alterar o livro.')
    });
  }

  abrirNovoLivro(): void {
    this.formLivro = { ...this.livroVazio(), usuarioId: this.usuarioService.obterIdUsuarioLogado() || null };
    this.abaLivro.set('obra');
    this.livroEmCriacao.set(true);
  }

  salvarLivro(): void {
    this.salvando.set(true);
    this.adminService.criarLivro(this.formLivro).subscribe({
      next: () => {
        this.salvando.set(false);
        this.livroEmCriacao.set(false);
        this.carregarTodosDados();
        this.mostrarMensagemSucesso(`Livro "${this.formLivro.titulo}" cadastrado.`);
      },
      error: (err) => {
        this.salvando.set(false);
        this.mostrarMensagemErro(err.error || 'Não foi possível cadastrar o livro.');
      }
    });
  }

  abrirPessoa(usuario?: UsuarioAdmin): void {
    this.formPessoa = usuario
      ? {
          username: usuario.username,
          senha: '',
          nomeUsuario: usuario.nomeUsuario,
          descricaoUsuario: usuario.descricaoUsuario ?? '',
          tipoUsuario: usuario.tipoUsuario === 'ADMIN' ? 'ADMIN' : 'COMUM'
        }
      : this.pessoaVazia();
    this.abaPessoa.set('perfil');
    this.pessoaEmEdicao.set(usuario ?? 'nova');
  }

  salvarPessoa(): void {
    const pessoa = this.pessoaEmEdicao();
    const id = pessoa && pessoa !== 'nova' ? pessoa.id : undefined;

    this.salvando.set(true);
    this.adminService.salvarUsuario(this.formPessoa, id).subscribe({
      next: (salvo) => {
        this.salvando.set(false);
        this.pessoaEmEdicao.set(null);
        this.carregarTodosDados();
        this.mostrarMensagemSucesso(`Usuário "${salvo.nomeUsuario}" ${id ? 'atualizado' : 'criado'}.`);
      },
      error: (err) => {
        this.salvando.set(false);
        this.mostrarMensagemErro(err.error || 'Não foi possível salvar o usuário.');
      }
    });
  }

  private pessoaVazia(): UsuarioAdminForm {
    return { username: '', senha: '', nomeUsuario: '', descricaoUsuario: '', tipoUsuario: 'COMUM' };
  }

  private livroVazio(): LivroAdminForm {
    return {
      titulo: '',
      autor: '',
      descricaoLivro: '',
      generoLivro: '',
      tipoInteracao: 'TROCA',
      usuarioId: null
    };
  }

  abrirDetalhesLivro(livro: LivroAdmin): void {
    this.livroDetalhes.set(livro);
  }

  fecharDetalhesLivro(): void {
    this.livroDetalhes.set(null);
  }

  abrirDetalhesUsuario(usuario: UsuarioAdmin): void {
    this.usuarioDetalhes.set(usuario);
  }

  fecharDetalhesUsuario(): void {
    this.usuarioDetalhes.set(null);
  }

  limparFiltrosLivros(): void {
    this.buscaLivro.set('');
    this.filtroGenero.set('TODOS');
    this.filtroTipoInteracao.set('TODOS');
    this.filtroDisponibilidade.set('TODOS');
  }

  limparFiltrosUsuarios(): void {
    this.buscaUsuario.set('');
    this.filtroTipoUsuario.set('TODOS');
  }

  mostrarMensagemSucesso(msg: string): void {
    this.mensagemSucesso.set(msg);
    setTimeout(() => {
      if (this.mensagemSucesso() === msg) {
        this.mensagemSucesso.set(null);
      }
    }, 4500);
  }

  mostrarMensagemErro(msg: string): void {
    this.mensagemErro.set(msg);
    setTimeout(() => {
      if (this.mensagemErro() === msg) {
        this.mensagemErro.set(null);
      }
    }, 5500);
  }

  limparMensagens(): void {
    this.mensagemSucesso.set(null);
    this.mensagemErro.set(null);
  }
}
