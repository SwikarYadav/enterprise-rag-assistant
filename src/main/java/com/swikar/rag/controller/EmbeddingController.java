package com.swikar.rag.controller;

import com.swikar.rag.services.EmbeddingService;
import com.swikar.rag.services.VectorStoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmbeddingController {

    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;

    public EmbeddingController(EmbeddingService embeddingService, VectorStoreService vectorStoreService) {
        this.embeddingService = embeddingService;
        this.vectorStoreService =vectorStoreService;
    }

    @GetMapping("/embed")
    public float[] embed(@RequestParam String text) {
        return embeddingService.embed(text);
    }

        @PostMapping("/search")
        public List<String> search(@RequestParam String question) {

            float[] embedding =
                    embeddingService.embed(question);

            return vectorStoreService
                    .searchSimilarChunks(embedding);

    }
}