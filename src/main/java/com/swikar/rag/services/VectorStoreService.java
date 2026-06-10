package com.swikar.rag.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorStoreService {

    private final JdbcTemplate jdbcTemplate;

    public VectorStoreService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public Integer testConnection() {

        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM document_chunks",
                Integer.class
        );
    }
    public int storeChunk(
            String documentName,
            Integer chunkNumber,
            String chunkText,
            float[] embedding) {

        String vectorString = java.util.Arrays.toString(embedding);

        String sql = """
            INSERT INTO document_chunks
            (
                document_name,
                chunk_number,
                chunk_text,
                embedding
            )
            VALUES
            (
                ?,
                ?,
                ?,
                CAST(? AS vector)
            )
            """;

        return jdbcTemplate.update(
                sql,
                documentName,
                chunkNumber,
                chunkText,
                vectorString
        );
    }

    public List<String>  searchSimilarChunks(float[] queryEmbedding) {

        String vectorString = java.util.Arrays.toString(queryEmbedding);

        String sql = """
            SELECT chunk_text
            FROM document_chunks
            ORDER BY embedding <=> CAST(? AS vector)
            LIMIT 3
            """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("chunk_text"),
                vectorString
        );
    }

}