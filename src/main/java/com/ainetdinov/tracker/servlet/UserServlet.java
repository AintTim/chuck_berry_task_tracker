package com.ainetdinov.tracker.servlet;

import com.ainetdinov.tracker.model.request.UserRequest;
import com.ainetdinov.tracker.service.UserService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.ainetdinov.tracker.constant.WebConstant.*;

@WebServlet(API + USER + SLASH + ASTERISK)
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        userService = (UserService) context.getAttribute(USER_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var username = req.getParameter(USER_NAME);
        var password = req.getParameter(PASSWORD);
        var user = UserRequest.builder().username(username).password(password).build();
        if (!userService.validateUserPresence(user)) {
            req.setAttribute(ERROR, userService.getMessage(ERROR_USER_VALIDATION));
            req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + API + TASKS);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var username = req.getParameter(USER_NAME);
        var password = req.getParameter(PASSWORD);
        UserRequest user = UserRequest.builder().username(username).password(password).build();
        if (!userService.validateUsernameAvailability(user)) {
            req.setAttribute(ERROR, userService.getMessage(ERROR_USERNAME_VALIDATION));
            req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
            return;
        }
        if (!userService.validatePasswordLength(user)) {
            req.setAttribute(ERROR, userService.getMessage(ERROR_PASSWORD_VALIDATION));
            req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
            return;
        }
        userService.createEntity(user);
        resp.sendRedirect(req.getContextPath() + API + TASKS);
    }
}
