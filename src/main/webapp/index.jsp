<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Авторизация</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f0f0f0;
        }

        .auth-form {
            margin: 0;
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
            color: #d9534f;
            background-color: #f2dede;
            border: 1px solid #ebccd1;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            width: 100%;
            text-align: center;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            font-size: 1.1em;
            margin-bottom: 8px;
            display: block;
        }

        .form-group input {
            width: 100%;
            padding: 10px;
            font-size: 1em;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .form-group input:focus:focus {
            border-color: #007BFF;
            outline: none;
        }

        .button-container {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }

        #loginButton {
            padding: 10px 15px;
            font-size: 1em;
            color: white;
            background-color: #28a745;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
        }

        #loginButton:hover {
            background-color: #218838;
        }

        #registerButton {
            display: inline-block;
            padding: 10px 15px;
            color: white;
            background-color: #007BFF;
            text-decoration: none;
            border-radius: 5px;
        }

        #registerButton:hover {
            background-color: #0056b3;
        }
    </style>
    <script>
        function submitForm(action) {
            var form = document.getElementById('authForm');
            form.method = (action === 'create') ? 'post' : 'get';
            form.submit();
        }
    </script>
</head>
<body>
<div class="form-container">
    <% if (request.getAttribute("error") != null) { %>
    <div class="error-message" id="errorMessage">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>
    <form class="auth-form" id="authForm" action="${pageContext.request.contextPath}/api/user" method="get">
        <div class="form-group">
            <label for="username">Логин</label>
            <input type="text" placeholder="Введите логин" id="username" name="username" value="${user}" required>
        </div>
        <div class="form-group">
            <label for="password">Пароль</label>
            <input type="password" placeholder="Введите пароль" id="password" name="password" required>
        </div>

        <div class="button-container">
            <input id="loginButton" type="button" value="Войти" onclick="submitForm('validate')">
            <input id="registerButton" type="button" value="Зарегистрироваться" onclick="submitForm('create')">
        </div>
    </form>
</div>

</body>
</html>