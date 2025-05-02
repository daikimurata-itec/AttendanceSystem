<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <title>打刻編集 - 編集</title>
</head>
<body>
  <h2>打刻編集 - 編集画面</h2>

  <form method="post" action="${pageContext.request.contextPath}/editAttendanceForm">
    <input type="hidden" name="recordId"   value="${att.recordId}" />
    <input type="hidden" name="employeeId" value="${att.employeeId}" />
    <input type="hidden" name="month"      value="${month}" />
    <input type="hidden" name="workDate"
           value="<fmt:formatDate value='${att.workDate}' pattern='yyyy-MM-dd' />" />

    <p>社員ID: ${att.employeeId}</p>
    <p>日付: <fmt:formatDate value="${att.workDate}" pattern="yyyy-MM-dd" /></p>

    <label>出勤時刻:
      <input type="time" name="clockIn"
             value="<fmt:formatDate value='${att.clockIn}' pattern='HH:mm' />" required />
    </label><br/>

    <label>退勤時刻:
      <input type="time" name="clockOut"
             value="<fmt:formatDate value='${att.clockOut}' pattern='HH:mm' />" required />
    </label><br/>

    <label>休憩時間:
      <input type="time" name="breakDuration"
             value="<fmt:formatDate value='${att.breakDuration}' pattern='HH:mm' />" required />
    </label><br/>

    <button type="submit" name="action" value="update">更新</button>

    <c:if test="${att.recordId != 0}">
      <button type="submit" name="action" value="delete"
              onclick="return confirm('本当に削除しますか？');">
        削除
      </button>
    </c:if>

    <button type="button"
            onclick="location.href='${pageContext.request.contextPath}/editAttendanceList?employeeId=${att.employeeId}&month=${month}'">
      戻る
    </button>
  </form>
</body>
</html>
