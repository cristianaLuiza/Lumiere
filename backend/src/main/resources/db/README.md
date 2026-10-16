# Objetos do banco (SQL Server)

O arquivo `objetos.sql` cria ou atualiza (`CREATE OR ALTER`) todas as views, functions, stored procedures e triggers. O backend o executa sozinho ao iniciar, logo depois que o Hibernate cria as tabelas (`spring.sql.init.*` em `application.properties`).

## Stored procedures

| Procedure | O que faz | Chamada pelo Java | Endpoint |
|---|---|---|---|
| `sp_cadastrar_usuario` | Valida usuário e senha, impede username repetido e devolve o id gerado (`OUTPUT`). | `UserRepository.cadastrarUsuario` ← `CadastroService` | `POST /cadastro` |
| `sp_aceitar_interesse` | Aceita um interesse pendente e cria a conversa entre o dono e o interessado, em transação. | `InteresseRepository.aceitarInteresse` ← `InteresseService` | `POST /interesse/{id}/criarChat` |
| `sp_excluir_livro` | Exclusão lógica do livro (`ativo = 0`). | `LivroRepository.excluirLivro` ← `LivroService` | `DELETE /livros/{id}` |

## Views

| View | Consulta | Usada em | Endpoint |
|---|---|---|---|
| `vw_livros_disponiveis` | Livros ativos e disponíveis + dados do dono + total de interesses pendentes (`JOIN` + `OUTER APPLY`). | `HomeService.listarLivros` | `GET /home/{usuarioId}` |
| `vw_interesses_detalhados` | Interesse + livro + dono + interessado (4 tabelas). | `InteresseRepository.listarEnviados/listarRecebidos` | `GET /interesse/{usuarioId}/enviados` e `/recebidos` |
| `vw_estatisticas_usuario` | Indicadores por usuário com CTEs, agregações e `LEFT JOIN`. | `UserService.estatisticas` | `GET /usuarios/{usuarioId}/estatisticas` |

## Functions

| Function | Tipo | Usada em | Endpoint |
|---|---|---|---|
| `fn_qtd_interesses_pendentes(@usuario_id)` | Escalar (`RETURNS INT`) | `UserService.estatisticas` | `GET /usuarios/{usuarioId}/estatisticas` (`interessesPendentes`) |
| `fn_buscar_livros(@texto)` | Retorna tabela (inline, `RETURNS TABLE`) | `LivroRepository.buscarPorTexto` | `GET /livros?texto=...` |
| `fn_livros_recomendados(@usuario_id)` | Retorna variável do tipo tabela (`RETURNS @recomendados TABLE`) | `HomeService.listarRecomendados` | `GET /home/{usuarioId}/recomendados` |

## Triggers

| Trigger | Evento | Regra | Disparada por |
|---|---|---|---|
| `trg_interesse_validar` | `AFTER INSERT` em `interesse` | Bloqueia interesse em livro próprio, inativo, indisponível ou repetido. | `POST /interesse` |
| `trg_interesse_aceito` | `AFTER UPDATE` em `interesse` | Ao aceitar, marca o livro como indisponível e recusa os outros interesses pendentes. | `sp_aceitar_interesse` (`POST /interesse/{id}/criarChat`) |
| `trg_livro_inativado` | `AFTER UPDATE` em `livros` | Ao excluir (inativar) um livro, recusa os interesses pendentes. | `sp_excluir_livro` (`DELETE /livros/{id}`) |

Os erros lançados com `THROW` (códigos 50000+) chegam ao frontend como HTTP 400 com a mensagem em português.

## Backup e restore

Os scripts ficam em `backup/` e gravam/leem o arquivo na pasta de backup padrão da instância (`SERVERPROPERTY('InstanceDefaultBackupPath')`).

```powershell
# Backup completo (com CHECKSUM e verificação do arquivo)
sqlcmd -S .\SQLEXPRESS -E -C -f 65001 -i backup\backup.sql -v DB=biblioteca_comunitaria ARQUIVO=lumiere.bak

# Restore (pare o backend antes)
sqlcmd -S .\SQLEXPRESS -E -C -f 65001 -i backup\restore.sql -v DB=biblioteca_comunitaria ARQUIVO=lumiere.bak
```

Demonstração sugerida: fazer o backup, apagar alguns registros (ou um objeto, como `DROP VIEW vw_estatisticas_usuario`), rodar o restore e mostrar que dados e objetos voltaram.

Para Linux/Docker com agendamento, retenção e cópia de `uploads/`, use `backup/backup.sh`.
