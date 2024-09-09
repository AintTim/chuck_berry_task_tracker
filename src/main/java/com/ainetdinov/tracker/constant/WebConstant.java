package com.ainetdinov.tracker.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebConstant {
    public static final String HIBER_CONFIG = "hibernateConfig";

    // services
    public static final String LABEL_SERVICE = "labelService";
    public static final String TASK_SERVICE = "taskService";
    public static final String HTTP_SERVICE = "httpService";

    // endpoints
    public static final String SLASH = "/";
    public static final String ASTERISK = "*";
    public static final String API = "/api/";
    public static final String LABELS = "labels";
    public static final String TASKS = "tasks";
    public static final String TASK = "task";

    // jsp
    public static final String INDEX_JSP = "/index.jsp";
    public static final String TRACKER_JSP = "/tracker.jsp";

    // attributes
    public static final String OPEN = "open";
    public static final String IN_PROGRESS = "in_progress";
    public static final String DONE = "done";
}
