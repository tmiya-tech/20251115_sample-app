package com.example.sample.webflux.todo.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.sample.webflux.todo.dto.CreateTodoRequest;
import com.example.sample.webflux.todo.dto.TodoDto;
import com.example.sample.webflux.todo.dto.UpdateTodoRequest;
import com.example.sample.webflux.todo.entity.Todo;
import com.example.sample.webflux.todo.repository.TodoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class TodoService {
    
    private final TodoRepository todoRepository;
    
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    
    private Duration randomDelay() {
        int millis = ThreadLocalRandom.current().nextInt(100, 200);
        return Duration.ofMillis(millis);
    }
    
    public Mono<TodoDto> createTodo(CreateTodoRequest request) {
        Todo todo = new Todo(request.getTitle(), request.getDescription());
        todo.setCreatedAt(LocalDateTime.now());
        return todoRepository.save(todo)
                .delayElement(randomDelay())
                .map(this::toDto);
    }
    public Mono<TodoDto> getTodo(Long id) {
        return todoRepository.findById(id)
                .delayElement(randomDelay())
                .map(this::toDto);
    }
    public Flux<TodoDto> getTodos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return todoRepository.findAllBy(pageable)
                .delayElements(randomDelay())
                .map(this::toDto);
    }
    public Mono<TodoDto> updateTodo(Long id, UpdateTodoRequest request) {
        return todoRepository.findById(id)
                .delayElement(randomDelay())
                .flatMap(todo -> {
                    if (request.getTitle() != null) {
                        todo.setTitle(request.getTitle());
                    }
                    if (request.getDescription() != null) {
                        todo.setDescription(request.getDescription());
                    }
                    if (request.getDone() != null) {
                        todo.setDone(request.getDone());
                    }
                    return todoRepository.save(todo);
                })
                .map(this::toDto);
    }
    public Mono<TodoDto> completeTodo(Long id) {
        return todoRepository.findById(id)
                .delayElement(randomDelay())
                .flatMap(todo -> {
                    todo.setDone(true);
                    return todoRepository.save(todo);
                })
                .map(this::toDto);
    }
    public Mono<Boolean> deleteTodo(Long id) {
        return todoRepository.existsById(id)
                .delayElement(randomDelay())
                .flatMap(exists -> {
                    if (exists) {
                        return todoRepository.deleteById(id).then(Mono.just(true));
                    } else {
                        return Mono.just(false);
                    }
                });
    }
    
    private TodoDto toDto(Todo todo) {
        return new TodoDto(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getDone(),
                todo.getCreatedAt()
        );
    }
}