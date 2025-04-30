<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>打刻画面</title>
</head>
<body>
    <h2>打刻画面</h2>
    <p>現在時刻：<strong>${currentTime}</strong></p>

    <form method="post" action="${pageContext.request.contextPath}/clockIn">
        <button type="submit" name="action" value="出勤">出勤</button>
        <button type="submit" name="action" value="退勤">退勤</button>
        <button type="submit" name="action" value="外出">外出</button>
        <button type="submit" name="action" value="再入">再入</button>
    </form>

    <c:if test="${not empty actionTime}">
        <p><strong>打刻結果：</strong>${actionTime}</p>
    </c:if>
</body>
</html>