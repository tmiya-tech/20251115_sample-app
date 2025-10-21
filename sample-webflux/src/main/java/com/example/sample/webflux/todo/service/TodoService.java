package com.example.sample.webflux.todo.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import com.example.sample.webflux.todo.dto.CreateTodoRequest;
import com.example.sample.webflux.todo.dto.SleepResponse;
import com.example.sample.webflux.todo.dto.TodoDto;
import com.example.sample.webflux.todo.dto.UpdateTodoRequest;
import com.example.sample.webflux.todo.entity.Todo;
import com.example.sample.webflux.todo.repository.TodoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final DatabaseClient databaseClient;

    public TodoService(TodoRepository todoRepository, DatabaseClient databaseClient) {
        this.todoRepository = todoRepository;
        this.databaseClient = databaseClient;
    }
    
    public Mono<TodoDto> createTodo(CreateTodoRequest request) {
        Todo todo = new Todo(request.getTitle(), request.getDescription());
        todo.setCreatedAt(LocalDateTime.now());
        return todoRepository.save(todo)
                .map(this::toDto);
    }
    public Mono<TodoDto> getTodo(Long id) {
        return todoRepository.findById(id)
                .map(this::toDto);
    }
    public Flux<TodoDto> getTodos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return todoRepository.findAllBy(pageable)
                .map(this::toDto);
    }
    public Mono<TodoDto> updateTodo(Long id, UpdateTodoRequest request) {
        return todoRepository.findById(id)
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
                .flatMap(todo -> {
                    todo.setDone(true);
                    return todoRepository.save(todo);
                })
                .map(this::toDto);
    }
    public Mono<Boolean> deleteTodo(Long id) {
        return todoRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return todoRepository.deleteById(id).then(Mono.just(true));
                    } else {
                        return Mono.just(false);
                    }
                });
    }
    
    public Mono<SleepResponse> sleep(long sleepMs) {
        LocalDateTime startTime = LocalDateTime.now();
        long startNanos = System.nanoTime();

        // Convert milliseconds to seconds for pg_sleep
        double sleepSeconds = sleepMs / 1000.0;

        return databaseClient.sql("SELECT pg_sleep(:sleepSeconds)")
                .bind("sleepSeconds", sleepSeconds)
                .fetch()
                .first()
                .then(Mono.fromCallable(() -> {
                    long endNanos = System.nanoTime();
                    LocalDateTime endTime = LocalDateTime.now();
                    long elapsedMs = (endNanos - startNanos) / 1_000_000;
                    return new SleepResponse(startTime, endTime, elapsedMs);
                }));
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