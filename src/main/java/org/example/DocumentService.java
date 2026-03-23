package org.example;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class DocumentService {

    private final VectorStore vectorStore;

    public DocumentService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingestPdf(MultipartFile file) throws IOException {
        // Save uploaded file temporarily
        Path tempFile = Files.createTempFile("upload-", ".pdf");
        file.transferTo(tempFile.toFile());

        // Read PDF
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(
            new FileSystemResource(tempFile),
            PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(
                    ExtractedTextFormatter.builder()
                        .withNumberOfBottomTextLinesToDelete(3)
                        .build()
                )
                .withPagesPerDocument(1)
                .build()
        );

        // Split into chunks
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> chunks = splitter.apply(pdfReader.get());

        // Store in vector store
        vectorStore.add(chunks);

        // Cleanup temp file
        Files.deleteIfExists(tempFile);

        System.out.println("Ingested " + chunks.size() + " chunks from: " + file.getOriginalFilename());
    }
}