package com.example.webflux.model;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.webflux.controller.TaskRequest;

@Document("tasks")
@Getter
@Setter
public class Task {

  private String id;
  private String name;
  private String desc;
  private String personId;

  public static Task from(TaskRequest taskRequest) {
    Task task = new Task();
    task.setName(taskRequest.getName());
    task.setDesc(taskRequest.getDesc());
    task.setPersonId(taskRequest.getPersonId());
    return task;
  }
}
