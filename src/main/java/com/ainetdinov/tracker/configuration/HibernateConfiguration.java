package com.ainetdinov.tracker.configuration;

import com.ainetdinov.tracker.model.entity.Comment;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.model.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfiguration {

    public SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration()
                .setProperty("hibernate.connection,driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5431/task_tracker")
                .setProperty("hibernate.connection.username", "root")
                .setProperty("hibernate.connection.password", "root")
                .setProperty("hibernate.hbm2ddl.auto", "validate")
                .setProperty("hibernate.show_sql", "true");

        configuration
                .addAnnotatedClass(Label.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Comment.class)
                .addAnnotatedClass(Task.class);

        return configuration.buildSessionFactory();
    }
}
