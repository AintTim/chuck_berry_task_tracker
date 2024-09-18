<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактировать задачу</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f0f0f0;
        }

        .form-container {
            max-width: 900px;
            width: 100%;
            padding: 20px;
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        .error-message {
            display: none;
            color: #d9534f;
            background-color: #f2dede;
            border: 1px solid #ebccd1;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            width: 100%;
            text-align: center;
        }

        .task-form {
            max-width: 900px;
            padding: 40px;
            border: 1px solid #ccc;
            border-radius: 12px;
            background-color: #fefefe;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            margin: 0;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            font-size: 1.1em;
            margin-bottom: 8px;
            display: block;
        }

        .form-group .form-control.readonly {
            background-color: #b1c8df;
            width: 100%;
            padding: 10px;
            font-size: 1em;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .form-group input,
        #labelSelect,
        #userSelect {
            width: 100%;
            padding: 10px;
            font-size: 1em;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .form-group input:focus,
        #labelSelect:focus,
        #userSelect:focus {
            border-color: #007BFF;
            outline: none;
        }

        .labels {
            display: flex;
            flex-wrap: wrap;
            margin-top: 15px;
        }

        .label {
            display: inline-block;
            padding: 7px 12px;
            margin: 5px;
            color: #fff;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.9em;
        }

        .button-container {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }

        #saveTaskButton {
            padding: 10px 15px;
            font-size: 1em;
            color: white;
            background-color: #28a745;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
        }

        #saveTaskButton:hover {
            background-color: #218838;
        }

        .button-like-link {
            display: inline-block;
            padding: 10px 15px;
            color: white;
            background-color: #007BFF;
            text-decoration: none;
            border-radius: 5px;
        }

        .button-like-link:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="form-container">
    <div class="error-message" id="errorMessage"></div>
    <form class="task-form" id="taskForm" action="/api/task" method="post">
        <input type="hidden" id="taskId" name="taskId" value="${task.id}">
        <input type="hidden" id="taskStatus" name="taskStatus" value="${task.status}">

        <div class="form-group">
            <label for="taskTitle">Название задачи</label>
            <div id="taskTitle" class="form-control readonly">${task.title}</div>
        </div>
        <div class="form-group">
            <label for="taskDesc">Описание</label>
            <input type="text" placeholder="Введите описание задачи" id="taskDesc" name="description" value="${task.description}">
        </div>
        <div class="form-group">
            <label for="userSelect">Выберите исполнителя:</label>
            <select id="userSelect" name="username" required>
                <option value="">-- Выберите пользователя --</option>
                <c:forEach var="user" items="${users}">
                    <option value="${user.username}"
                            <c:if test="${task.assignee.username eq user.username}"> selected </c:if>
                    >${user.username}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label for="labelSelect">Выберите лейблы:</label>
            <select id="labelSelect">
                <option value="">-- Выберите лейбл --</option>
                <c:forEach var="label" items="${labels}">
                    <option value="${label.label}" style="background-color: ${label.color}">${label.label}</option>
                </c:forEach>
            </select>
        </div>

        <div class="labels" id="selectedLabels">
            <c:forEach var="label" items="${task.labels}">
                <div class="label" style="background-color: ${label.color}">${label.label}</div>
            </c:forEach>
        </div>

        <div class="button-container">
            <button type="submit" id="saveTaskButton">Сохранить задачу</button>
            <a href="${pageContext.request.contextPath}/api/tasks" class="button-like-link">Вернуться</a>
        </div>
    </form>
</div>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.getElementById('taskForm');
        form.addEventListener('submit', function(event) {
            event.preventDefault();

            const taskId = document.getElementById('taskId').value;
            const taskStatus = document.getElementById('taskStatus').value;
            const taskTitle = document.getElementById('taskTitle').textContent;
            const taskDesc = document.getElementById('taskDesc').value;
            const userSelect = document.getElementById('userSelect').value;

            const selectedLabels = [];
            const labelElements = document.querySelectorAll('.labels .label');
            labelElements.forEach(function(labelElement) {
                selectedLabels.push(labelElement.textContent.trim());
            });

            const data = {
                id: taskId,
                title: taskTitle,
                description: taskDesc,
                status: taskStatus,
                assignee: userSelect,
                labels: selectedLabels
            };

            sendDataToServer(data);
        });

        function sendDataToServer(data) {
            fetch(data.id, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (response.ok) {
                        const button = document.querySelector('.button-container a');
                        if (button) {
                            button.click();
                        }
                    }
                    else {
                        return response.json();
                    }
                })
                .then(data => {
                    if (typeof data === 'string') {
                        const errorMessage = document.getElementById('errorMessage');
                        errorMessage.textContent = data;
                        errorMessage.style.display = 'block';
                    }
                })
                .catch(error =>  console.error('Ошибка:', error));
        }
    });
</script>

<script title="Обновление лейблов">
    const selectedLabelsContainer = document.getElementById('selectedLabels');
    const labelSelect = document.getElementById('labelSelect');

    labelSelect.addEventListener('change', function() {
        const selectedIndex = this.selectedIndex;
        const selectedOption = this.options[selectedIndex];

        const selectedValue = selectedOption.value;
        const backgroundColor = selectedOption.style.backgroundColor;

        if (selectedValue && !Array.from(selectedLabelsContainer.children).some(label => label.dataset.value === selectedValue)) {
            const labelDiv = document.createElement('div');
            labelDiv.className = 'label';
            labelDiv.textContent = selectedValue;
            labelDiv.dataset.value = selectedValue;
            labelDiv.style.backgroundColor = backgroundColor;

            labelDiv.addEventListener('click', function() {
                selectedLabelsContainer.removeChild(labelDiv);
            });

            selectedLabelsContainer.appendChild(labelDiv);
        }
        this.selectedIndex = 0;
    });

    const existingLabels = selectedLabelsContainer.querySelectorAll('.label');
    existingLabels.forEach(label => {
        label.addEventListener('click', function() {
            selectedLabelsContainer.removeChild(label);
        });
    });
</script>

</body>
</html>

