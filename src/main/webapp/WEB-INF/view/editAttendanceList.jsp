<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>打刻編集 - 一覧</title></head>
<body>
<h2>打刻編集 - 一覧表示</h2>
<p>社員ID: ${employeeId}</p>
<table border="1">
    <tr><th>日付</th><th>出勤</th><th>退勤</th><th>休憩時間</th><th>操作</th></tr>
    <c:forEach var="r" items="${records}">
        <tr>
            <td>${r.date}</td>
            <td>${r.clockIn}</td>
            <td>${r.clockOut}</td>
            <td>${r.breakDuration}</td>
            <td>
                <form method="get" action="${pageContext.request.contextPath}/editAttendanceForm">
                    <input type="hidden" name="recordId" value="${r.id}">
                    <button type="submit">編集</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<button type="button" onclick="location.href='${pageContext.request.contextPath}/editAttendanceSearch'">戻る</button>
</body>
</html>