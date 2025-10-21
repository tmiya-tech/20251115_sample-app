package com.example.sample.web.todo.repository;

import com.example.sample.web.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    Page<Todo> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT pg_sleep(:sleepSeconds)", nativeQuery = true)
    void sleep(@Param("sleepSeconds") double sleepSeconds);
}