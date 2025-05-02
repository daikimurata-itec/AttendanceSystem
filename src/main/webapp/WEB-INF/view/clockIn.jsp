<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>打刻画面</title>
    <style>
        .clock {
            font-size: 1.5em;
            font-weight: bold;
        }
        .button {
            padding: 8px 16px;
            margin: 4px;
            font-size: 1em;
        }
        /* 新たに追加：従業員IDと名前の横にボタンを並べるためのスタイル */
        .employee-info {
            display: flex;
            align-items: center;
        }
        .employee-info p {
            margin-right: 10px; /* ID と 名前の間に少しスペース */
        }
        .back-button {
            margin-left: 10px;
        }
    </style>
    <script>
        function updateClock() {
            const el = document.getElementById("now");
            const now = new Date();
            const hh = String(now.getHours()).padStart(2, '0');
            const mm = String(now.getMinutes()).padStart(2, '0');
            const ss = String(now.getSeconds()).padStart(2, '0');
            el.textContent = hh + ":" + mm + ":" + ss;
        }
        window.onload = function() {
            updateClock();
            setInterval(updateClock, 1000);
        };
        function goBackToLogin() {
            location.href = "${pageContext.request.contextPath}/login";
        }
    </script>
</head>
<body>
    <h2>打刻画面</h2>

    <!-- 従業員IDと名前を横並びに配置 -->
    <div class="employee-info">
        <p>従業員ID：${employeeId}</p>
        <p>名前：${employeeName}</p>

        <!-- 戻るボタン -->
        <button type="button" class="button back-button" onclick="goBackToLogin()">戻る</button>
    </div>

    <p>現在時刻：<span id="now" class="clock">${currentTime}</span></p>

    <form method="post" action="${pageContext.request.contextPath}/clockIn">
        <button type="submit" name="action" value="出勤" class="button">出勤</button>
        <button type="submit" name="action" value="退勤" class="button">退勤</button>
        <button type="submit" name="action" value="外出" class="button">外出</button>
        <button type="submit" name="action" value="再入" class="button">再入</button>
    </form>

    <c:if test="${not empty actionTime}">
        <p><strong>打刻結果：</strong>${actionTime}</p>
    </c:if>
</body>
</html>
