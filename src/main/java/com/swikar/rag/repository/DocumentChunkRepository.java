package com.swikar.rag.repository;

import com.swikar.rag.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk,Long> {
}
