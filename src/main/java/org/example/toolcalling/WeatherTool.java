package org.example.toolcalling;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WeatherTool {

    @Tool(description = "Get current weather for a given city. Call this when user asks about weather.")
    public String getWeather(
            @ToolParam(description = "The city name e.g. Pune, Mumbai, Delhi")
            String city) {

        // Replace with real API call e.g. OpenWeatherMap
        // For now returning mock data
        Map<String, String> weatherData = Map.of(
            "pune",   "28°C, Sunny",
            "mumbai", "32°C, Humid",
            "delhi",  "22°C, Cloudy",
            "bangalore", "24°C, Partly Cloudy"
        );

        String result = weatherData.getOrDefault(
            city.toLowerCase(), "25°C, Clear"
        );

        return "Weather in " + city + ": " + result;
    }
}