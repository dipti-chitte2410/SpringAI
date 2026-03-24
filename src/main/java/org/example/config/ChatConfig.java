package org.example.config;

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

    // Spring AI provides this bean automatically
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();  // stored in RAM, resets on restart
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel,
                                 ChatMemory chatMemory,
                                 VectorStore vectorStore) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                You are a helpful assistant. Remember the conversation history
                and refer to it when answering follow-up questions.
                """)
                .defaultAdvisors(
                        // Memory advisor — MUST be first
                        new MessageChatMemoryAdvisor(chatMemory),

                        // RAG advisor — retrieves relevant docs
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().build())
                )
                .build();
    }
}