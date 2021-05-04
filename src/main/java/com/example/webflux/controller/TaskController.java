package com.example.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webflux.model.Task;
import com.example.webflux.service.TaskService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("tasks")
public class TaskController {

  @Autowired
  private TaskService taskService;

  @PostMapping
  public Mono<Task> addTask(@RequestBody TaskRequest taskRequest) {
    return taskService.createTask(Task.from(taskRequest));
  }

  @GetMapping
  public Flux<Task> getAllTasks() {
    return taskService.getAllTasks();
  }

}
