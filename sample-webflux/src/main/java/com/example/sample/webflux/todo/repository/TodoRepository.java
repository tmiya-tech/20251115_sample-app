package com.example.sample.webflux.todo.repository;

import com.example.sample.webflux.todo.entity.Todo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TodoRepository extends R2dbcRepository<Todo, Long> {

    Flux<Todo> findAllBy(Pageable pageable);
}