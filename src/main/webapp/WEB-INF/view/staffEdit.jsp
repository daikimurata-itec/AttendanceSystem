<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>編集画面</title></head>
<body>
<form action="staffEdit" method="post">
  <input type="hidden" name="id" value="<%= request.getAttribute("id") %>" />

  名前：<input type="text" name="name" 
        value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>" /><br/>

  アドレス：<input type="text" name="email" 
        value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" /><br/>

  パスワード：<input type="password" name="password" /><br/>

  <% String error = (String) request.getAttribute("error"); %>
  <% if (error != null) { %>
    <p style="color:red;"><%= error %></p>
  <% } %>

  <button type="button" onclick="location.href='${pageContext.request.contextPath}/staffEditSearch'">戻る</button>
  <input type="submit" value="登録" />
</form>
</body>

</html>

