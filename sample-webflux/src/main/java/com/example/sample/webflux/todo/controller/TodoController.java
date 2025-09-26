package com.example.sample.webflux.todo.controller;

import com.example.sample.webflux.todo.dto.CreateTodoRequest;
import com.example.sample.webflux.todo.dto.TodoDto;
import com.example.sample.webflux.todo.dto.UpdateTodoRequest;
import com.example.sample.webflux.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    
    private final TodoService todoService;
    
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    @PostMapping
    public Mono<ResponseEntity<TodoDto>> createTodo(@RequestBody CreateTodoRequest request) {
        return todoService.createTodo(request)
                .map(todo -> ResponseEntity.status(HttpStatus.CREATED).body(todo));
    }
    
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TodoDto>> getTodo(@PathVariable Long id) {
        return todoService.getTodo(id)
                .map(todo -> ResponseEntity.ok(todo))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public Mono<ResponseEntity<Flux<TodoDto>>> getTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        if (size > 100) {
            size = 100;
        }
        
        Flux<TodoDto> todos = todoService.getTodos(page, size);
        return Mono.just(ResponseEntity.ok(todos));
    }
    
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TodoDto>> updateTodo(@PathVariable Long id, @RequestBody UpdateTodoRequest request) {
        return todoService.updateTodo(id, request)
                .map(todo -> ResponseEntity.ok(todo))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/complete")
    public Mono<ResponseEntity<TodoDto>> completeTodo(@PathVariable Long id) {
        return todoService.completeTodo(id)
                .map(todo -> ResponseEntity.ok(todo))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTodo(@PathVariable Long id) {
        return todoService.deleteTodo(id)
                .map(deleted -> deleted ? 
                        ResponseEntity.noContent().<Void>build() : 
                        ResponseEntity.notFound().<Void>build());
    }
}