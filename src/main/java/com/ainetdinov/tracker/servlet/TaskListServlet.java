package com.ainetdinov.tracker.servlet;

import com.ainetdinov.tracker.constant.Status;
import com.ainetdinov.tracker.service.HttpService;
import com.ainetdinov.tracker.service.TaskService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.ainetdinov.tracker.constant.WebConstant.*;

@WebServlet(API + TASKS + SLASH + ASTERISK)
public class TaskListServlet extends HttpServlet {
    private TaskService taskService;
    private HttpService httpService;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        taskService = (TaskService) context.getAttribute(TASK_SERVICE);
        httpService = (HttpService) context.getAttribute(HTTP_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null) {
            req.setAttribute(OPEN, taskService.getTasksOfStatus(Status.OPEN));
            req.setAttribute(IN_PROGRESS, taskService.getTasksOfStatus(Status.IN_PROGRESS));
            req.setAttribute(DONE, taskService.getTasksOfStatus(Status.DONE));
            req.setAttribute(STATUSES, Status.values());
            req.getRequestDispatcher(TASKS_JSP).forward(req, resp);
        } else {
            Status status = Status.getStatus(httpService.extractPath(req));
            var tasks = taskService.getTasksOfStatus(status);
            httpService.sendJsonObject(resp, tasks);
        }
    }
}
