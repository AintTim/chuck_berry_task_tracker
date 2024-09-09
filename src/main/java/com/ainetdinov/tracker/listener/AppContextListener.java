package com.ainetdinov.tracker.listener;

import com.ainetdinov.tracker.configuration.HibernateConfiguration;
import com.ainetdinov.tracker.constant.WebConstant;
import com.ainetdinov.tracker.model.mapper.LabelMapper;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.repository.LabelRepository;
import com.ainetdinov.tracker.repository.TaskRepository;
import com.ainetdinov.tracker.service.HttpService;
import com.ainetdinov.tracker.service.LabelService;
import com.ainetdinov.tracker.service.TaskService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    private ServletContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        HibernateConfiguration config = new HibernateConfiguration();
        HttpService httpService = new HttpService();

        LabelRepository labelRepository = new LabelRepository(config.getSessionFactory());
        LabelService labelService = new LabelService(labelRepository, LabelMapper.INSTANCE);
        TaskRepository taskRepository = new TaskRepository(config.getSessionFactory());
        TaskService taskService = new TaskService(taskRepository, TaskMapper.INSTANCE);

        context.setAttribute(WebConstant.HTTP_SERVICE, httpService);
        context.setAttribute(WebConstant.LABEL_SERVICE, labelService);
        context.setAttribute(WebConstant.TASK_SERVICE, taskService);

        ServletContextListener.super.contextInitialized(sce);
    }
}
