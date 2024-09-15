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
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.ResourceBundle;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        HibernateConfiguration config = new HibernateConfiguration();
        HttpService httpService = new HttpService();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

        LabelRepository labelRepository = new LabelRepository(config.getSessionFactory());
        TaskRepository taskRepository = new TaskRepository(config.getSessionFactory());
        UserRepository userRepository = new UserRepository(config.getSessionFactory());

        LabelService labelService = new LabelService(labelRepository, LabelMapper.INSTANCE, resourceBundle);
        UserService userService = new UserService(userRepository, UserMapper.INSTANCE, resourceBundle);
        TaskService taskService = new TaskService(taskRepository, TaskMapper.INSTANCE, resourceBundle);

        context.setAttribute(WebConstant.HTTP_SERVICE, httpService);
        context.setAttribute(WebConstant.LABEL_SERVICE, labelService);
        context.setAttribute(WebConstant.TASK_SERVICE, taskService);
        context.setAttribute(WebConstant.USER_SERVICE, userService);

        ServletContextListener.super.contextInitialized(sce);
    }
}
