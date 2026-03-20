package org.example;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JokeController {

    private final OpenAiAPIService openAiAPIService;

    public JokeController(OpenAiAPIService openAiAPIService) {
        this.openAiAPIService = openAiAPIService;
    }


    @GetMapping("/joke")
    public String getJoke(@RequestParam String topic) {
        String prompt = "Tell me a funny joke about " + topic;

        ChatMessage message = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4o-mini")          // ✅ current, fast, cheap
                .messages(List.of(message))
                .maxTokens(100)
                .temperature(0.7)
                .build();

        return openAiAPIService.createChatCompletion(request)
                .getChoices().get(0).getMessage().getContent().trim();
    }
}