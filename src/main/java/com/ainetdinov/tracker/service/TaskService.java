package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.command.create.CreateTask;
import com.ainetdinov.tracker.command.get.GetTaskByTitle;
import com.ainetdinov.tracker.command.get.GetTasksWithStatus;
import com.ainetdinov.tracker.command.get.GetUserTasks;
import com.ainetdinov.tracker.command.remove.RemoveTask;
import com.ainetdinov.tracker.command.update.UpdateTask;
import com.ainetdinov.tracker.command.update.UpdateTaskComments;
import com.ainetdinov.tracker.command.update.UpdateTaskStatus;
import com.ainetdinov.tracker.constant.Status;
import com.ainetdinov.tracker.model.dto.TaskDto;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.model.request.TaskRequest;
import com.ainetdinov.tracker.model.request.UserRequest;
import com.ainetdinov.tracker.repository.TaskRepository;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.ainetdinov.tracker.constant.WebConstant.DESC_MAX_LENGTH;
import static com.ainetdinov.tracker.constant.WebConstant.LABELS_MAX;

@Log4j2
public class TaskService extends EntityService<Task, TaskDto, TaskRequest> {

    public TaskService(TaskRepository taskRepository, TaskMapper mapper, ResourceBundle messages) {
        super(taskRepository, mapper, messages);
    }

    public List<TaskDto> getUserTasks(UserRequest request, Status status) {
        log.debug("Command: GetUserTasks\tget {} tasks assigned to user {}", status, request.getId());
        return executeCommand(new GetUserTasks((TaskRepository) repository, (TaskMapper) mapper, request, status));
    }

    public List<TaskDto> getTasksOfStatus(Status status) {
        log.debug("Command: GetTasksWithStatus\tget {} tasks", status);
        return executeCommand(new GetTasksWithStatus((TaskRepository) repository, (TaskMapper) mapper, status));
    }

    public void createTask(TaskRequest task, List<String> labels) {
        log.debug("Command: CreateTask\tcreate new task '{}'", task.getTitle());
        executeCommand(new CreateTask((TaskRepository) repository, (TaskMapper) mapper, task, labels));
    }

    public TaskDto updateTaskComments(TaskRequest request) {
        log.debug("Command: UpdateTaskComments\tupdate task '{}' comments", request.getId());
        return executeCommand(new UpdateTaskComments((TaskRepository) repository, (TaskMapper) mapper, request));
    }

    public void updateTaskStatus(TaskRequest taskRequest) {
        log.debug("Command: UpdateTaskStatus\tset task '{}' status to {}", taskRequest.getId(), taskRequest.getStatus());
        executeCommand(new UpdateTaskStatus((TaskRepository) repository, (TaskMapper) mapper, taskRequest));
    }

    @Override
    public TaskDto updateEntity(TaskRequest taskRequest) {
        log.debug("Command: UpdateTask\tupdate task {}", taskRequest.getId());
        return executeCommand(new UpdateTask((TaskRepository) repository, (TaskMapper) mapper, taskRequest));
    }

    @Override
    public void deleteEntity(TaskRequest taskRequest) {
        log.debug("Command: RemoveTask\tdelete task {}", taskRequest.getId());
        executeCommand(new RemoveTask(repository, taskRequest));
    }

    public boolean validateLabelsNumber(TaskRequest request) {
        log.debug("Validate task {} labels number: {}", request.getId(), request.getLabels());
        return !request.getLabels().isEmpty() && request.getLabels().size() <= LABELS_MAX;
    }

    public boolean validateLabelsNumber(String[] labels) {
        log.debug("Validate labels number: {}", labels.length);
        return !labels[0].isEmpty() && labels.length <= LABELS_MAX;
    }

    public boolean validateTitlePresence(TaskRequest request) {
        log.debug("Command: GetTaskByTitle\tget task with title {}", request.getTitle());
        var task = executeCommand(new GetTaskByTitle((TaskRepository) repository, request.getTitle()));
        return Objects.isNull(task);
    }

    public boolean validateDescriptionLength(TaskRequest request) {
        log.debug("Validate task description:\n{}", request.getDescription());
        return request.getDescription().length() <= DESC_MAX_LENGTH;
    }
}
