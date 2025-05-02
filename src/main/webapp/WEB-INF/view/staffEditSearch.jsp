<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>スタッフ検索</title>
</head>
<body>
    <h2>スタッフ検索</h2>

    <form method="get" action="staffEditSearch">
        社員ID: 
        <input type="text" name="employeeId" />
        <button type="submit" name="action" value = "search">検索</button>
    </form>

    <hr/>

    <c:choose>
        <c:when test="${searched == 'found'}">
            <p>ID: <%= String.format("%04d", (Integer) request.getAttribute("id")) %></p>
            <p>${name}</p>

            

            
        </c:when>

        <c:when test="${searched == 'emptyId'}">
            <p style="color: red;">社員IDを入力してください。</p>
        </c:when>

        <c:when test="${searched == 'invalidId'}">
            <p style="color: red;">社員IDは数字で入力してください。</p>
        </c:when>

        <c:when test="${searched == 'notFound'}">
            <p style="color: red;">該当するスタッフが見つかりませんでした。</p>
        </c:when>
    </c:choose>
    
    <button type="button" onclick="location.href='${pageContext.request.contextPath}/adminMenu'">HOME</button>
    

    <form action="staffEditSearch" method="get" style="display:inline;">

                <input type="hidden" name="employeeId" value="${id}" />
                <button type="submit" name="action" value = "edit">編集</button>
            </form>
</body>

</html>

