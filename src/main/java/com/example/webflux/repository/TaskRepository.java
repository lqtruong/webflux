package com.example.webflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.webflux.model.Task;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
}
