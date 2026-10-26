-- Objetos programáveis do banco biblioteca_comunitaria.
-- O backend executa este script na inicialização, depois que o Hibernate cria as tabelas.
-- Também pode ser executado manualmente: sqlcmd -S <servidor> -d biblioteca_comunitaria -f 65001 -i objetos.sql

-- =====================================================================
-- VIEWS
-- =====================================================================

-- Livros ativos e disponíveis, com dados do dono e quantidade de interesses pendentes.
CREATE OR ALTER VIEW dbo.vw_livros_disponiveis AS
SELECT
    l.id,
    l.titulo,
    l.autor,
    l.descricao_livro,
    l.disponivel,
    l.ativo,
    l.tipo_interacao,
    l.genero_livro,
    u.id AS usuario_id,
    u.nome_usuario,
    u.foto_usuario,
    u.descricao_usuario,
    pendentes.total AS total_interesses
FROM dbo.livros l
JOIN dbo.usuarios u ON u.id = l.usuario_id
OUTER APPLY (
    SELECT COUNT(*) AS total
    FROM dbo.interesse i
    WHERE i.livro_id = l.id
      AND i.status = 'PENDENTE'
) pendentes
WHERE l.ativo = 1
  AND ISNULL(l.disponivel, 1) = 1;
GO

-- Interesses com o livro, o dono do livro e o usuário interessado.
CREATE OR ALTER VIEW dbo.vw_interesses_detalhados AS
SELECT
    i.id AS interesse_id,
    i.status,
    l.id AS livro_id,
    l.titulo,
    l.autor,
    l.genero_livro,
    l.tipo_interacao,
    l.disponivel,
    dono.id AS dono_id,
    dono.nome_usuario AS dono_nome,
    interessado.id AS interessado_id,
    interessado.username AS interessado_username,
    interessado.nome_usuario AS interessado_nome,
    interessado.descricao_usuario AS interessado_descricao,
    interessado.foto_usuario AS interessado_foto
FROM dbo.interesse i
JOIN dbo.livros l ON l.id = i.livro_id
JOIN dbo.usuarios dono ON dono.id = l.usuario_id
JOIN dbo.usuarios interessado ON interessado.id = i.usuario_interessado_id
WHERE l.ativo = 1;
GO

-- Indicadores de cada usuário: livros cadastrados, disponíveis, negociados e interesses.
CREATE OR ALTER VIEW dbo.vw_estatisticas_usuario AS
WITH livros_usuario AS (
    SELECT
        usuario_id,
        COUNT(*) AS livros_cadastrados,
        SUM(CASE WHEN disponivel = 1 THEN 1 ELSE 0 END) AS livros_disponiveis,
        SUM(CASE WHEN disponivel = 0 THEN 1 ELSE 0 END) AS livros_negociados
    FROM dbo.livros
    WHERE ativo = 1
    GROUP BY usuario_id
),
enviados AS (
    SELECT
        usuario_interessado_id AS usuario_id,
        COUNT(*) AS interesses_enviados
    FROM dbo.interesse
    GROUP BY usuario_interessado_id
),
recebidos AS (
    SELECT
        l.usuario_id,
        COUNT(*) AS interesses_recebidos,
        SUM(CASE WHEN i.status = 'ACEITO' THEN 1 ELSE 0 END) AS interesses_aceitos
    FROM dbo.interesse i
    JOIN dbo.livros l ON l.id = i.livro_id
    WHERE l.ativo = 1
    GROUP BY l.usuario_id
)
SELECT
    u.id AS usuario_id,
    u.nome_usuario,
    ISNULL(lu.livros_cadastrados, 0) AS livros_cadastrados,
    ISNULL(lu.livros_disponiveis, 0) AS livros_disponiveis,
    ISNULL(lu.livros_negociados, 0) AS livros_negociados,
    ISNULL(e.interesses_enviados, 0) AS interesses_enviados,
    ISNULL(r.interesses_recebidos, 0) AS interesses_recebidos,
    ISNULL(r.interesses_aceitos, 0) AS interesses_aceitos
FROM dbo.usuarios u
LEFT JOIN livros_usuario lu ON lu.usuario_id = u.id
LEFT JOIN enviados e ON e.usuario_id = u.id
LEFT JOIN recebidos r ON r.usuario_id = u.id;
GO

-- =====================================================================
-- FUNCTIONS
-- =====================================================================

-- Escalar: quantidade de interesses pendentes recebidos nos livros do usuário.
CREATE OR ALTER FUNCTION dbo.fn_qtd_interesses_pendentes (@usuario_id BIGINT)
RETURNS INT
AS
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM dbo.interesse i
        JOIN dbo.livros l ON l.id = i.livro_id
        WHERE l.usuario_id = @usuario_id
          AND l.ativo = 1
          AND i.status = 'PENDENTE'
    );
END;
GO

-- Retorna tabela (inline): busca de livros ativos por título, autor ou gênero.
CREATE OR ALTER FUNCTION dbo.fn_buscar_livros (@texto VARCHAR(255))
RETURNS TABLE
AS
RETURN
    SELECT
        l.id,
        l.ativo,
        l.autor,
        l.descricao_livro,
        l.disponivel,
        l.genero_livro,
        l.tipo_interacao,
        l.titulo,
        l.usuario_id
    FROM dbo.livros l
    WHERE l.ativo = 1
      AND (
          l.titulo LIKE '%' + @texto + '%'
          OR l.autor LIKE '%' + @texto + '%'
          OR l.genero_livro = @texto
      );
GO

-- Retorna variável do tipo tabela: livros recomendados pelos gêneros de interesse do usuário.
-- Sem histórico de interesses, recomenda os livros mais procurados.
CREATE OR ALTER FUNCTION dbo.fn_livros_recomendados (@usuario_id BIGINT)
RETURNS @recomendados TABLE (
    id BIGINT,
    titulo VARCHAR(255),
    autor VARCHAR(255),
    descricao_livro VARCHAR(255),
    disponivel BIT,
    ativo BIT,
    tipo_interacao VARCHAR(255),
    genero_livro VARCHAR(255),
    usuario_id BIGINT,
    nome_usuario VARCHAR(255),
    foto_usuario VARCHAR(255),
    descricao_usuario VARCHAR(255),
    total_interesses INT
)
AS
BEGIN
    DECLARE @generos TABLE (genero VARCHAR(255) PRIMARY KEY);

    INSERT INTO @generos (genero)
    SELECT DISTINCT l.genero_livro
    FROM dbo.interesse i
    JOIN dbo.livros l ON l.id = i.livro_id
    WHERE i.usuario_interessado_id = @usuario_id
      AND l.genero_livro IS NOT NULL;

    INSERT INTO @recomendados
    SELECT
        v.id, v.titulo, v.autor, v.descricao_livro, v.disponivel, v.ativo, v.tipo_interacao, v.genero_livro,
        v.usuario_id, v.nome_usuario, v.foto_usuario, v.descricao_usuario, v.total_interesses
    FROM dbo.vw_livros_disponiveis v
    WHERE v.usuario_id <> @usuario_id
      AND v.genero_livro IN (SELECT genero FROM @generos)
      AND NOT EXISTS (
          SELECT 1
          FROM dbo.interesse i
          WHERE i.livro_id = v.id
            AND i.usuario_interessado_id = @usuario_id
      );

    IF NOT EXISTS (SELECT 1 FROM @recomendados)
        INSERT INTO @recomendados
        SELECT TOP 10
            v.id, v.titulo, v.autor, v.descricao_livro, v.disponivel, v.ativo, v.tipo_interacao, v.genero_livro,
            v.usuario_id, v.nome_usuario, v.foto_usuario, v.descricao_usuario, v.total_interesses
        FROM dbo.vw_livros_disponiveis v
        WHERE v.usuario_id <> @usuario_id
          AND NOT EXISTS (
              SELECT 1
              FROM dbo.interesse i
              WHERE i.livro_id = v.id
                AND i.usuario_interessado_id = @usuario_id
          )
        ORDER BY v.total_interesses DESC, v.id DESC;

    RETURN;
END;
GO

-- =====================================================================
-- STORED PROCEDURES
-- =====================================================================

-- Valida e cadastra um usuário, devolvendo o id gerado.
CREATE OR ALTER PROCEDURE dbo.sp_cadastrar_usuario
    @username VARCHAR(255),
    @senha VARCHAR(255),
    @nome_usuario VARCHAR(255),
    @usuario_id BIGINT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    IF LTRIM(RTRIM(ISNULL(@username, ''))) = ''
        THROW 50030, N'Informe o nome de usuário', 1;

    IF LEN(ISNULL(@senha, '')) < 3
        THROW 50031, N'A senha deve conter no mínimo 3 dígitos', 1;

    IF EXISTS (SELECT 1 FROM dbo.usuarios WHERE username = @username)
        THROW 50032, N'Esse nome de usuário já existe', 1;

    INSERT INTO dbo.usuarios (username, senha, nome_usuario, tipo_usuario, data_cadastro)
    VALUES (@username, @senha, @nome_usuario, 'COMUM', SYSDATETIME());

    SET @usuario_id = SCOPE_IDENTITY();
END;
GO

-- Aceita um interesse pendente e cria a conversa entre o dono do livro e o interessado.
-- A trigger trg_interesse_aceito torna o livro indisponível e recusa os demais interesses.
CREATE OR ALTER PROCEDURE dbo.sp_aceitar_interesse
    @interesse_id BIGINT
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    DECLARE @dono_id BIGINT, @interessado_id BIGINT, @conversa_id BIGINT;

    SELECT
        @dono_id = l.usuario_id,
        @interessado_id = i.usuario_interessado_id
    FROM dbo.interesse i
    JOIN dbo.livros l ON l.id = i.livro_id
    WHERE i.id = @interesse_id
      AND i.status = 'PENDENTE'
      AND l.ativo = 1;

    IF @dono_id IS NULL
        THROW 50040, N'Interesse não encontrado ou já respondido.', 1;

    BEGIN TRANSACTION;

    UPDATE dbo.interesse
    SET status = 'ACEITO'
    WHERE id = @interesse_id;

    INSERT INTO dbo.conversa (data_criacao)
    VALUES (SYSDATETIME());

    SET @conversa_id = SCOPE_IDENTITY();

    INSERT INTO dbo.conversa_usuario (conversa_id, usuario_id)
    VALUES (@conversa_id, @dono_id), (@conversa_id, @interessado_id);

    COMMIT TRANSACTION;
END;
GO

-- Exclusão lógica de um livro. A trigger trg_livro_inativado recusa os interesses pendentes.
CREATE OR ALTER PROCEDURE dbo.sp_excluir_livro
    @livro_id BIGINT
AS
BEGIN
    SET NOCOUNT ON;

    IF NOT EXISTS (SELECT 1 FROM dbo.livros WHERE id = @livro_id AND ativo = 1)
    BEGIN
        DECLARE @mensagem NVARCHAR(200) = CONCAT(N'O livro com o ID: ', @livro_id, N' não foi encontrado');
        THROW 50050, @mensagem, 1;
    END;

    UPDATE dbo.livros
    SET ativo = 0,
        disponivel = 0
    WHERE id = @livro_id;
END;
GO

-- =====================================================================
-- TRIGGERS
-- =====================================================================

-- Impede interesse em livro próprio, inativo, indisponível ou repetido.
CREATE OR ALTER TRIGGER dbo.trg_interesse_validar
ON dbo.interesse
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS (
        SELECT 1
        FROM inserted n
        LEFT JOIN dbo.livros l ON l.id = n.livro_id
        WHERE l.id IS NULL
           OR l.ativo = 0
           OR l.disponivel = 0
    )
        THROW 50060, N'Este livro não está disponível para interesse.', 1;

    IF EXISTS (
        SELECT 1
        FROM inserted n
        JOIN dbo.livros l ON l.id = n.livro_id
        WHERE l.usuario_id = n.usuario_interessado_id
    )
        THROW 50061, N'Você não pode demonstrar interesse no próprio livro.', 1;

    IF EXISTS (
        SELECT 1
        FROM inserted n
        JOIN dbo.interesse i
          ON i.livro_id = n.livro_id
         AND i.usuario_interessado_id = n.usuario_interessado_id
         AND i.id <> n.id
    )
        THROW 50062, N'Você já demonstrou interesse neste livro.', 1;
END;
GO

-- Ao aceitar um interesse, o livro fica indisponível e os demais interesses pendentes são recusados.
CREATE OR ALTER TRIGGER dbo.trg_interesse_aceito
ON dbo.interesse
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF NOT UPDATE(status)
        RETURN;

    DECLARE @aceitos TABLE (interesse_id BIGINT, livro_id BIGINT);

    INSERT INTO @aceitos (interesse_id, livro_id)
    SELECT n.id, n.livro_id
    FROM inserted n
    JOIN deleted o ON o.id = n.id
    WHERE n.status = 'ACEITO'
      AND ISNULL(o.status, '') <> 'ACEITO';

    UPDATE l
    SET disponivel = 0
    FROM dbo.livros l
    JOIN @aceitos a ON a.livro_id = l.id;

    UPDATE i
    SET status = 'RECUSADO'
    FROM dbo.interesse i
    JOIN @aceitos a ON a.livro_id = i.livro_id
    WHERE i.status = 'PENDENTE'
      AND i.id <> a.interesse_id;
END;
GO

-- Ao inativar (excluir) um livro, recusa os interesses que ainda estavam pendentes.
CREATE OR ALTER TRIGGER dbo.trg_livro_inativado
ON dbo.livros
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF NOT UPDATE(ativo)
        RETURN;

    UPDATE i
    SET status = 'RECUSADO'
    FROM dbo.interesse i
    JOIN inserted n ON n.id = i.livro_id
    JOIN deleted o ON o.id = n.id
    WHERE o.ativo = 1
      AND n.ativo = 0
      AND i.status = 'PENDENTE';
END;
GO
