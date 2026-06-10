package com.swikar.rag.controller;

import com.swikar.rag.services.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UploadController {
    private final DocumentService documentService;

    public UploadController(DocumentService documentService){
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam ("file")MultipartFile file) throws IOException {
        documentService.process(file);
        return "file Uploaded successfully";
    }
}
