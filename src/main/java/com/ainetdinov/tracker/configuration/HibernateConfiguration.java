package com.ainetdinov.tracker.configuration;

import com.ainetdinov.tracker.model.entity.Comment;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@RequiredArgsConstructor
public class HibernateConfiguration {
    private final String url;
    private final String username;
    private final String password;

    public SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration()
                .setProperty("hibernate.connection,driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", url)
                .setProperty("hibernate.connection.username", username)
                .setProperty("hibernate.connection.password", password)
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
