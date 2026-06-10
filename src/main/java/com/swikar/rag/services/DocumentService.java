package com.swikar.rag.services;

import org.apache.pdfbox.Loader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
//pdf
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

//word
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

//excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Service
public class DocumentService {
    private final ChunkingService chunkingService;
    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;

    public DocumentService(ChunkingService chunkingService,
                           EmbeddingService embeddingService,
                           VectorStoreService vectorStoreService) {
        this.chunkingService = chunkingService;
        this.embeddingService = embeddingService;
        this.vectorStoreService = vectorStoreService;
    }


    public void process(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String content;
        assert filename != null;

//        for text file
        if (filename.endsWith(".txt")) {
            content = extractTxtText(file);

//         for pdf file
        } else if (filename.endsWith(".pdf")) {
            content = extractPdfText(file);
            System.out.println(content);

//         for word file
        }else if (filename.endsWith(".docx")) {
                content = extractDocxText(file);

        }else if(filename.endsWith(".exsx")){
            content = extractExcelText(file);
        }

         else {
            throw new RuntimeException(
                    "Unsupported file type"
            );
        }


        List<String> chunks =
                chunkingService.chunk(content);

        for (int i = 0; i < chunks.size(); i++) {
            String chunk = chunks.get(i);

            float[] embedding =
                    embeddingService.embed(chunk);

            vectorStoreService.storeChunk(
                    file.getOriginalFilename(),
                    i + 1,
                    chunk,
                    embedding
            );

        }

    }

    private String extractTxtText(
            MultipartFile file)
            throws IOException {

        return new String(
                file.getBytes(),
                StandardCharsets.UTF_8
        );
    }

    // METHOD 2
    private String extractPdfText(
            MultipartFile file)
            throws IOException {

        try (PDDocument document =
                     Loader.loadPDF(file.getBytes())) {

            PDFTextStripper stripper =
                    new PDFTextStripper();

            return stripper.getText(document);
        }
    }
    private String extractDocxText(MultipartFile file)
            throws IOException {

        StringBuilder text = new StringBuilder();

        try (XWPFDocument document =
                     new XWPFDocument(file.getInputStream())) {

            for (XWPFParagraph paragraph :
                    document.getParagraphs()) {

                text.append(paragraph.getText())
                        .append("\n");
            }
        }

        return text.toString();
    }
    private String extractExcelText(MultipartFile file)
            throws IOException {

        StringBuilder text = new StringBuilder();

        try (Workbook workbook =
                     new XSSFWorkbook(file.getInputStream())) {

            for (Sheet sheet : workbook) {

                for (Row row : sheet) {

                    for (Cell cell : row) {

                        text.append(cell.toString())
                                .append(" ");
                    }

                    text.append("\n");
                }
            }
        }

        return text.toString();
    }
}
