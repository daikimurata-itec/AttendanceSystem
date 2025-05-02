<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>管理者メニュー</title>
    <style>
        .menu-button { margin: 5px; padding: 10px 20px; font-size: 16px; }
        .logout { position: absolute; top: 10px; right: 10px; }
    </style>
</head>
<body>
    <div class="logout">
        <form action="${pageContext.request.contextPath}/login" method="get">
            <button type="submit">ログアウト</button>
        </form>
    </div>
    <h2>管理者メニュー</h2>
    <form action="" method="get">
        <button class="menu-button" formaction="${pageContext.request.contextPath}/staffRegist">スタッフ登録</button>
        <button class="menu-button" formaction="${pageContext.request.contextPath}/staffEditSearch">スタッフ編集</button>
        <button class="menu-button" formaction="${pageContext.request.contextPath}/staffDeleteSearch">スタッフ削除</button>
        <button class="menu-button" formaction="${pageContext.request.contextPath}/editAttendanceSearch">打刻編集</button>
        <button class="menu-button" formaction="${pageContext.request.contextPath}/output">出力</button>
    </form>
</body>
</html>