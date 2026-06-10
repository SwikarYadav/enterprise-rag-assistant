package com.swikar.rag.services;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public EmbeddingService(
            @Qualifier("ollamaEmbeddingModel")
            EmbeddingModel embeddingModel) {

        this.embeddingModel = embeddingModel;
    }

    public float[] embed(String text) {
        return embeddingModel.embed(text);
    }

    public List<float[]> embedChunks(List<String> chunks) {

        List<float[]> embeddings = new ArrayList<>();

        for (String chunk : chunks) {
            embeddings.add(
                    embeddingModel.embed(chunk)
            );
        }

        return embeddings;
    }
}