package com.example.webflux.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.webflux.SampleWebfluxApplication;
import com.example.webflux.model.Task;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SampleWebfluxApplication.class)
public class TaskRepositoryTest {

  @Autowired
  TaskRepository repository;

  @BeforeEach
  public void resetTasks() {
    repository.deleteAll().block();
  }

  @Test
  public void givenTask_whenSave_newTaskCreated() {
    Task actual = new Task();
    actual.setName("math task");
    actual.setDesc("task desc");
    actual.setPersonId("2");
    repository.save(actual).block();

    ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", startsWith());

    Task expected = new Task();
    expected.setName("mat");

    Example<Task> example = Example.of(expected, matcher);
    Flux<Task> taskFlux = repository.findAll(example);

    StepVerifier.create(taskFlux)
        .recordWith(ArrayList::new)
        .thenConsumeWhile(task -> true)
        .consumeRecordedWith(results -> givenTask_whenSave_newTaskCreated_assertion(actual, results))
        .verifyComplete();
  }

  private static void givenTask_whenSave_newTaskCreated_assertion(Task actual, final Collection<Task> results) {
    assertThat(results, hasSize(1));
    results.forEach(taskFound -> assertThat(actual.getName(), is(taskFound.getName())));
  }
}
