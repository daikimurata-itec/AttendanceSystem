<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>登録画面</title></head>
<body>
<form action="staffRegist" method="post">
  名前：<input type="text" name="name" /><br/>
  アドレス：<input type="text" name="email" /><br/>
  パスワード<input type="password" name="password" /><br/>
  <% String error = (String) request.getAttribute("error"); %>
<% if (error != null) { %>
    <p style="color:red;"><%= error %></p>
<% } %>
  
  <button type="button" onclick="location.href='${pageContext.request.contextPath}/adminMenu'">HOME</button>
  <input type="submit" value="登録" />
</form>
</body>
</html>
