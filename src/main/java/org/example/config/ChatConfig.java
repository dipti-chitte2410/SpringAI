package org.example.config;

import org.example.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory(); // keeps history per conversationId
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel,
                                 ChatMemory chatMemory,
                                 VectorStore vectorStore,
                                 WeatherTool weatherTool) {
        return ChatClient.builder(chatModel)
            // System prompt
            .defaultSystem("You are a helpful assistant. Use provided context and tools.")
            // Memory advisor — maintains conversation history
            .defaultAdvisors(
                new MessageChatMemoryAdvisor(chatMemory),
                // RAG advisor — retrieves relevant docs before each call
                new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().build())
            )
            // Register tool
            .defaultTools(weatherTool)
            .build();
    }
}