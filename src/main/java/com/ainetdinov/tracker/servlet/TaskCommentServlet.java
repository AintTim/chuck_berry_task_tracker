package com.ainetdinov.tracker.servlet;

import com.ainetdinov.tracker.model.request.TaskRequest;
import com.ainetdinov.tracker.service.HttpService;
import com.ainetdinov.tracker.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.ainetdinov.tracker.constant.WebConstant.*;

@WebServlet(API + COMMENTS + SLASH + ASTERISK)
public class TaskCommentServlet extends HttpServlet {
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
        ObjectMapper mapper = new ObjectMapper();
        var object = httpService.getObjectFromRequestPath(mapper, req, TaskRequest.class);
        var task = taskService.getEntityById(object);
        req.setAttribute(TASK, task);
        req.getRequestDispatcher(COMMENTS_JSP).forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        ObjectMapper mapper = new ObjectMapper();
        var task = httpService.getObjectFromRequest(mapper, req, TaskRequest.class);
        var updated = taskService.updateTaskComments(task);
        req.setAttribute(TASK, updated);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
