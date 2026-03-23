package org.example;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final ChatClient chatClient;
    private final DocumentService documentService;
    private final VectorStore vectorStore;

    public RagController(ChatClient.Builder builder,
                         DocumentService documentService,
                         VectorStore vectorStore) {
        this.documentService = documentService;
        this.vectorStore = vectorStore;

        this.chatClient = builder
            .defaultSystem("""
                You are a helpful assistant. Answer questions ONLY based on
                the provided document context. If the answer is not in the
                context, say "I don't know based on the provided documents."
                """)
            .defaultAdvisors(
                new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().build())
            )
            .build();
    }

    // Upload PDF
    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            documentService.ingestPdf(file);
            return "Uploaded and indexed: " + file.getOriginalFilename();
        } catch (IOException e) {
            return "Upload failed: " + e.getMessage();
        }
    }

    // Ask question about uploaded docs
    @GetMapping("/ask")
    public String ask(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }

    // Streaming ask
    @GetMapping(value = "/ask/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> askStream(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .stream()
                .content();
    }
}