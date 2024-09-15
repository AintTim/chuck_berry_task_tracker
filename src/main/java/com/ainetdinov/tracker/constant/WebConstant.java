package com.ainetdinov.tracker.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebConstant {
    public static final String HIBER_CONFIG = "hibernateConfig";

    // services
    public static final String LABEL_SERVICE = "labelService";
    public static final String TASK_SERVICE = "taskService";
    public static final String HTTP_SERVICE = "httpService";
    public static final String USER_SERVICE = "userService";

    // endpoints
    public static final String SLASH = "/";
    public static final String ASTERISK = "*";
    public static final String API = "/api/";
    public static final String LABELS = "labels";
    public static final String TASKS = "tasks";
    public static final String TASK = "task";
    public static final String USERS = "users";
    public static final String USER = "user";
    public static final String COMMENTS = "comments";

    // jsp
    public static final String INDEX_JSP = "/index.jsp";
    public static final String TASKS_JSP = "/tasks.jsp";
    public static final String CREATE_TASK_JSP = "/createTask.jsp";
    public static final String EDIT_TASK_JSP = "/editTask.jsp";
    public static final String COMMENTS_JSP = "/comments.jsp";
    public static final String LABELS_JSP = "/labels.jsp";

    // attributes
    public static final String OPEN = "open";
    public static final String IN_PROGRESS = "in_progress";
    public static final String DONE = "done";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String STATUS = "status";
    public static final String STATUSES = "statuses";
    public static final String SELECTED_LABELS = "selectedLabels";
    public static final String ERROR = "error";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";

    // error messages
    public static final String ERROR_TASK_TITLE_EXISTS = "error.task.title.exists";
    public static final String ERROR_LABEL_NAME_EXISTS = "error.label.name.exists";
    public static final String ERROR_LABELS_COUNT ="error.labels.count";
    public static final String ERROR_DESC_LENGTH ="error.desc.length";
    public static final String ERROR_USER_VALIDATION ="error.user.validation";
    public static final String ERROR_PASSWORD_VALIDATION ="error.password.validation";
    public static final String ERROR_USERNAME_VALIDATION ="error.username.validation";
    public static final String ERROR_LABEL_VALIDATION ="error.label.validation";

    // constraints
    public static final int LABELS_MAX = 3;
    public static final int DESC_MAX_LENGTH = 150;
}
