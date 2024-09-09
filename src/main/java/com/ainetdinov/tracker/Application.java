package com.ainetdinov.tracker;

import com.ainetdinov.tracker.command.generic.CreateCommand;
import com.ainetdinov.tracker.configuration.HibernateConfiguration;
import com.ainetdinov.tracker.constant.Status;
import com.ainetdinov.tracker.model.entity.Comment;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.model.entity.User;
import com.ainetdinov.tracker.model.mapper.CommentMapper;
import com.ainetdinov.tracker.model.mapper.LabelMapper;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.model.mapper.UserMapper;
import com.ainetdinov.tracker.repository.CommentRepository;
import com.ainetdinov.tracker.repository.LabelRepository;
import com.ainetdinov.tracker.repository.TaskRepository;
import com.ainetdinov.tracker.repository.UserRepository;
import com.ainetdinov.tracker.service.CommentService;
import com.ainetdinov.tracker.service.LabelService;
import com.ainetdinov.tracker.service.TaskService;
import com.ainetdinov.tracker.service.UserService;
import org.hibernate.SessionFactory;

public class Application {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new HibernateConfiguration().getSessionFactory();
        UserRepository userRepository = new UserRepository(sessionFactory);
        TaskRepository taskRepository = new TaskRepository(sessionFactory);
        CommentRepository commentRepository = new CommentRepository(sessionFactory);
        LabelRepository labelRepository = new LabelRepository(sessionFactory);

        LabelService labelService = new LabelService(labelRepository, LabelMapper.INSTANCE);
        TaskService taskService = new TaskService(taskRepository, TaskMapper.INSTANCE);
        UserService userService = new UserService(userRepository, UserMapper.INSTANCE);
        CommentService commentService = new CommentService(commentRepository, CommentMapper.INSTANCE);

        Label label = new Label();
        label.setLabel("Colorful label");

        Comment comment = new Comment();
        comment.setComment("Just a comment");

        User user = new User();
        user.setUsername("Anton");
        user.setPassword("1234");

        Task task = new Task();
        task.addLabel(label);
        task.addComment(comment);
        task.setStatus(Status.OPEN);
        task.setTitle("Colorful task");
        task.setAssignee(user);

        taskService.createEntity(task);
    }

    private static CreateCommand<User> createUser(UserRepository userRepository, Label label, int i) {
        User user = new User();
        user.setUsername("user "+i);
        user.setPassword("qwertyu12334");
        CreateCommand<User> createUserCommand = new CreateCommand<>(userRepository, user);

        Comment comment = new Comment();
        comment.setComment("First comment for user "+i);

        Comment comment2 = new Comment();
        comment2.setComment("Second comment for user "+i);

        Task task = new Task();
        task.setTitle("Task number "+i);
        task.setDescription("This is task description for user "+i);
        task.setStatus(Status.OPEN);

        task.addComment(comment);
        task.addComment(comment2);
        task.addLabel(label);
        user.addTask(task);
        return createUserCommand;
    }
}
