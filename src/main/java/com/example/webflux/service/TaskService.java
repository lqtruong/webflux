package com.example.webflux.service;

import com.example.webflux.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

  Mono<Task> createTask(final Task task);

  Flux<Task> getAllTasks();

}
