package com.swikar.rag.controller;

import com.swikar.rag.services.ChunkingService;
import com.swikar.rag.services.EmbeddingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PipelineController {

    private final ChunkingService chunkingService;
    private final EmbeddingService embeddingService;

    public PipelineController(
            ChunkingService chunkingService,
            EmbeddingService embeddingService) {

        this.chunkingService = chunkingService;
        this.embeddingService = embeddingService;
    }

    @PostMapping("/pipeline")
    public String process(@RequestBody String text) {

        List<String> chunks =
                chunkingService.chunk(text);

        List<float[]> embeddings =
                embeddingService.embedChunks(chunks);

        return "Chunks: " + chunks.size()
                + ", Embeddings: "
                + embeddings.size()
                + ", Dimension: "
                + embeddings.get(0).length;

    }
}