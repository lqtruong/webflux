package com.example.webflux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.webflux.model.Task;
import com.example.webflux.repository.TaskRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TaskServiceImpl implements TaskService {

  @Autowired
  private TaskRepository taskRepository;


  @Override
  public Mono<Task> createTask(Task task) {
    return taskRepository.save(task);
  }

  @Override
  public Flux<Task> getAllTasks() {
    return taskRepository.findAll();
  }

}
