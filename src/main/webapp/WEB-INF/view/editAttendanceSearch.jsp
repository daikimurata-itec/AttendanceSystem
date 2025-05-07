<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>打刻編集 - 検索</title>
  <style>
    .form-row { margin-bottom: 8px; }
    .flex-row { display: flex; align-items: center; margin-bottom: 8px; }
    .flex-row label { margin-right: 8px; }
  </style>
</head>
<body>
  <h2>打刻編集 - 検索画面</h2>

  <form method="get" action="${pageContext.request.contextPath}/editAttendanceSearch">
    <!-- 社員ID 入力行 -->
    <div class="form-row">
      <label>
        社員ID:
        <input type="text" name="employeeId"
               value="${fn:escapeXml(employeeId)}"
               required />
      </label>
    </div>

    <!-- 月選択と検索ボタン行 -->
    <div class="flex-row">
      <label>
        月 (YYYY-MM):
        <input type="month" name="month"
               value="${fn:escapeXml(month)}"
               required />
      </label>
      <button type="submit" name="action" value="search">検索</button>
    </div>

    <!-- HOME と 編集ボタン -->
    <div class="form-row">
      <button type="button"
              onclick="location.href='${pageContext.request.contextPath}/adminMenu'">
        HOME
      </button>
      <button type="submit" name="action" value="edit">打刻編集</button>
    </div>
  </form>

  <!-- エラーメッセージ -->
  <c:if test="${not empty errorMessage}">
    <p style="color:red;">${errorMessage}</p>
  </c:if>

  <!-- 検索結果表示 -->
  <c:if test="${not empty employeeName}">
    <p>
      ID: <strong><fmt:formatNumber value="${employeeId}" pattern="0000" /></strong>
      名前: <strong>${fn:escapeXml(employeeName)}</strong>
    </p>
  </c:if>
</body>
</html>
