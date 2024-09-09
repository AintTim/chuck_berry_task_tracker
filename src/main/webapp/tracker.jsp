<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Мои задачи</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 20px;
        }
        h2 {
            font-size: 24px;
            margin: 0;
        }
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
        }
        .task {
            background-color: #f9f9f9;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 6px;
            margin-bottom: 10px;
            position: relative;
        }
        .edit-button {
            background: none; /* Убираем фон */
            border: none; /* Убираем границу */
            cursor: pointer; /* Курсор при наведении */
            color: #888; /* Цвет иконки */
            font-size: 16px; /* Размер шрифта */
            padding: 0; /* Убираем внутренние отступы */
            margin: 0; /* Убираем внешние отступы */
        }
        .delete-button {
            color: red; /* Цвет иконки удаления */
            background: none; /* Убираем фон */
            border: none; /* Убираем границу */
            cursor: pointer; /* Курсор при наведении */
            padding: 0; /* Убираем внутренние отступы */
            margin: 0; /* Убираем внешние отступы */
        }
        .assigned {
            font-size: 14px;
            font-weight: bold;
            margin-bottom: 5px;
            color: #555;
        }
        .labels {
            display: flex;
            gap: 5px;
            margin-bottom: 10px;
        }
        .label {
            font-size: 12px;
            padding: 4px 8px;
            border-radius: 4px;
            color: white;
            background-color: rgba(255, 165, 0, 0.7);
        }
        .description {
            margin-top: 10px;
            padding: 10px; /* Увеличиваем отступ внутри */
            background-color: #f7f7f7; /* Светлый серый фон */
            border-radius: 5px; /* Закругляем углы */
            border: 1px solid #ddd; /* Тонкая граница */
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Тень для объема */
            font-style: italic; /* Курсив для описания */
            color: #555; /* Для контраста */
        }
        .comments {
            margin-top: 10px;
            padding: 10px; /* Увеличиваем отступ внутри */
            background-color: #e6f7ff; /* Светлый голубой фон */
            border-radius: 5px; /* Закругляем углы */
            border: 1px solid #b3e0ff; /* Легкая граница */
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Тень для объема */
        }
        .comment {
            padding: 5px; /* Отступы для комментариев */
            border-bottom: 1px solid #b3d1ff; /* Легкая линия разделения */
            color: #333; /* Цвет текста */
        }
        .comment-text {
            margin-left: 10px; /* Отступ для текста комментария */
        }
        .comment:last-child {
            border-bottom: none; /* Убираем границу у последнего комментария */
        }
        .task-form {
            display: none; /* Initially hidden */
            margin-bottom: 20px;
            background-color: white;
            padding: 10px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .task-form input {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .add-task-button {
            padding: 10px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        .add-task-button:hover {
            background-color: #218838;
        }
        .task-buttons {
            position: absolute;
            top: 0;
            right: 0;
            display: flex;
            align-items: baseline; /* Выравнивание по вертикали */
            gap: 15px; /* Уменьшенный отступ между кнопками */
            padding: 5px; /* Внутренние отступы */
            background-color: #f9f9f9; /* Цвет фона */
            border-radius: 5px; /* Закругление углов */
            min-width: 70px;
            justify-content: flex-end;
        }
    </style>
</head>
<body>
<header>
    <h2>Мои задачи</h2>
</header>

<div class="task-form" id="taskForm">
    <input type="text" id="taskTitle" placeholder="Название задачи" required>
    <input type="text" id="taskAssigned" placeholder="Кому назначена" required>
    <button class="add-task-button" onclick="addTask()">Добавить задачу</button>
    <button class="add-task-button" onclick="toggleTaskForm()">Отменить</button>
</div>

<div class="board">
    <div class="column open" id="open">
    <c:set var = "counterOpen" value="${open.size()}"/>
        <div class="column-title">Open</div>
        <span class="task-counter" id="open-counter">${counterOpen}</span>
        <span class="add-task-icon" onclick="toggleTaskForm()">➕</span>
        <c:forEach var="task" items="${open}">
            <div class="task" id="task${task.id}">
                <div style="position: relative;">
                    <div class="task-buttons">
                        <button class="delete-button" onclick="deleteTask(${task.id})">🗑️</button>
                        <button class="edit-button" onclick="editTask(this)">⋮</button>
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
                    <c:if test="${not empty task.comments}">
                    <div class="comments">
                        <c:forEach var="comment" items="${task.comments}">
                            <div class="comment">
                                <span class="comment-text">${comment.comment}</span>
                            </div>
                        </c:forEach>
                    </div>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="column in-progress" id="in-progress">
    <c:set var = "counterInProgress" value="${in_progress.size()}"/>
        <div class="column-title">In Progress</div>
        <span class="task-counter" id="in-progress-counter">${counterInProgress}</span>
        <c:forEach var="task" items="${in_progress}">
                <div class="task" id="task${task.id}">
                    <div style="position: relative;">
                        <div class="task-buttons">
                            <button class="delete-button" onclick="deleteTask(${task.id})">🗑️</button>
                            <button class="edit-button" onclick="editTask(this)">⋮</button>
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
                        <c:if test="${not empty task.comments}">
                            <div class="comments">
                                <c:forEach var="comment" items="${task.comments}">
                                    <div class="comment">
                                        <span class="comment-text">${comment.comment}</span>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>
                </div>
        </c:forEach>
    </div>
    <div class="column done" id="done">
        <c:set var = "counterDone" value="${done.size()}"/>
        <div class="column-title">Done</div>
        <span class="task-counter" id="done-counter">${counterDone}</span>
        <c:forEach var="task" items="${done}">
                <div class="task" id="task${task.id}">
                    <div style="position: relative;">
                        <div class="task-buttons">
                            <button class="delete-button" onclick="deleteTask(${task.id})">🗑️</button>
                            <button class="edit-button" onclick="editTask(this)">⋮</button>
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
                        <c:if test="${not empty task.comments}">
                            <div class="comments">
                                <c:forEach var="comment" items="${task.comments}">
                                    <div class="comment">
                                        <span class="comment-text">${comment.comment}</span>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>
                </div>
        </c:forEach>
    </div>
</div>

<script>
    function toggleTaskForm() {
        const form = document.getElementById('taskForm');
        form.style.display = form.style.display === 'none' ? 'block' : 'none';
        // Установка фокуса на первое поле ввода при открытии формы
        if (form.style.display === 'block') {
            document.getElementById('taskTitle').focus();
        }
    }

    function editTask(task) {}

    function addTask() {
        const title = document.getElementById('taskTitle').value.trim();
        const assigned = document.getElementById('taskAssigned').value.trim();

        if (title === "" || assigned === "") {
            alert("Пожалуйста, заполните все поля!");
            return;
        }

        const taskColumn = document.getElementById('open');
        const taskDiv = document.createElement('div');
        taskDiv.className = 'task';
        taskDiv.innerHTML = `
            <div style="position: relative;">
                <div class="task-buttons">
                        <button class="delete-button" onclick="deleteTask(${task.id})">🗑️</button>
                        <button class="edit-button" onclick="editTask(this)">⋮</button>
                </div>
                <div class="assigned">Назначено: ${assigned}</div>
                <p>${title}</p>
                <div class="labels">
                    <span class="label">Домашние дела</span>
                </div>
                <div class="description">Описание задачи.</div>
                <div class="comments">
                    <p>Пока нет комментариев.</p>
                </div>
            </div>
        `;

        taskColumn.appendChild(taskDiv);
        updateTaskCounter('open');
        document.getElementById('taskTitle').value = '';
        document.getElementById('taskAssigned').value = '';
        toggleTaskForm();
    }

    function updateTaskCounter(columnId) {
        const column = document.getElementById(columnId);
        const tasks = column.getElementsByClassName('task');
        const counterSpan = document.getElementById(columnId + '-counter');
        counterSpan.innerText = tasks.length;
    }

    function deleteTask(taskId) {
        console.log("Удаляем по URL:", `api/task/`+taskId);
        const url = `task/`+taskId;
        console.log("URL запроса:", url);
        fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (response.ok) {
                    // Задача была удалена, обновите страницу или удалите элемент задачи
                    document.getElementById(`task`+taskId).remove();
                } else {
                    console.error('Ответ: ', response)
                    console.error('Ошибка при удалении задачи:', response.ok);
                }
            })
            .catch(error => console.error('Ошибка: ', error));
    }
</script>

</body>
</html>
