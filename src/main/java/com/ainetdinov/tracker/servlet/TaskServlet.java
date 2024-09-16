package com.ainetdinov.tracker.servlet;

import com.ainetdinov.tracker.constant.Status;
import com.ainetdinov.tracker.model.request.TaskRequest;
import com.ainetdinov.tracker.model.request.UserRequest;
import com.ainetdinov.tracker.service.HttpService;
import com.ainetdinov.tracker.service.LabelService;
import com.ainetdinov.tracker.service.TaskService;
import com.ainetdinov.tracker.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.ainetdinov.tracker.constant.WebConstant.*;

@WebServlet(API + TASK + SLASH + ASTERISK)
public class TaskServlet extends HttpServlet {
    private TaskService taskService;
    private LabelService labelService;
    private UserService userService;
    private HttpService httpService;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        taskService = (TaskService) context.getAttribute(TASK_SERVICE);
        labelService = (LabelService) context.getAttribute(LABEL_SERVICE);
        userService = (UserService) context.getAttribute(USER_SERVICE);
        httpService = (HttpService) context.getAttribute(HTTP_SERVICE);
        mapper = (ObjectMapper) context.getAttribute(MAPPER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null) {
            req.setAttribute(LABELS, labelService.getEntities());
            req.setAttribute(USERS, userService.getEntities());
            req.getRequestDispatcher(CREATE_TASK_JSP).forward(req, resp);
        } else if (req.getPathInfo().contains(COMMENTS)) {
            var object = httpService.getObjectFromRequestPath(mapper, req, TaskRequest.class);
            var task = taskService.getEntityById(object);
            req.setAttribute(TASK, task);
            req.getRequestDispatcher(COMMENTS_JSP).forward(req, resp);
        } else {
            var object = httpService.getObjectFromRequestPath(mapper, req, TaskRequest.class);
            var task = taskService.getEntityById(object);
            req.setAttribute(LABELS, labelService.getEntities());
            req.setAttribute(USERS, userService.getEntities());
            req.setAttribute(TASK, task);
            req.getRequestDispatcher(EDIT_TASK_JSP).forward(req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        var object = httpService.getObjectFromRequestPath(mapper, req, TaskRequest.class);
        taskService.deleteEntity(object);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getPathInfo().contains(STATUS)) {
            var task = httpService.getObjectFromRequestPath(mapper, req, TaskRequest.class);
            var status = Status.getStatus(httpService.extractLastPathPart(req));
            task = task.toBuilder().status(status).build();

            taskService.updateTaskStatus(task);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            var task = httpService.getObjectFromRequest(mapper, req, TaskRequest.class);
            if (validateField(req, taskService::validateDescriptionLength, task, ERROR_DESC_LENGTH)) {
                httpService.sendJsonObject(mapper, resp, taskService.getMessage(ERROR_DESC_LENGTH), HttpServletResponse.SC_NOT_ACCEPTABLE);
                return;
            }

            if (validateField(req, taskService::validateLabelsNumber, task, ERROR_LABELS_COUNT)) {
                httpService.sendJsonObject(mapper, resp, taskService.getMessage(ERROR_LABELS_COUNT), HttpServletResponse.SC_NOT_ACCEPTABLE);
                return;
            }

            var updated = taskService.updateEntity(task);
            req.setAttribute(TASK, updated);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var title = req.getParameter(TITLE);
        var description = req.getParameter(DESCRIPTION);
        var delimiter = ", ";
        var labels = req.getParameter(SELECTED_LABELS).split(delimiter);
        var user = req.getParameter(USER_NAME);

        TaskRequest task = TaskRequest.builder()
                .title(title)
                .description(description)
                .assignee(UserRequest.builder().username(user).build())
                .build();

        if (validateField(req, taskService::validateTitlePresence, task, ERROR_TASK_TITLE_EXISTS)) {
            req.setAttribute(DESCRIPTION, description);
            req.getRequestDispatcher(CREATE_TASK_JSP).forward(req, resp);
            return;
        }
        if (validateField(req, taskService::validateDescriptionLength, task, ERROR_DESC_LENGTH)) {
            req.setAttribute(TITLE, title);
            req.getRequestDispatcher(CREATE_TASK_JSP).forward(req, resp);
            return;
        }
        if (validateField(req, taskService::validateLabelsNumber, labels, ERROR_LABELS_COUNT)) {
            req.setAttribute(TITLE, title);
            req.setAttribute(DESCRIPTION, description);
            req.getRequestDispatcher(CREATE_TASK_JSP).forward(req, resp);
            return;
        }

        taskService.createTask(task, List.of(labels));
        resp.sendRedirect(req.getContextPath() + API + TASKS);
    }

    private <T> boolean validateField(HttpServletRequest req, Predicate<T> condition, T field, String errorMessage) {
        if (!condition.test(field)) {
            req.setAttribute(ERROR, taskService.getMessage(errorMessage));
            req.setAttribute(LABELS, labelService.getEntities());
            req.setAttribute(USERS, userService.getEntities());
            req.setAttribute(STATUSES, Status.values());
            return true;
        }
        return false;
    }
}
