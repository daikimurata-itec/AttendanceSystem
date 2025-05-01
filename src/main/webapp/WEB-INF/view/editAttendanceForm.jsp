<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <title>打刻編集 - 編集</title>
  <style>
    /* 必要に応じてスタイル */
  </style>
</head>
<body>
  <h2>打刻編集 - 編集画面</h2>

  <form method="post" action="${pageContext.request.contextPath}/editAttendanceForm">
    <!-- recordId は既存 -->
    <input type="hidden" name="recordId"   value="${att.recordId}" />
    <!-- ここで必ず employeeId と month も送る -->
    <input type="hidden" name="employeeId" value="${att.employeeId}" />
    <input type="hidden" name="month"      value="${month}" />
    <!-- workDate は existing code -->
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

    <button type="submit">更新</button>
    <!-- 戻る：history.back() でも、明示的に一覧に戻すなら -->
    <button type="button"
            onclick="location.href='${pageContext.request.contextPath}/editAttendanceList?employeeId=${att.employeeId}&month=${month}'">
      戻る
    </button>
  </form>
</body>
</html>
