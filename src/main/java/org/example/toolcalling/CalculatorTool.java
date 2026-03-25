package org.example.toolcalling;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Component
public class CalculatorTool {

    @Tool(description = "Perform basic math calculations. Use when user asks to calculate, compute or solve math.")
    public String calculate(
            @ToolParam(description = "Math expression to evaluate e.g. 10 * 5 + 3")
            String expression) {
        try {
            // Simple eval using ScriptEngine
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            Object result = engine.eval(expression);
            return "Result of " + expression + " = " + result;
        } catch (Exception e) {
            return "Could not calculate: " + expression;
        }
    }
}