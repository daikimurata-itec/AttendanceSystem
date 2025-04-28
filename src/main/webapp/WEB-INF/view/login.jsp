<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>ログイン</title>
</head>
<body>
    <h1>ログイン</h1>

    <form action="login" method="post">
        <label for="username">メールアドレス</label>
        <input type="email" id="username" name="username" required><br><br>

        <label for="password">パスワード</label>
        <input type="password" id="password" name="password" required><br><br>

        <input type="submit" value="ログイン">
    </form>

    <c:if test="${not empty errorMessage}">
        <p style="color:red;">${errorMessage}</p>
    </c:if>
</body>
</html>
