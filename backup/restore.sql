-- Restaura o banco a partir de um arquivo gerado por backup.sql, substituindo o banco atual.
-- Pare o backend antes de restaurar.
-- Uso: sqlcmd -S .\SQLEXPRESS -E -C -f 65001 -i backup\restore.sql -v DB=biblioteca_comunitaria ARQUIVO=lumiere.bak

:on error exit
SET NOCOUNT ON;
USE master;

DECLARE @caminho NVARCHAR(4000) = CONCAT(CAST(SERVERPROPERTY('InstanceDefaultBackupPath') AS NVARCHAR(4000)), N'\', N'$(ARQUIVO)');

IF DB_ID(N'$(DB)') IS NOT NULL
    ALTER DATABASE [$(DB)] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;

RESTORE DATABASE [$(DB)]
FROM DISK = @caminho
WITH REPLACE, CHECKSUM;

ALTER DATABASE [$(DB)] SET MULTI_USER;

PRINT CONCAT(N'Banco restaurado de: ', @caminho);
GO
