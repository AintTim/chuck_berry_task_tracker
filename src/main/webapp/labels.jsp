<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Выбор лейбла</title>
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
            text-align: center;
        }

        .label-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 10px;
            margin-bottom: 20px;
        }

        .label-block {
            padding: 10px 15px;
            border-radius: 5px;
            cursor: pointer;
            transition: all 0.3s;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            background-color: #f5f5f5;
            border: 2px solid transparent;
            font-size: smaller;
        }

        .label-block.selected {
            box-shadow: 0 2px 10px rgba(178, 0, 255, 0.5);
            border-color: #b200ff;
            font-size: large;
        }

        .label-input-container {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
            justify-content: center;
        }

        .label-input {
            flex-grow: 1;
            padding: 10px;
            font-size: 1em;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-right: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .label-input.read-only {
            background-color: #b1c8df;
            border: none;
            cursor: default;
            width: 180px;
        }

        .color-picker {
            padding: 10px;
            font-size: 1em;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .button-container {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }

        .save-button {
            padding: 10px 15px;
            font-size: 1em;
            color: white;
            background-color: #28a745;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .save-button:hover {
            background-color: #218838;
        }

        .back-link {
            display: inline-block;
            padding: 10px 15px;
            color: white;
            background-color: #007BFF;
            text-decoration: none;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .back-link:hover {
            background-color: #0056b3;
        }

        .color-picker {
            padding: 1px;
            font-size: 1em;
            border: 3px solid #999999;
            border-radius: 50%;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            width: 40px;
            height: 40px;
            cursor: pointer;
            transition: border-color 0.3s;
            appearance: none;
            -webkit-appearance: none;
            -moz-appearance: none;
        }

        .color-picker::-webkit-color-swatch-wrapper {
            padding: 0;
        }

        .color-picker::-webkit-color-swatch {
            border: none;
            border-radius: 50%;
        }

        .color-picker:hover {
            border-color: #b200ff;
        }

        .label-block {
            display: flex;
            font-size: larger;
            justify-content: space-between;
            align-items: center;
            border: 2px solid;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 5px;
            cursor: pointer;
        }

        .delete-button {
            background-color: transparent;
            margin-left: 10px;
            border: none;
            color: #888;
            font-size: 25px;
            cursor: pointer;
        }

        .delete-button:hover {
            color: #555;
        }

    </style>
</head>
<body>
<div class="form-container">
    <div class="error-message" id="errorMessage"></div>
    <div class="label-container" id="labelContainer">
        <c:forEach var="label" items="${labels}">
            <div class="label-block" style="border-color: ${label.color}" data-id="${label.id}">
                <span>${label.label}</span>
                <button class="delete-button">×</button>
            </div>
        </c:forEach>
    </div>
    <div class="label-input-container">
        <input type="text" class="label-input" id="labelInput" placeholder="Введите название лейбла">
        <input type="color" class="color-picker" id="colorPicker">
    </div>
    <div class="button-container">
        <button class="save-button" onclick="saveLabel()">Сохранить</button>
        <a href="${pageContext.request.contextPath}/api/tasks" class="back-link">Вернуться</a>
    </div>
</div>

<script>
    let selectedLabel = null;

    function createLabelBlock(text, color, container) {
        const labelBlock = document.createElement('div');
        labelBlock.classList.add('label-block');
        labelBlock.style.borderColor = color;

        const labelText = document.createElement('span');
        labelText.textContent = text;
        labelBlock.appendChild(labelText);

        const deleteButton = document.createElement('button');
        deleteButton.classList.add('delete-button');
        deleteButton.innerHTML = '&times;';
        deleteButton.addEventListener('click', (event) => {
            event.stopPropagation();
            deleteLabel(labelBlock);
        });
        labelBlock.appendChild(deleteButton);

        labelBlock.addEventListener('click', () => selectLabel(labelBlock));
        container.appendChild(labelBlock);

        return labelBlock;
    }


    function deleteLabel(labelBlock) {
        const labelId = labelBlock.dataset.id;
        const url = `${pageContext.request.contextPath}` + `/api/labels/`+labelId
        console.log('Deleting label with id '+labelId)
        fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
                if (response.status === 204) {
                    if (selectedLabel === labelBlock) {
                        selectedLabel = null;
                    }
                    labelBlock.parentNode.removeChild(labelBlock);
                    const errorMessage = document.getElementById('errorMessage');
                    errorMessage.style.display = 'none';
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


    function selectLabel(labelBlock) {
        if (labelBlock === selectedLabel) {
            labelBlock.classList.remove('selected');
            selectedLabel = null;
            document.getElementById('labelInput').classList.remove("read-only");
            document.getElementById('labelInput').readOnly = false;
            return;
        }

        if (selectedLabel) {
            selectedLabel.classList.remove('selected');
        }

        labelBlock.classList.add('selected');
        selectedLabel = labelBlock;

        document.getElementById('labelInput').value = labelBlock.querySelector('span').textContent;
        document.getElementById('colorPicker').value = rgbToHex(labelBlock.style.borderColor);

        document.getElementById('labelInput').classList.add("read-only");
        document.getElementById('labelInput').readOnly = true;
    }


    function saveLabel() {
        const errorMessage = document.getElementById('errorMessage');
        const labelInput = document.getElementById('labelInput');
        const colorPicker = document.getElementById('colorPicker');
        const labelText = labelInput.value;
        const url = `${pageContext.request.contextPath}` + `/api/labels`
        let method = selectedLabel ? 'PUT' : 'POST';

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ label: labelText, color: colorPicker.value })
        })
        .then(response => {
                if (response.ok) {
                    errorMessage.style.display = 'none';
                    if (selectedLabel) {
                        const labelBlock = selectedLabel;
                        const labelSpan = labelBlock.querySelector('span');
                        labelSpan.textContent = labelText;
                        labelBlock.style.borderColor = colorPicker.value;
                    } else {
                        createLabelBlock(labelText, colorPicker.value, document.getElementById('labelContainer'));
                    }

                    labelInput.value = '';
                    colorPicker.value = '#000000';

                    if (selectedLabel) {
                        selectedLabel.classList.remove('selected');
                        document.getElementById('labelInput').classList.remove("read-only");
                        document.getElementById('labelInput').readOnly = false;
                    }
                    selectedLabel = null;
                } else {
                    return response.json();
                }
        })
        .then(data => {
                if (typeof data === 'string') {
                    errorMessage.textContent = data;
                    errorMessage.style.display = 'block';
                }
        })
        .catch(error =>  console.error('Ошибка:', error));
    }


    function rgbToHex(rgb) {
        const parts = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
        if (parts) {
            return '#' + parts.slice(1, 4).map(n => parseInt(n, 10).toString(16).padStart(2, '0')).join('');
        }
        return rgb;
    }
</script>
<script title="Обработчики событий">
    document.querySelectorAll('.delete-button').forEach(button => {
        button.addEventListener('click', (event) => {
            event.stopPropagation();
            const labelBlock = event.target.closest('.label-block');
            deleteLabel(labelBlock);
        });
    });

    document.querySelectorAll('.label-block').forEach(labelBlock => {
        labelBlock.addEventListener('click', () => selectLabel(labelBlock));
    });
</script>
</body>
</html>
