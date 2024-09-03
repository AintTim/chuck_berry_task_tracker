package com.ainetdinov.tracker.listener;

import com.ainetdinov.tracker.configuration.HibernateConfiguration;
import com.ainetdinov.tracker.constant.WebConstant;
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

        context.setAttribute(WebConstant.HIBER_CONFIG, config);

        ServletContextListener.super.contextInitialized(sce);
    }
}
