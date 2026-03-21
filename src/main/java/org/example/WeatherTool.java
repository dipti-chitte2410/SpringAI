package org.example;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class WeatherTool {

    @Tool(description = "Get the current weather for a given city")
    public String getWeather(String city) {
        // Replace with real weather API call if needed
        return "The weather in " + city + " is 28°C and sunny.";
    }
}