package com.example.todo.web.controller;

import com.example.todo.web.dto.CreateTodoRequest;
import com.example.todo.web.dto.TodoDto;
import com.example.todo.web.dto.UpdateTodoRequest;
import com.example.todo.web.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    
    private final TodoService todoService;
    
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@RequestBody CreateTodoRequest request) {
        TodoDto todo = todoService.createTodo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(todo);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getTodo(@PathVariable Long id) {
        return todoService.getTodo(id)
                .map(todo -> ResponseEntity.ok(todo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<Page<TodoDto>> getTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        if (size > 100) {
            size = 100;
        }
        
        Page<TodoDto> todos = todoService.getTodos(page, size);
        return ResponseEntity.ok(todos);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> updateTodo(@PathVariable Long id, @RequestBody UpdateTodoRequest request) {
        return todoService.updateTodo(id, request)
                .map(todo -> ResponseEntity.ok(todo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TodoDto> completeTodo(@PathVariable Long id) {
        return todoService.completeTodo(id)
                .map(todo -> ResponseEntity.ok(todo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        boolean deleted = todoService.deleteTodo(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}