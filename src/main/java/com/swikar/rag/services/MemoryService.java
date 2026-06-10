package com.swikar.rag.services;

import com.swikar.rag.model.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MemoryService {

    private static final int MAX_HISTORY = 20;

    private final Map<String, List<ChatMessage>> conversations =
            new ConcurrentHashMap<>();

    public List<ChatMessage> getHistory(String conversationId) {

        return conversations.computeIfAbsent(
                conversationId,
                id -> Collections.synchronizedList(
                        new ArrayList<>()
                )
        );
    }

    public void addUserMessage(
            String conversationId,
            String message) {

        addMessage(
                conversationId,
                new ChatMessage("USER", message)
        );
    }

    public void addAssistantMessage(
            String conversationId,
            String message) {

        addMessage(
                conversationId,
                new ChatMessage("ASSISTANT", message)
        );
    }

    private void addMessage(
            String conversationId,
            ChatMessage message) {

        List<ChatMessage> history =
                getHistory(conversationId);

        history.add(message);

        while (history.size() > MAX_HISTORY) {
            history.remove(0);
        }
    }
}