package com.ainetdinov.tracker.listener;

import com.ainetdinov.tracker.configuration.HibernateConfiguration;
import com.ainetdinov.tracker.constant.WebConstant;
import com.ainetdinov.tracker.model.mapper.LabelMapper;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.model.mapper.UserMapper;
import com.ainetdinov.tracker.repository.LabelRepository;
import com.ainetdinov.tracker.repository.TaskRepository;
import com.ainetdinov.tracker.repository.UserRepository;
import com.ainetdinov.tracker.service.HttpService;
import com.ainetdinov.tracker.service.LabelService;
import com.ainetdinov.tracker.service.TaskService;
import com.ainetdinov.tracker.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.ResourceBundle;

import static com.ainetdinov.tracker.constant.WebConstant.*;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        ObjectMapper mapper = new ObjectMapper();
        HttpService httpService = new HttpService();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");
        HibernateConfiguration config = new HibernateConfiguration(
                resourceBundle.getString(DB_URL),
                resourceBundle.getString(DB_USERNAME),
                resourceBundle.getString(DB_PASSWORD));

        LabelRepository labelRepository = new LabelRepository(config.getSessionFactory());
        TaskRepository taskRepository = new TaskRepository(config.getSessionFactory());
        UserRepository userRepository = new UserRepository(config.getSessionFactory());

        LabelService labelService = new LabelService(labelRepository, LabelMapper.INSTANCE, resourceBundle);
        UserService userService = new UserService(userRepository, UserMapper.INSTANCE, resourceBundle);
        TaskService taskService = new TaskService(taskRepository, TaskMapper.INSTANCE, resourceBundle);

        context.setAttribute(MAPPER, mapper);
        context.setAttribute(HTTP_SERVICE, httpService);
        context.setAttribute(LABEL_SERVICE, labelService);
        context.setAttribute(TASK_SERVICE, taskService);
        context.setAttribute(USER_SERVICE, userService);

        ServletContextListener.super.contextInitialized(sce);
    }
}
