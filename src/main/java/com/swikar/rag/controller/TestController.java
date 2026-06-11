package com.swikar.rag.controller;

import com.swikar.rag.model.ChatRequest;
import com.swikar.rag.services.EmbeddingService;
import com.swikar.rag.services.RagService;
import com.swikar.rag.services.VectorStoreService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class TestController {

    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;
    private final RagService ragService;
//    private final ChatRequest ;

    public TestController(
            EmbeddingService embeddingService,
            VectorStoreService vectorStoreService,
            RagService ragService
    ) {

        this.embeddingService = embeddingService;
        this.vectorStoreService = vectorStoreService;
        this.ragService = ragService;
//        this.chatRequest=chatRequest;
    }

    @PostMapping("/insert-test")
    public String insertTest() {

        String chunkText = "This is a test chunk";

        float[] embedding = embeddingService.embed(chunkText);

        int rows = vectorStoreService.storeChunk(
                "test.pdf",
                1,
                chunkText,
                embedding
        );

        return "Rows Inserted : " + rows;
    }

    @PostMapping("/rag")
    public Map<String, String> rag(
            @RequestBody ChatRequest request) {

//        String question = request.get("question");

        return Map.of(
                "answer",
                ragService.ask(
                        request.getConversationId(),
                        request.getQuestion()
                )
        );
    }
}