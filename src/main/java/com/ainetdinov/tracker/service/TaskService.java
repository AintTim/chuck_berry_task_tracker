package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.command.GetTasksWithStatus;
import com.ainetdinov.tracker.command.generic.UpdateCommand;
import com.ainetdinov.tracker.command.remove.RemoveTask;
import com.ainetdinov.tracker.command.remove.RemoveTaskComment;
import com.ainetdinov.tracker.constant.Status;
import com.ainetdinov.tracker.model.dto.TaskDto;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskService extends EntityService<Task> {
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        super(taskRepository);
        this.taskMapper = taskMapper;
    }

    public List<TaskDto> getTasks() {
        var tasks = getEntities();
        return toDtoList(tasks);
    }

    public List<TaskDto> getTasksOfStatus(Status status) {
        var tasks = executeCommand(new GetTasksWithStatus(repository, status));
        return toDtoList(tasks);
    }

    public TaskDto getTaskById(Long id) {
        return taskMapper.toDto(getEntityById(id));
    }

    @Override
    public TaskDto updateEntity(Task task) {
        return taskMapper.toDto(executeCommand(new UpdateCommand<>(repository, task)));
    }

    @Override
    public void deleteEntity(Long id) {
        executeCommand(new RemoveTask(repository, id));
    }

    public void deleteComment(Long taskId, Long commentId) {
        executeCommand(new RemoveTaskComment(repository, taskId, commentId));
    }

    private List<TaskDto> toDtoList(List<Task> tasks) {
        List<TaskDto> dtoList = new ArrayList<>();
        for (var task : tasks) {
            dtoList.add(taskMapper.toDto(task));
        }
        return dtoList;
    }
}
