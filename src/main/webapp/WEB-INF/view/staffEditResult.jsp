<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>リザルト画面</title></head>
<body>
<%
    Object idObj = request.getAttribute("id");
    int id = 0;
    if (idObj != null) {
        id = Integer.parseInt(idObj.toString());  // 安全にintに変換
    }
    String formattedId = String.format("%04d", id); // 4桁ゼロ埋め
%>
<p>ID: <%= formattedId %></p>
<p><%= request.getAttribute("name") %> さんの情報を更新しました。</p>
<form action="index.jsp" method="get">
  <button class="menu-button" formaction="${pageContext.request.contextPath}/adminMenu">HOME</button>
</form>
</body>
</html>