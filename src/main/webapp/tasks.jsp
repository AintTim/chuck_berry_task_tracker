<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Мои задачи</title>
    <style>
        /* Filter styles */
        .filter-container {
            display: flex;
            align-items: center;
        }

        .filter-dropdown {
            position: relative;
            display: inline-block;
            margin-right: 10px;
        }

        .filter-button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            border-radius: 4px;
            font-size: 14px;
            font-weight: bold;
        }

        .filter-dropdown-content {
            display: none;
            position: absolute;
            background-color: #f1f1f1;
            min-width: 160px;
            box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
            z-index: 1;
            border-radius: 4px;
        }

        .filter-dropdown-content .filter-option {
            color: black;
            padding: 12px 16px;
            text-decoration: none;
            display: block;
            cursor: pointer;
        }

        .filter-dropdown-content .filter-option:hover {
            background-color: #ddd;
        }

        /* Filter reset button style */
        .filter-reset-button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            text-decoration: none;
            font-size: 14px;
            font-weight: bold;
            border-radius: 4px;
            transition: background-color 0.3s ease;
        }

        .filter-reset-button:hover {
            background-color: #45a049;
        }

        /* Header styles */
        header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20px;
            background-color: #f5f5f5;
            border-bottom: 1px solid #ddd;
        }

        header h2, header a {
            font-size: 24px;
            font-weight: bold;
            color: #333;
            margin: 0;
            display: inline-block;
            vertical-align: middle;
        }

        header a {
            text-decoration: none;
            transition: color 0.3s ease;
            margin-left: 20px;
        }

        header a:hover {
            color: #4CAF50;
        }

        h2 {
            font-size: 24px;
            margin: 0;
        }
        /* Global styles */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }

        /* Board styles */
        .board {
            display: flex;
            gap: 20px;
        }

        .column {
            background-color: white;
            padding: 10px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            flex: 1;
            position: relative;
            min-height: 200px;
        }

        .column-title {
            display: inline-block;
            padding: 5px;
            font-weight: bold;
            margin-bottom: 5px;
            border-radius: 8px;
        }

        .open .column-title {
            background-color: rgba(255, 173, 173, 0.5);
        }

        .in-progress .column-title {
            background-color: rgba(173, 173, 255, 0.5);
        }

        .done .column-title {
            background-color: rgba(173, 255, 173, 0.5);
        }

        .task-counter {
            display: inline-block;
            padding: 5px;
            margin-left: 10px;
            border-radius: 8px;
            background-color: rgba(200, 200, 200, 0.5);
            font-weight: bold;
        }

        .add-task-icon {
            position: absolute;
            top: 10px;
            right: 10px;
            font-size: 15px;
            cursor: pointer;
            color: #888;
            text-decoration: none;
        }

        .add-task-icon:hover,
        .edit-button-icon:hover,
        .comment-icon:hover {
            color: rgba(96, 44, 182, 0.45);
        }

        /* Task styles */
        .task {
            background-color: #f9f9f9;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 6px;
            margin-bottom: 10px;
            position: relative;
        }

        .edit-button-icon {
            background: none;
            text-decoration: none;
            border: none;
            cursor: pointer;
            color: #888;
            font-size: 16px;
            padding: 0;
            margin: 0;
        }

        .delete-button {
            color: red;
            background: none;
            border: none;
            cursor: pointer;
            padding: 0;
            margin: 0;
        }

        .assigned {
            font-size: 14px;
            font-weight: bold;
            margin-bottom: 5px;
            color: #555;
        }

        .description {
            margin-top: 10px;
            padding: 10px;
            background-color: #f7f7f7;
            border-radius: 5px;
            border: 1px solid #ddd;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            font-style: italic;
            color: #555;
        }

        /* Comment styles */
        .comment-container {
            display: flex;
            gap: 5px;
            padding-top: 5px;
            align-items: center;
            font-size: 1.2em;
            color: #666;
            cursor: pointer;
            justify-content: flex-end;
        }

        .comment-container .comment-icon {
            margin-right: 5px;
            text-decoration: none;
            color: #888;
        }

        .comment-container .comment-count {
            font-size: 0.8em;
            color: #888;
            margin-left: 5px;
            margin-top: 3px;
        }

        /* Label styles */
        .labels {
            flex: 1 1 50%;
            display: flex;
            flex-direction: column;
            gap: 5px;
        }

        .label {
            font-size: 12px;
            padding: 4px 8px;
            border-radius: 4px;
            color: white;
            background-color: rgba(255, 165, 0, 0.7);
            cursor: pointer;
        }

        .task-form h2 {
            text-align: center;
            margin-bottom: 20px;
        }

        .task-form .labels {
            display: flex;
            gap: 5px;
            margin-bottom: 10px;
        }

        .task-form label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        .task-form input[type="text"],
        .task-form select {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 14px;
        }

        .task-form select:focus,
        .task-form input[type="text"]:focus {
            border-color: #007BFF;
            outline: none;
            box-shadow: 0 0 2px rgba(0,123,255,0.5);
        }

        .task-buttons {
            position: absolute;
            top: 0;
            right: 0;
            display: flex;
            align-items: baseline;
            gap: 15px;
            padding: 5px;
            background-color: #f9f9f9;
            border-radius: 5px;
            min-width: 70px;
            justify-content: flex-end;
        }

        div[draggable="true"] {
            cursor: default;
        }

        div[draggable="true"]:hover {
            opacity: 0.8;
        }
    </style>
</head>
<body>
<header>
    <div>
        <h2>Мои задачи</h2>
        <a href="${pageContext.request.contextPath}/api/labels">Лейблы</a>
    </div>
    <div class="filter-container">
        <div class="filter-dropdown">
            <button class="filter-button">Фильтр по исполнителю</button>
            <div class="filter-dropdown-content">
                <c:forEach var="user" items="${users}">
                    <div class="filter-option" data-user-id="${user.id}">${user.username}</div>
                </c:forEach>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/api/tasks" class="filter-reset-button">Сбросить</a>
    </div>
</header>

<div class="board">
    <div class="column open" id="open" ondrop="drop(event, 'open')" ondragover="allowDrop(event)">
    <c:set var = "counterOpen" value="${open.size()}"/>
        <div class="column-title">Open</div>
        <span class="task-counter" id="open-counter">${counterOpen}</span>
        <a href="${pageContext.request.contextPath}/api/task" class="add-task-icon" title="Добавить задачу">➕</a>
        <c:forEach var="task" items="${open}">
            <div class="task" id="${task.id}" draggable="true" ondragstart="drag(event)" data-status="open">
                <div style="position: relative;">
                    <div class="task-buttons">
                        <button class="delete-button" onclick="deleteTask(${task.id})" title="Удалить задачу">🗑️</button>
                        <a href="${pageContext.request.contextPath}/api/task/${task.id}" class="edit-button-icon" title="Редактировать задачу">⋮</a>
                    </div>
                    <div class="assigned">${task.assignee.username}</div>
                    <h4 class="task-title">${task.title}</h4>
                    <div class="labels">
                        <c:forEach var="label" items="${task.labels}">
                            <span class="label" style="background-color: ${label.color}">${label.label}</span>
                        </c:forEach>
                    </div>
                    <c:if test="${not empty task.description}">
                        <div class="description">${task.description}</div>
                    </c:if>
                        <div class="comment-container">
                            <c:if test="${not empty task.comments}">
                            <span class="comment-count">${fn:length(task.comments)}</span>
                            </c:if>
                            <a href="${pageContext.request.contextPath}/api/comments/${task.id}" class="comment-icon" title="Комментарии">💬</a>
                        </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="column in-progress" id="in-progress" ondrop="drop(event, 'in-progress')" ondragover="allowDrop(event)">
    <c:set var = "counterInProgress" value="${in_progress.size()}"/>
        <div class="column-title">In Progress</div>
        <span class="task-counter" id="in-progress-counter">${counterInProgress}</span>
        <c:forEach var="task" items="${in_progress}">
                <div class="task" id="${task.id}" draggable="true" ondragstart="drag(event)" data-status="in-progress">
                    <div style="position: relative;">
                        <div class="task-buttons">
                            <button class="delete-button" onclick="deleteTask(${task.id})" title="Удалить задачу">🗑️</button>
                            <a href="${pageContext.request.contextPath}/api/task/${task.id}" class="edit-button-icon" title="Редактировать задачу">⋮</a>
                        </div>
                        <div class="assigned">${task.assignee.username}</div>
                        <h4 class="task-title">${task.title}</h4>
                        <div class="labels">
                            <c:forEach var="label" items="${task.labels}">
                                <span class="label" style="background-color: ${label.color}">${label.label}</span>
                            </c:forEach>
                        </div>
                        <c:if test="${not empty task.description}">
                            <div class="description">${task.description}</div>
                        </c:if>
                            <div class="comment-container">
                                <c:if test="${not empty task.comments}">
                                    <span class="comment-count">${fn:length(task.comments)}</span>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/api/comments/${task.id}" class="comment-icon" title="Комментарии">💬</a>
                            </div>
                    </div>
                </div>
        </c:forEach>
    </div>
    <div class="column done" id="done" ondrop="drop(event, 'done')" ondragover="allowDrop(event)">
        <c:set var = "counterDone" value="${done.size()}"/>
        <div class="column-title">Done</div>
        <span class="task-counter" id="done-counter">${counterDone}</span>
        <c:forEach var="task" items="${done}">
            <div class="task" id="${task.id}" draggable="true" ondragstart="drag(event)" data-status="done">
                <div style="position: relative;">
                    <div class="task-buttons">
                        <button class="delete-button" onclick="deleteTask(${task.id})" title="Удалить задачу">🗑️</button>
                        <a href="${pageContext.request.contextPath}/api/task/${task.id}" class="edit-button-icon" title="Редактировать задачу">⋮</a>
                    </div>
                    <div class="assigned">${task.assignee.username}</div>
                    <h4 class="task-title">${task.title}</h4>
                    <div class="labels">
                        <c:forEach var="label" items="${task.labels}">
                            <span class="label" style="background-color: ${label.color}">${label.label}</span>
                        </c:forEach>
                    </div>
                    <c:if test="${not empty task.description}">
                        <div class="description">${task.description}</div>
                    </c:if>
                        <div class="comment-container">
                            <c:if test="${not empty task.comments}">
                                <span class="comment-count">${fn:length(task.comments)}</span>
                            </c:if>
                            <a href="${pageContext.request.contextPath}/api/comments/${task.id}" class="comment-icon" title="Комментарии">💬</a>
                        </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<script title="Перемещение и редактирование">
    function drag(event) {
        console.log("Dragging task:", event.target.id);
        event.dataTransfer.setData("text", event.target.id);
    }

    function allowDrop(event) {
        console.log("Allowing drop");
        event.preventDefault();
    }

    function drop(event, newStatus) {
        event.preventDefault();
        const taskId = event.dataTransfer.getData("text");
        const taskElement = document.getElementById(taskId);
        const oldStatus = taskElement.getAttribute('data-status');

        if (oldStatus !== newStatus) {
            taskElement.remove();
            document.getElementById(newStatus).appendChild(taskElement);
            taskElement.setAttribute('data-status', newStatus);

            updateTaskStatus(taskId, newStatus, oldStatus);
        }
    }

    function updateTaskStatus(taskId, newStatus, oldStatus) {
        const url = `task/` + taskId + `/status/` + newStatus;

        fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Ошибка при обновлении статуса задачи');
                }
                updateTaskCounter(oldStatus);
                updateTaskCounter(newStatus);
            })
            .catch(error => console.error('Ошибка:', error));
    }
</script>

<script title="Удаление и счетчик">
    function updateTaskCounter(status) {
        const url = `tasks/`+status;
        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Ошибка при получении задач');
                }
                return response.json();
            })
            .then(data => {
                const taskCount = data.length;
                const counterSpan = document.getElementById(status + '-counter');
                counterSpan.innerText = taskCount;
            })
            .catch(error => console.error('Ошибка: ', error));
    }

    function deleteTask(taskId) {
        const taskElement = document.getElementById(taskId);
        const status = taskElement.getAttribute('data-status');
        const url = `task/`+taskId;

        fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (response.status === 204) {
                    taskElement.remove();
                    updateTaskCounter(status);
                } else {
                    console.error('Ответ: ', response)
                    console.error('Ошибка при удалении задачи:', response.ok);
                }
            })
            .catch(error => console.error('Ошибка: ', error));
    }
</script>

<script title="Фильтрация">
    const filterOptions = document.querySelectorAll('.filter-option');

    filterOptions.forEach(option => {
        option.addEventListener('click', () => {
            const selectedUserId = option.dataset.userId;
            window.location.href = `${pageContext.request.contextPath}/api/tasks?assigneeId=`+selectedUserId;
        });
    });

    const filterButton = document.querySelector('.filter-button');
    filterButton.addEventListener('click', () => {
        const filterDropdown = document.querySelector('.filter-dropdown-content');
        filterDropdown.style.display = filterDropdown.style.display === 'block' ? 'none' : 'block';
    });
</script>

</body>
</html>
