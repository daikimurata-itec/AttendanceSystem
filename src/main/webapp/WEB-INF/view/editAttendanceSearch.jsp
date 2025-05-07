<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
  <title>打刻編集 - 検索</title>
</head>
<body>
  <h2>打刻編集 - 検索画面</h2>

  <form method="get" action="${pageContext.request.contextPath}/editAttendanceSearch">
    <!-- 社員ID入力行 -->
    <div style="margin-bottom: 8px;">
      <label>
        社員ID:
        <input type="text" name="employeeId"
               value="${fn:escapeXml(employeeId)}"
               required />
      </label>
    </div>
    <!-- 月選択と検索ボタン行 -->
    <div style="display: flex; align-items: center; margin-bottom: 8px;">
      <label style="margin-right: 8px;">
        月 (YYYY-MM):
        <input type="month" name="month"
               value="${fn:escapeXml(month)}"
               required />
      </label>
      <button type="submit" name="action" value="search">検索</button>
    </div>
    <!-- 編集と戻るボタン行 -->
    <div>
      <button type="button"
        onclick="location.href='${pageContext.request.contextPath}/adminMenu'">
        HOME
      </button>
      <button type="submit" name="action" value="edit">打刻編集</button>
    </div>
  </form>

  <c:if test="${not empty errorMessage}">
    <p style="color:red;">${errorMessage}</p>
  </c:if>

  <c:if test="${not empty employeeName}">
    <p>
      ID: <%= String.format("%04d", Integer.parseInt((String) request.getAttribute("employeeId"))) %>
      名前: <strong>${fn:escapeXml(employeeName)}</strong>
    </p>
  </c:if>
</body>
</html>