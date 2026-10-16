package com.biblioteca.backend.service;


import com.biblioteca.backend.dto.LivroHomeDTO;
import com.biblioteca.backend.enums.GeneroLivro;
import com.biblioteca.backend.enums.TipoInteracao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HomeService {

    private static final RowMapper<LivroHomeDTO> LIVRO_HOME_MAPPER = (rs, i) -> new LivroHomeDTO(
            rs.getLong("id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getString("descricao_livro"),
            rs.getBoolean("disponivel"),
            rs.getBoolean("ativo"),
            rs.getString("tipo_interacao") != null ? TipoInteracao.valueOf(rs.getString("tipo_interacao")) : null,
            rs.getString("genero_livro") != null ? GeneroLivro.valueOf(rs.getString("genero_livro")) : null,
            rs.getLong("usuario_id"),
            rs.getString("nome_usuario"),
            rs.getString("foto_usuario"),
            rs.getString("descricao_usuario")
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<LivroHomeDTO> listarLivros(Long usuarioId) {
        return jdbcTemplate.query(
                "SELECT * FROM dbo.vw_livros_disponiveis WHERE usuario_id <> ? ORDER BY id DESC",
                LIVRO_HOME_MAPPER,
                usuarioId
        );
    }

    public List<LivroHomeDTO> listarRecomendados(Long usuarioId) {
        return jdbcTemplate.query(
                "SELECT * FROM dbo.fn_livros_recomendados(?)",
                LIVRO_HOME_MAPPER,
                usuarioId
        );
    }
}
