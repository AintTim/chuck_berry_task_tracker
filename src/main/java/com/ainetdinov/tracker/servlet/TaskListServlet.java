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

@WebServlet(API + TASKS)
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
        var tasks = taskService.getTasks();
        var openTasks = tasks.stream().filter(t -> t.getStatus().equals(Status.OPEN)).toList();
        var inProgressTasks = tasks.stream().filter(t -> t.getStatus().equals(Status.IN_PROGRESS)).toList();
        var doneTasks = tasks.stream().filter(t -> t.getStatus().equals(Status.DONE)).toList();
        req.setAttribute(OPEN, openTasks);
        req.setAttribute(IN_PROGRESS, inProgressTasks);
        req.setAttribute(DONE, doneTasks);
        req.getRequestDispatcher(TRACKER_JSP).forward(req, resp);
    }
}
