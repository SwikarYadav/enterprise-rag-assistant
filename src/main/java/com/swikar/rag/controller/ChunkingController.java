package com.swikar.rag.controller;

import com.swikar.rag.services.ChunkingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChunkingController {

    private final ChunkingService chunkingService;

    public ChunkingController(ChunkingService chunkingService) {
        this.chunkingService = chunkingService;
    }

    @PostMapping("/chunk")
    public List<String> chunk(@RequestBody String text) {
        return chunkingService.chunk(text);
    }
}