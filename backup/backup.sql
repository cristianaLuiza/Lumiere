-- Backup completo do banco (tabelas, dados, views, functions, procedures e triggers).
-- O arquivo é gravado na pasta de backup padrão da instância do SQL Server.
-- Uso: sqlcmd -S .\SQLEXPRESS -E -C -f 65001 -i backup\backup.sql -v DB=biblioteca_comunitaria ARQUIVO=lumiere.bak

:on error exit
SET NOCOUNT ON;

DECLARE @caminho NVARCHAR(4000) = CONCAT(CAST(SERVERPROPERTY('InstanceDefaultBackupPath') AS NVARCHAR(4000)), N'\', N'$(ARQUIVO)');

BACKUP DATABASE [$(DB)]
TO DISK = @caminho
WITH INIT, CHECKSUM, NAME = N'$(DB) - backup completo';

RESTORE VERIFYONLY FROM DISK = @caminho WITH CHECKSUM;

PRINT CONCAT(N'Backup salvo em: ', @caminho);
GO
