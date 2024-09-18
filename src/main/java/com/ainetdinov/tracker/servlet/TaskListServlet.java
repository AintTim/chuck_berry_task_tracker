package com.ainetdinov.tracker.servlet;

import com.ainetdinov.tracker.constant.Status;
import com.ainetdinov.tracker.model.dto.TaskDto;
import com.ainetdinov.tracker.model.request.UserRequest;
import com.ainetdinov.tracker.service.HttpService;
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
import java.util.List;
import java.util.function.Function;

import static com.ainetdinov.tracker.constant.WebConstant.*;

@WebServlet(API + TASKS + SLASH + ASTERISK)
public class TaskListServlet extends HttpServlet {
    private TaskService taskService;
    private UserService userService;
    private HttpService httpService;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        taskService = (TaskService) context.getAttribute(TASK_SERVICE);
        userService = (UserService) context.getAttribute(USER_SERVICE);
        httpService = (HttpService) context.getAttribute(HTTP_SERVICE);
        mapper = (ObjectMapper) context.getAttribute(MAPPER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getQueryString() != null) {
            var id = extractQuery(req);
            var user = UserRequest.builder().id(id).build();
            Function<Status, List<TaskDto>> getUserTasks =
                    status -> taskService.getUserTasks(user, status);
            forwardAttributes(req, resp, getUserTasks);
        } else if (req.getPathInfo() == null) {
            Function<Status, List<TaskDto>> getTasks =
                    status -> taskService.getTasksOfStatus(status);
            forwardAttributes(req, resp, getTasks);
        } else {
            Status status = Status.getStatus(httpService.extractPath(req));
            var tasks = taskService.getTasksOfStatus(status);
            httpService.sendJsonObject(mapper, resp, tasks, HttpServletResponse.SC_OK);
        }
    }

    private long extractQuery(HttpServletRequest req) {
        int valueIndex = 1;
        String delimiter = "=";
        return Long.parseLong(req.getQueryString().split(delimiter)[valueIndex]);
    }

    private void forwardAttributes(HttpServletRequest req, HttpServletResponse resp, Function<Status, List<TaskDto>> function) throws ServletException, IOException {
        req.setAttribute(OPEN, function.apply(Status.OPEN));
        req.setAttribute(IN_PROGRESS, function.apply(Status.IN_PROGRESS));
        req.setAttribute(DONE, function.apply(Status.DONE));
        req.setAttribute(STATUSES, Status.values());
        req.setAttribute(USERS, userService.getEntities());
        req.getRequestDispatcher(TASKS_JSP).forward(req, resp);
    }
}
