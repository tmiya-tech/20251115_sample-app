package com.example.sample.web.todo.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/error-test")
public class ErrorTestController {
    
    private static final Logger logger = LoggerFactory.getLogger(ErrorTestController.class);
    
    @GetMapping("/stack-trace")
    public ResponseEntity<Map<String, String>> testStackTrace() {
        try {
            // 意図的に例外を発生させてスタックトレースをログに出力
            throw new RuntimeException("This is a test exception for stack trace logging");
        } catch (Exception e) {
            logger.error("Test error occurred", e);
            return ResponseEntity.ok(Map.of(
                "status", "error_logged",
                "message", "Stack trace has been logged"
            ));
        }
    }
}
