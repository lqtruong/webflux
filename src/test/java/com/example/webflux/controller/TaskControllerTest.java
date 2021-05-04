package com.example.webflux.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.example.webflux.model.Task;
import com.example.webflux.service.TaskService;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = TaskController.class)
public class TaskControllerTest {

  @Autowired
  private WebTestClient webClient;

  @MockBean
  private TaskService taskService;

  @Test
  public void givenTask_whenCreate_Successful() {

    TaskRequest taskRequest = new TaskRequest();
    taskRequest.setName("math task");
    taskRequest.setDesc("task desc");
    taskRequest.setPersonId("2");

    final Task task = Task.from(taskRequest);
    when(taskService.createTask(any(Task.class))).thenReturn(Mono.just(task));

    webClient.post()
        .uri("/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(taskRequest))
        .exchange()
        .expectStatus().isOk()
        .expectBody(Task.class)
        .value(Task::getName, is(taskRequest.getName()))
        .value(Task::getDesc, is(taskRequest.getDesc()))
        .value(Task::getPersonId, is(taskRequest.getPersonId()));

    verify(taskService, times(1)).createTask(any());

  }

}
