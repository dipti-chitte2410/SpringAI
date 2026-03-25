package org.example.config;

import org.example.toolcalling.CalculatorTool;
import org.example.toolcalling.DateTimeTool;
import org.example.toolcalling.WeatherTool;
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
        return new InMemoryChatMemory();
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel,
                                 ChatMemory chatMemory,
                                 VectorStore vectorStore,
                                 WeatherTool weatherTool,
                                 CalculatorTool calculatorTool,
                                 DateTimeTool dateTimeTool) {

        return ChatClient.builder(chatModel)
                .defaultSystem("""
                You are a helpful assistant with access to tools.
                - Use the weather tool when asked about weather
                - Use the calculator tool for any math
                - Use the datetime tool for date/time questions
                - Use RAG context for document questions
                - Answer directly if no tool is needed
                """)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().build())
                )
                // Register all tools here
                .defaultTools(weatherTool, calculatorTool, dateTimeTool)
                .build();
    }
}