<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>打刻編集 - 一覧画面</title>
    <style>
        .title-area {
            text-align: center;
            margin-top: 20px;
            font-size: 24px;
            font-weight: bold;
        }
        .sub-title-area {
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 10px 0 20px;
            font-size: 16px;
            position: relative;
        }
        .sub-title-area .back-button {
            position: absolute;
            right: 10%;
        }
        .button {
            padding: 6px 12px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        .button:hover {
            background-color: #45a049;
        }
        table {
            width: 80%;
            margin: 0 auto 40px;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f0f0f0;
        }
        .edit-button {
            padding: 4px 8px;
        }
    </style>
</head>
<body>

    <div class="title-area">打刻編集 - 一覧画面</div>

    <div class="sub-title-area">
        <div>
            （ID：${employeeId} / 名前：${employeeName} / 月：${month}）
        </div>
        <div class="back-button">
            <!-- 戻るボタンを検索画面へ遷移 -->
            <button class="button"
                    onclick="location.href='${pageContext.request.contextPath}/editAttendanceSearch'">
                戻る
            </button>
        </div>
    </div>

    <table>
        <thead>
            <tr>
                <th>日付</th>
                <th>出勤時刻</th>
                <th>退勤時刻</th>
                <th>編集</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="day" items="${allDaysInMonth}">
                <!-- key = "yyyy-MM-dd" -->
                <fmt:formatDate var="key" value="${day}" pattern="yyyy-MM-dd"/>
                <c:set var="rec" value="${recordMap[key]}"/>

                <tr>
                    <td><fmt:formatDate value="${day}" pattern="yyyy/MM/dd"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${rec != null && rec.clockIn != null}">
                                ${rec.clockIn.toString().substring(0,5)}
                            </c:when>
                            <c:otherwise>－</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${rec != null && rec.clockOut != null}">
                                ${rec.clockOut.toString().substring(0,5)}
                            </c:when>
                            <c:otherwise>－</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <form action="${pageContext.request.contextPath}/editAttendanceForm" method="get" style="margin:0;">
                            <input type="hidden" name="employeeId" value="${employeeId}" />
                            <input type="hidden" name="date"       value="${key}" />
                            <button type="submit" class="edit-button">編集</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>
