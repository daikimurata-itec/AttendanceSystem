<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>打刻編集 結果</title>
</head>
<body>
  <h2>${message}</h2>

  <p>社員ID：<strong>${att.employeeId}</strong></p>
  <p>名前　：<strong>${employeeName}</strong></p>
  <hr/>

  <table border="1" cellpadding="4" cellspacing="0">
    <tr>
      <th>日付</th>
      <th>出勤時刻</th>
      <th>退勤時刻</th>
      <th>休憩時間</th>
    </tr>
    <tr>
      <td>
        <fmt:formatDate value="${att.workDate}" pattern="yyyy-MM-dd" />
      </td>
      <td>
        <c:choose>
          <c:when test="${not empty att.clockIn}">
            <fmt:formatDate value="${att.clockIn}" pattern="HH:mm" />
          </c:when>
          <c:otherwise>―</c:otherwise>
        </c:choose>
      </td>
      <td>
        <c:choose>
          <c:when test="${not empty att.clockOut}">
            <fmt:formatDate value="${att.clockOut}" pattern="HH:mm" />
          </c:when>
          <c:otherwise>―</c:otherwise>
        </c:choose>
      </td>
      <td>
        <c:choose>
          <c:when test="${not empty att.breakDuration}">
            ${att.breakDuration.toString().substring(0,5)}
          </c:when>
          <c:otherwise>―</c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>

  <form action="${pageContext.request.contextPath}/adminMenu" method="get">
    <button type="submit">HOME</button>
  </form>
</body>
</html>
