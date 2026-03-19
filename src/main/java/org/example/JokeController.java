package org.example;

import com.theokanning.openai.completion.CompletionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JokeController {

    private final OpenAiAPIService openAiAPIService;

    public JokeController(OpenAiAPIService openAiAPIService) {
        this.openAiAPIService = openAiAPIService;
    }


    @GetMapping("/joke")
    public String getJoke(@RequestParam String topic) {
        String prompt = "Tell me a funny joke about " + topic;
        CompletionRequest request = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt(prompt)
                .maxTokens(100)
                .temperature(0.7)
                .build();

        return openAiAPIService.createCompletion(request).getChoices().get(0).getText().trim();
    }
}