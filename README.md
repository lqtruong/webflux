# webflux
Sample of Spring Webflux with MongoDB and tests

# Controller
TaskController is a representation of the RESTful API using reactive way by Spring Webflux

```
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
```

Whereas, the POST `/tasks` to create a task and return a `Mono` single task record.

The GET `/tasks` responds a list of tasks represented a `Flux` of Task.


# Repository

There are various way to retrieve data from database in a reactive way. We can use `ReactiveMongoTemplate` or `ReactiveMongoRepository` or `RxJava` way. In this example, we use `ReactiveMongoRepository`

```
public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
}
```

# Tests
- Repository integration tests
See [TaskRepositoryTest.java](src/test/java/com/example/webflux/repository/TaskRepositoryTest.java)

```
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

```

- Controller Unit tests using Mock
See [TaskControllerTest.java](src/test/java/com/example/webflux/controller/TaskControllerTest.java)

```
ExtendWith(SpringExtension.class)
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
```

# APIs

- To create a task

```
curl --location --request POST 'localhost:8080/tasks' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "math",
    "desc": "math exercise",
    "personId": "2"
}'
```

- To list all tasks
```
curl --location --request GET 'localhost:8080/tasks'
```



