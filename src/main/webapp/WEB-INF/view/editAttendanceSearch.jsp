<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head><title>打刻編集 - 検索</title></head>
<body>
<h2>打刻編集 - 検索画面</h2>
<form method="post" action="${pageContext.request.contextPath}/editAttendanceSearch">
    <label>社員ID: <input type="text" name="employeeId" required></label><br>
    <label>月 (YYYY-MM): <input type="month" name="month" required></label><br>
    <button type="submit">検索</button>
    <button type="button" onclick="location.href='${pageContext.request.contextPath}/adminMenu'">戻る</button>
</form>
</body>
</html>