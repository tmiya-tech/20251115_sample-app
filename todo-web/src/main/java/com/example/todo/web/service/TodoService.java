package com.example.todo.web.service;

import com.example.todo.web.dto.CreateTodoRequest;
import com.example.todo.web.dto.TodoDto;
import com.example.todo.web.dto.UpdateTodoRequest;
import com.example.todo.web.entity.Todo;
import com.example.todo.web.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TodoService {
    
    private final TodoRepository todoRepository;
    
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    
    public TodoDto createTodo(CreateTodoRequest request) {
        Todo todo = new Todo(request.getTitle(), request.getDescription());
        Todo savedTodo = todoRepository.save(todo);
        return new TodoDto(savedTodo);
    }
    
    public Optional<TodoDto> getTodo(Long id) {
        return todoRepository.findById(id)
                .map(TodoDto::new);
    }
    
    public Page<TodoDto> getTodos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return todoRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(TodoDto::new);
    }
    
    public Optional<TodoDto> updateTodo(Long id, UpdateTodoRequest request) {
        return todoRepository.findById(id)
                .map(todo -> {
                    if (request.getTitle() != null) {
                        todo.setTitle(request.getTitle());
                    }
                    if (request.getDescription() != null) {
                        todo.setDescription(request.getDescription());
                    }
                    if (request.getDone() != null) {
                        todo.setDone(request.getDone());
                    }
                    Todo savedTodo = todoRepository.save(todo);
                    return new TodoDto(savedTodo);
                });
    }
    
    public Optional<TodoDto> completeTodo(Long id) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todo.setDone(true);
                    Todo savedTodo = todoRepository.save(todo);
                    return new TodoDto(savedTodo);
                });
    }
    
    public boolean deleteTodo(Long id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}