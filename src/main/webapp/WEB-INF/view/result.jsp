<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<body>
    <h2>${message}</h2>
    <form action="${pageContext.request.contextPath}/adminMenu" method="get">
        <button type="submit">HOME</button>
    </form>
</body>
</html>
