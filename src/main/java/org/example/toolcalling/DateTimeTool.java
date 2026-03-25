package org.example.toolcalling;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeTool {

    @Tool(description = "Get current date and time. Use when user asks about today's date or current time.")
    public String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        return "Current date and time: " + now.format(formatter);
    }

    @Tool(description = "Get day of week for a given date. Use when user asks what day a date falls on.")
    public String getDayOfWeek(
            @ToolParam(description = "Date in format yyyy-MM-dd e.g. 2025-12-25")
            String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return date + " falls on a " + parsedDate.getDayOfWeek();
    }
}