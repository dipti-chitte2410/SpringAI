package org.example;

import com.theokanning.openai.OpenAiService;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
                return new OpenAPI().info(new Info()
                        .title("Spring AI Jokes API")
                        .version("1.0")
                        .description("API for generating funny jokes based on topics"));
    }


}