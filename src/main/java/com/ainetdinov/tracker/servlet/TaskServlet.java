package com.ainetdinov.tracker.servlet;

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

@WebServlet(API + TASK + SLASH + ASTERISK)
public class TaskServlet extends HttpServlet {
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
        var task = taskService.getTaskById(httpService.extractId(req));
        req.setAttribute(TASK, task);
        req.getRequestDispatcher(TRACKER_JSP).forward(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        taskService.deleteEntity(httpService.extractId(req));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
