package com.swikar.rag.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkingService {

    private static final int CHUNK_SIZE = 100;

    public List<String> chunk(String text) {

        String[] words = text.split("\\s+");

        List<String> chunks = new ArrayList<>();

        StringBuilder currentChunk = new StringBuilder();

        int count = 0;

        for (String word : words) {

            currentChunk.append(word).append(" ");
            count++;

            if (count == CHUNK_SIZE) {

                chunks.add(currentChunk.toString().trim());

                currentChunk = new StringBuilder();

                count = 0;
            }
        }

        // Remaining words
        if (!currentChunk.isEmpty()) {
            chunks.add(currentChunk.toString().trim());
        }

        return chunks;
    }
}