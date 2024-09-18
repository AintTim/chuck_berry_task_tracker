<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Комментарии задачи</title>
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

        .form-group input {
            width: 100%;
            padding: 10px;
            font-size: 1em;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .form-group input:focus {
            border-color: #007BFF;
            outline: none;
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

        .comment-block {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }

        .comment-block .comment-input {
            flex-grow: 1;
            margin-right: 10px;
        }

        .comment-block .delete-comment-btn,
        .comment-block .add-comment-btn {
            background-color: #dc3545;
            color: #fff;
            border: none;
            padding: 5px 10px;
            border-radius: 5px;
            cursor: pointer;
        }

        .comment-block .add-comment-btn {
            background-color: #28a745;
        }

    </style>
</head>
<body>
<div class="form-container">
    <div class="task-form" id="taskForm">
        <input type="hidden" id="taskId" name="taskId" value="${task.id}">
        <input type="hidden" id="taskStatus" name="taskStatus" value="${task.status}">

        <div class="form-group">
            <label for="taskTitle">Название задачи</label>
            <div id="taskTitle" class="form-control readonly">${task.title}</div>
        </div>
        <c:if test="${not empty task.description}">
        <div class="form-group">
            <label for="taskDesc">Описание</label>
            <div id="taskDesc" class="form-control readonly">${task.description}</div>
        </div>
        </c:if>
        <div class="form-group">
            <label for="taskAssignee">Исполнитель</label>
            <div id="taskAssignee" class="form-control readonly">${task.assignee.username}</div>
        </div>
        <div class="form-group">
            <label for="comments">Комментарии</label>
            <div id="comments">
                <c:forEach var="comment" items="${task.comments}">
                    <div class="comment-block">
                        <input type="text" class="comment-input" value="${comment.comment}">
                        <button class="delete-comment-btn">Удалить</button>
                    </div>
                </c:forEach>
                <div class="comment-block">
                    <input type="text" class="comment-input" placeholder="Добавить комментарий">
                    <button class="add-comment-btn">Добавить</button>
                </div>
            </div>
        </div>

        <div class="button-container">
            <button type="submit" id="saveTaskButton" onclick="updateComments()">Сохранить задачу</button>
            <a href="${pageContext.request.contextPath}/api/tasks" class="button-like-link">Вернуться</a>
        </div>
    </div>
</div>

<script title="Запрос на обновление комментария">
    function updateComments() {
        const taskId = document.getElementById('taskId').value;
        const commentInputs = document.querySelectorAll('.comment-block .comment-input');
        const commentData = {
            id: taskId,
            comments: []
        };

        commentInputs.forEach(function(input) {
            const commentText = input.value.trim();
            if (commentText !== '') {
                commentData.comments.push(commentText);
            }
        });

        sendCommentDataToServer(commentData);
    }

    function sendCommentDataToServer(commentData) {
        const url = `${pageContext.request.contextPath}` + `/api/comments`
        fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(commentData)
        })
            .then(response => {
                if (response.ok) {
                    const button = document.querySelector('.button-container a');
                    if (button) {
                        button.click();
                    }
                } else {
                    console.error("Response: ", response)
                }
            })
            .catch(error => console.error('Ошибка:', error));
    }
</script>
<script title="Создание/удаление комментария">
    document.addEventListener('DOMContentLoaded', function() {
        const commentInputs = document.querySelectorAll('.comment-block .comment-input');
        const addCommentButtons = document.querySelectorAll('.comment-block .add-comment-btn');
        const deleteCommentButtons = document.querySelectorAll('.comment-block .delete-comment-btn');

        addCommentButtons.forEach(function(button) {
            button.addEventListener('click', function(event) {
                event.preventDefault();
                const commentInput = this.previousElementSibling;
                const commentText = commentInput.value.trim();
                if (commentText !== '') {
                    createCommentBlock(commentText, document.getElementById('comments'));
                    commentInput.value = '';
                }
            });
        });

        deleteCommentButtons.forEach(function(button) {
            button.addEventListener('click', function() {
                const commentBlock = this.parentElement;
                commentBlock.parentElement.removeChild(commentBlock);
            });
        });
    });

    function createCommentBlock(commentText, parent) {
        const commentBlock = document.createElement('div');
        commentBlock.classList.add('comment-block');

        const commentInput = document.createElement('input');
        commentInput.type = 'text';
        commentInput.classList.add('comment-input');
        commentInput.value = commentText;

        const deleteButton = document.createElement('button');
        deleteButton.classList.add('delete-comment-btn');
        deleteButton.textContent = 'Удалить';
        deleteButton.addEventListener('click', function() {
            commentBlock.parentElement.removeChild(commentBlock);
        });

        commentBlock.appendChild(commentInput);
        commentBlock.appendChild(deleteButton);

        parent.insertBefore(commentBlock, parent.lastElementChild);
    }
</script>

</body>
</html>

