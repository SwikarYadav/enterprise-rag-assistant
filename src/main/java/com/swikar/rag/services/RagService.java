package com.swikar.rag.services;

import com.swikar.rag.model.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {
    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;
    private final ChatService chatService;
    private  final MemoryService memoryService;
//    private final ChatMessage chatMessage;

    public RagService(EmbeddingService embeddingService,
                      VectorStoreService vectorStoreService,
                      ChatService chatService,
                      MemoryService memoryService)

    {
        this.embeddingService = embeddingService;
        this.vectorStoreService = vectorStoreService;
        this.chatService = chatService;
        this.memoryService = memoryService;
    }

    private String formatHistory(
            List<ChatMessage> history) {

        StringBuilder sb = new StringBuilder();

        for(ChatMessage msg : history) {

            sb.append(msg.getRole())
                    .append(": ")
                    .append(msg.getContent())
                    .append("\n");
        }

        return sb.toString();
    }


    public String ask( String conversationId,
                       String question) {

        memoryService.addUserMessage(
                conversationId,
                question
        );
        List<ChatMessage> history =
                memoryService.getHistory(conversationId);

        float[] embedding =
                embeddingService.embed(question);

        List<String> chunks =
                vectorStoreService
                        .searchSimilarChunks(embedding);

        System.out.println("Retrieved Chunks:");
        chunks.forEach(System.out::println);

        String context =
                String.join("\n", chunks);

        String prompt = """
YYou are a RAG assistant.

You have two sources of information:

1. Conversation History
2. Document Context

Instructions:

- If the answer exists in Conversation History, use it.
- If the answer exists in Document Context, use it.
- If both contain relevant information, combine them.
- Do not get distracted by unrelated document content.
- Do not mention information that was not asked for.
- If neither source contains the answer, say you do not know.

Conversation History:
%s

Document Context:
%s

Question:
%s
"""
                .formatted(
                        formatHistory(history),
                        context,
                        question
                );

        System.out.println("\n========== HISTORY ==========");
        System.out.println(formatHistory(history));

        System.out.println("\n========== CONTEXT ==========");
        System.out.println(context);

        System.out.println("\n========== QUESTION ==========");
        System.out.println(question);
        System.out.println("\n========== PROMPT STATS ==========");
        System.out.println("History Length  : " + formatHistory(history).length());
        System.out.println("Context Length  : " + context.length());
        System.out.println("Question Length : " + question.length());
        System.out.println("Prompt Length   : " + prompt.length());
        String answer =
                chatService.ask(prompt);

        memoryService.addAssistantMessage(
                conversationId,
                answer
        );

        return answer;

    }
}
