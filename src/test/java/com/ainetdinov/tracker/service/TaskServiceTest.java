package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.base.BaseTest;
import com.ainetdinov.tracker.configuration.HibernateConfiguration;
import com.ainetdinov.tracker.constant.Status;
import com.ainetdinov.tracker.model.dto.TaskDto;
import com.ainetdinov.tracker.model.mapper.LabelMapper;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.model.mapper.UserMapper;
import com.ainetdinov.tracker.model.request.TaskRequest;
import com.ainetdinov.tracker.repository.LabelRepository;
import com.ainetdinov.tracker.repository.TaskRepository;
import com.ainetdinov.tracker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.*;

class TaskServiceTest extends BaseTest {
    TaskService taskService;
    LabelService labelService;
    UserService userService;
    UserRepository userRepository;
    TaskRepository taskRepository;
    LabelRepository labelRepository;
    TaskMapper mapper;

    @BeforeEach
    void setUp() {
        HibernateConfiguration configuration = new HibernateConfiguration(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        userRepository = new UserRepository(configuration.getSessionFactory());
        labelRepository = new LabelRepository(configuration.getSessionFactory());
        taskRepository = new TaskRepository(configuration.getSessionFactory());
        taskService = new TaskService(taskRepository, TaskMapper.INSTANCE, resourceBundle);
        labelService = new LabelService(labelRepository, LabelMapper.INSTANCE, resourceBundle);
        userService = new UserService(userRepository, UserMapper.INSTANCE, resourceBundle);
        mapper = TaskMapper.INSTANCE;
    }

    @AfterEach
    void tearDown() {
        taskService.deleteEntities();
        userService.deleteEntities();
        labelService.deleteEntities();
    }

    @Test
    void should_create_task() {
        var label = createDefaultLabel(labelService);
        var user = createDefaultUser(userService, "default");
        var request = TaskRequest.builder()
                .title("Default Title")
                .description("Default Description")
                .assignee(user)
                .build();
        var labelNames = List.of(label.getLabel());
        taskService.createTask(request, labelNames);

        var tasks = taskService.getEntities();
        var task = tasks.getFirst();

        assertEquals(1, tasks.size());
        assertEquals(request.getTitle(), task.getTitle());
        assertEquals(request.getDescription(), task.getDescription());
        assertEquals(request.getAssignee().getUsername(), task.getAssignee().getUsername());
    }

    @Test
    void should_update_task() {
        var user = createDefaultUser(userService, "default");
        var label = createDefaultLabel(labelService);
        var task = createDefaultTask(taskService, user, label, "Task 1");
        var dto = taskService.getEntities().getFirst();
        var updateRequest = task.toBuilder()
                .id(dto.getId())
                .title("Updated Title")
                .build();
        var updated = taskService.updateEntity(updateRequest);
        assertThat(updated.getTitle(), equalTo(updateRequest.getTitle()));
    }

    @Test
    void should_delete_task() {
        var user = createDefaultUser(userService, "default");
        var label = createDefaultLabel(labelService);
        var task = createDefaultTask(taskService, user, label, "Task 1")
                .toBuilder()
                .id(1L)
                .build();
        createDefaultTask(taskService, user, label, "Task 2");
        taskService.deleteEntity(task);
        var tasks = taskService.getEntities();
        var deletedTask = taskService.getEntityById(task);

        assertEquals(1, tasks.size());
        assertThat(deletedTask, nullValue());
    }

    @Test
    void should_get_user_tasks() {
        var user1 = createDefaultUser(userService, "anton");
        var userId = userService.getUserByUsername(user1).getId();
        user1 = user1.toBuilder().id(userId).build();

        var user2 = createDefaultUser(userService, "john");
        var label = createDefaultLabel(labelService);
        createDefaultTask(taskService, user1, label, "Task 1");
        createTask(taskService, user1, label, "Task 2", Status.IN_PROGRESS);
        createDefaultTask(taskService, user2, label, "Task 3");


        var userTasks = taskService.getUserTasks(user1, Status.OPEN);
        assertEquals(1, userTasks.size());
        assertThat(userTasks.getFirst().getStatus(), equalTo(Status.OPEN));
        assertThat(userTasks.getFirst().getAssignee().getUsername(), equalTo(user1.getUsername()));
    }

    @Test
    void should_get_tasks_of_status() {
        var user1 = createDefaultUser(userService, "anton");
        var user2 = createDefaultUser(userService, "john");
        var label = createDefaultLabel(labelService);
        createDefaultTask(taskService, user1, label, "Task 1");
        createTask(taskService, user1, label, "Task 2", Status.IN_PROGRESS);
        createTask(taskService, user2, label, "Task 3", Status.DONE);
        createDefaultTask(taskService, user2, label, "Task 4");

        var openTasks = taskService.getTasksOfStatus(Status.OPEN);
        var taskStatuses = openTasks.stream().map(TaskDto::getStatus).toList();
        assertEquals(2, openTasks.size());
        assertThat(taskStatuses, not(contains(Status.IN_PROGRESS)));
    }

    @Test
    void title_validation_should_return_true_if_unique() {
        var user = createDefaultUser(userService, "anton");
        var label = createDefaultLabel(labelService);
        var task = createDefaultTask(taskService, user, label, "Task 1");
        var updated = task.toBuilder().title("Unique Title").build();

        assertThat(taskService.validateTitlePresence(updated), equalTo(true));
        assertThat(taskService.validateTitlePresence(task), equalTo(false));
    }
}