package com.ainetdinov.tracker.base;

import com.ainetdinov.tracker.configuration.HibernateConfiguration;
import com.ainetdinov.tracker.constant.Status;
import com.ainetdinov.tracker.model.request.CommentRequest;
import com.ainetdinov.tracker.model.request.LabelRequest;
import com.ainetdinov.tracker.model.request.TaskRequest;
import com.ainetdinov.tracker.model.request.UserRequest;
import com.ainetdinov.tracker.service.LabelService;
import com.ainetdinov.tracker.service.TaskService;
import com.ainetdinov.tracker.service.UserService;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.ResourceBundle;

public class BaseTest {
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    public static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");
    public static Connection connection;
    public static Liquibase liquibase;

    @BeforeAll
    public static void beforeAll() throws Exception {
        postgres.start();
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    protected LabelRequest createDefaultLabel(LabelService service) {
        var request = LabelRequest.builder().label("Default").build();
        service.createEntity(request);
        return request;
    }

    protected LabelRequest createLabel(LabelService service, String name) {
        var request = LabelRequest.builder().label(name).build();
        service.createEntity(request);
        return request;
    }

    protected UserRequest createDefaultUser(UserService service, String username) {
        var request = UserRequest.builder().username(username).password("qwertyu").build();
        service.createEntity(request);
        return request;
    }

    protected UserRequest createUser(UserService service, String username, Long id) {
        var request = UserRequest.builder().username(username).password("qwertyu").build();
        service.createEntity(request);
        return request.toBuilder().id(id).build();
    }

    protected CommentRequest createComment(String comment) {
        return CommentRequest.builder().comment(comment).build();
    }

    protected TaskRequest createDefaultTask(TaskService taskService, UserRequest user, LabelRequest label, String title) {
        var request = TaskRequest.builder()
                .title(title)
                .description("Default description")
                .assignee(user)
                .build();
        taskService.createTask(request, List.of(label.getLabel()));
        return request;
    }

    protected TaskRequest createTask(TaskService taskService, UserRequest user, LabelRequest label, String title, Status status) {
        var request = TaskRequest.builder()
                .title(title)
                .description("Default description")
                .assignee(user)
                .status(status)
                .build();
        taskService.createTask(request, List.of(label.getLabel()));
        return request;
    }
}
