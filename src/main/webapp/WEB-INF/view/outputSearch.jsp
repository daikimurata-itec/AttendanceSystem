<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"      %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>出力 - 検索</title></head>
<body>
  <h2>出力画面</h2>
  <form method="get" action="${pageContext.request.contextPath}/output">
    <!-- 1) 社員ID & 検索 -->
    <div>
      <label>社員ID：
        <input type="text" name="employeeId"
               value="${fn:escapeXml(employeeId)}" required />
      </label>
      <button type="submit" name="action" value="search">検索</button>
    </div>

    <c:if test="${not empty errorMessage}">
      <p style="color:red">${errorMessage}</p>
    </c:if>

    <!-- 2) 検索後に表示 -->
    <c:if test="${not empty employeeName}">
      <p>ID：<strong>${fn:escapeXml(employeeId)}</strong>
         名前：<strong>${fn:escapeXml(employeeName)}</strong></p>

      <div>
        <label>月 (YYYY-MM)：
          <input type="month" name="month"
                 value="${fn:escapeXml(month)}" required />
        </label>
      </div>
      <div>
        <label>出力フォーマット：
          <select name="format">
            <option value="PDF">PDF</option>
            <option value="CSV">CSV</option>
          </select>
        </label>
      </div>
      <div>
        <!-- 確認画面へ -->
        <button type="submit" name="action" value="confirm">出力確認</button>
        <button type="button"
                onclick="location.href='${pageContext.request.contextPath}/adminMenu'">
          HOME
        </button>
      </div>
    </c:if>
  </form>
</body>
</html>
