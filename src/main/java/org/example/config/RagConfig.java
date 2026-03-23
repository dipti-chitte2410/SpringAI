package org.example.config;

import org.apache.commons.compress.utils.Lists;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RagConfig {

    // In-memory vector store — no external DB needed
    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel)
                .build();
    }

    // Load documents into the vector store at startup
    @Bean
    ApplicationRunner ingestDocuments(VectorStore vectorStore) {
        return args -> {
            List<Document> docs = new ArrayList<>();
            docs.add(new Document("Spring AI simplifies building AI apps on the JVM."));
            docs.add(new Document("Ollama lets you run LLMs locally for free."));
            docs.add(new Document("RAG stands for Retrieval Augmented Generation."));
            vectorStore.add(docs);
        };
    }
}