<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>出力 - 検索</title>
  <style>
    .form-row { margin-bottom: 8px; }
    .flex-row { display: flex; align-items: center; margin-bottom: 8px; }
    .flex-row label { margin-right: 8px; }
  </style>
</head>
<body>
  <h2>出力画面</h2>
  <form method="get" action="${pageContext.request.contextPath}/output">
    <!-- 社員ID & 検索 -->
    <div class="form-row">
      <label>
        社員ID：
        <input type="text" name="employeeId"
               value="${fn:escapeXml(employeeId)}" required />
      </label>
      <button type="submit" name="action" value="search">検索</button>
    </div>

    <!-- エラーメッセージ -->
    <c:if test="${not empty errorMessage}">
      <p style="color:red;">${errorMessage}</p>
    </c:if>

    <!-- HOME ボタンは常に表示 -->
    <div class="form-row">
      <button type="button"
              onclick="location.href='${pageContext.request.contextPath}/adminMenu'">
        HOME
      </button>
    </div>

    <!-- 検索結果表示 -->
    <c:if test="${not empty employeeName}">
      <p>
        ID: <strong><fmt:formatNumber value="${employeeId}" pattern="0000"/></strong>
        名前: <strong>${fn:escapeXml(employeeName)}</strong>
      </p>
      <div class="flex-row">
        <label>
          月 (YYYY-MM)：
          <input type="month" name="month"
                 value="${fn:escapeXml(month)}" required />
        </label>
      </div>
      <div class="form-row">
        <label>
          出力フォーマット：
          <select name="format">
            <option value="PDF">PDF</option>
            <option value="CSV">CSV</option>
          </select>
        </label>
      </div>
      <div class="form-row">
        <button type="submit" name="action" value="confirm">出力確認</button>
      </div>
    </c:if>
  </form>
</body>
</html>
