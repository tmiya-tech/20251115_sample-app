package com.example.sample.webflux.todo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("todo")
public class Todo {
    
    @Id
    private Long id;
    
    @Column("title")
    private String title;
    
    @Column("description")
    private String description;
    
    @Column("done")
    private Boolean done = false;
    
    @Column("created_at")
    private LocalDateTime createdAt;
    
    public Todo() {}
    
    public Todo(String title, String description) {
        this.title = title;
        this.description = description;
        this.done = false;
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getDone() {
        return done;
    }
    
    public void setDone(Boolean done) {
        this.done = done;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}