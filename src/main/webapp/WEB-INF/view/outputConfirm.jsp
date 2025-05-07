<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>出力確認 - 打刻一覧</title>
  <style>
    table { border-collapse:collapse; width:100%; }
    th, td { border:1px solid #ccc; padding:4px; text-align:center; }
    th { background:#f0f0f0; }
  </style>
</head>
<body>
  <h2>出力確認</h2>
  <p>
    社員ID：<strong>${employeeId}</strong><br/>
    名前　：<strong>${employeeName}</strong><br/>
    月　　：<strong>${month}</strong><br/>
    フォーマット：<strong>${format}</strong>
  </p>

  <h3>打刻一覧</h3>
  <table>
    <tr>
      <th>日付</th>
      <th>出勤</th>
      <th>退勤</th>
      <th>外出</th>
      <th>再入</th>
      <th>休憩</th>
    </tr>
    <c:forEach var="day" items="${allDaysInMonth}">
      <!-- key = yyyy-MM-dd -->
      <fmt:formatDate var="key" value="${day}" pattern="yyyy-MM-dd"/>
      <c:set var="rec" value="${recordMap[key]}"/>
      <tr>
        <td><fmt:formatDate value="${day}" pattern="yyyy-MM-dd"/></td>
        <td>
          <c:choose>
            <c:when test="${not empty rec and rec.clockIn != null}">
              <fmt:formatDate value="${rec.clockIn}" pattern="HH:mm"/>
            </c:when>
            <c:otherwise>―</c:otherwise>
          </c:choose>
        </td>
        <td>
          <c:choose>
            <c:when test="${not empty rec and rec.clockOut != null}">
              <fmt:formatDate value="${rec.clockOut}" pattern="HH:mm"/>
            </c:when>
            <c:otherwise>―</c:otherwise>
          </c:choose>
        </td>
        <td>
          <c:choose>
            <c:when test="${not empty rec and rec.breakOut != null}">
              <fmt:formatDate value="${rec.breakOut}" pattern="HH:mm"/>
            </c:when>
            <c:otherwise>―</c:otherwise>
          </c:choose>
        </td>
        <td>
          <c:choose>
            <c:when test="${not empty rec and rec.breakIn != null}">
              <fmt:formatDate value="${rec.breakIn}" pattern="HH:mm"/>
            </c:when>
            <c:otherwise>―</c:otherwise>
          </c:choose>
        </td>
        <td>
          <c:choose>
            <c:when test="${not empty rec and rec.breakDuration != null}">
              ${rec.breakDuration.toString().substring(0,5)}
            </c:when>
            <c:otherwise>―</c:otherwise>
          </c:choose>
        </td>
      </tr>
    </c:forEach>
  </table>

  <form method="post" action="${pageContext.request.contextPath}/output">
    <input type="hidden" name="employeeId" value="${employeeId}"/>
    <input type="hidden" name="month"      value="${month}"/>
    <input type="hidden" name="format"     value="${format}"/>
    <button type="submit" name="action" value="back">戻る</button>
    <button type="submit" name="action" value="print">出力する</button>
  </form>
</body>
</html>
