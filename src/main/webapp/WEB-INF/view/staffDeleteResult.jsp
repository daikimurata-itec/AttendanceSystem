<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>リザルト画面</title></head>
<body>
<%
    int id = (int) request.getAttribute("id");
    String formattedId = String.format("%04d", id); // 4桁ゼロ埋め
%>
<p>ID: <%= formattedId %></p>
<p><%= request.getAttribute("name") %> さんを削除しました。</p>
<form action="index.jsp" method="get">
  <button class="menu-button" formaction="${pageContext.request.contextPath}/adminMenu">HOME</button>
</form>
</body>
</html>