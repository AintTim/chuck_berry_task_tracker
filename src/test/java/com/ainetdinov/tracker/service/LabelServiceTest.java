package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.base.BaseTest;
import com.ainetdinov.tracker.configuration.HibernateConfiguration;
import com.ainetdinov.tracker.model.mapper.LabelMapper;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.model.mapper.UserMapper;
import com.ainetdinov.tracker.model.request.LabelRequest;
import com.ainetdinov.tracker.repository.LabelRepository;
import com.ainetdinov.tracker.repository.TaskRepository;
import com.ainetdinov.tracker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.*;

class LabelServiceTest extends BaseTest {
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
    void should_create_label() {
        var request = LabelRequest.builder().label("Custom Label").build();
        labelService.createEntity(request);
        var labels = labelService.getEntities();
        var label = labels.getFirst();

        assertEquals(1, labels.size());
        assertEquals(request.getLabel(), label.getLabel());
        assertEquals(request.getColor(), label.getColor());
    }

    @Test
    void should_update_label() {
        var label = createDefaultLabel(labelService);
        var blueColor = "#b1c8df";
        label = label.toBuilder().color(blueColor).build();
        var updated = labelService.updateEntity(label);
        assertThat(updated.getColor(), Matchers.equalTo(blueColor));
    }

    @Test
    void should_delete_label() {
        var request = createDefaultLabel(labelService);
        createLabel(labelService, "Additional label");
        var labelId = labelService.getLabelByName(request).getId();

        labelService.deleteEntity(new LabelRequest(labelId));
        var labels = labelService.getEntities();
        var deletedLabel = labelService.getLabelByName(request);

        assertEquals(1, labels.size());
        assertThat(deletedLabel, nullValue());
    }

    @Test
    void should_get_label_by_name_or_null() {
        var nonExistentLabel = labelService.getLabelByName(new LabelRequest("Default"));
        var label = createDefaultLabel(labelService);
        var presentLabel = labelService.getLabelByName(label);

        assertThat(nonExistentLabel, nullValue());
        assertThat(presentLabel, notNullValue());
        assertThat(presentLabel.getLabel(), equalTo(label.getLabel()));
    }

    @Test
    void should_return_true_if_any_task_remain_without_any_label() {
        var user = createDefaultUser(userService, "default");
        var label = createDefaultLabel(labelService);
        createDefaultTask(taskService, user, label, "Task 1");

        var labelId = labelService.getLabelByName(label).getId();
        boolean isDeletingProhibited = labelService.validateLabelDeletionAvailability(new LabelRequest(labelId));
        assertTrue(isDeletingProhibited);
    }

    @Test
    void should_return_false_if_no_task_remain_without_label() {
        var user = createDefaultUser(userService, "default");
        var label1 = createDefaultLabel(labelService);
        var label2 = createLabel(labelService, "Additional Label");
        var task = createDefaultTask(taskService, user, label1, "Task 1");
        var dto = taskService.getEntities().getFirst();
        var updateRequest = task.toBuilder()
                .id(dto.getId())
                .labels(List.of(label1, label2))
                .build();
        taskService.updateEntity(updateRequest);

        var labelId = labelService.getLabelByName(label1).getId();
        boolean isDeletingProhibited = labelService.validateLabelDeletionAvailability(new LabelRequest(labelId));
        assertFalse(isDeletingProhibited);
    }

    @Test
    void should_return_false_if_label_not_related_to_any_task() {
        var label = createDefaultLabel(labelService);
        var labelId = labelService.getLabelByName(label).getId();

        boolean isDeletingProhibited = labelService.validateLabelDeletionAvailability(new LabelRequest(labelId));
        assertFalse(isDeletingProhibited);
    }
}