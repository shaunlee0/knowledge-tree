<%@ page import="com.shaun.knowledgetree.domain.SingularWikiEntityDto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- include header start (leave me alone) -->
<jsp:include page='header.jsp'/>
<!-- include header end -->

<!-- page content start (customise) -->

<%
    SingularWikiEntityDto article = (SingularWikiEntityDto) request.getAttribute("article");
%>

<head>
    <title>Knowledge Tree - Article : <%=article.getTitle()%></title>
</head>

<h1>Article info for <%=article.getTitle()%></h1>

<%=article.getPageContent().getHtml()%>

<!-- page content end -->

<!-- include footer start (leave me alone) -->
<jsp:include page='footer.jsp'/>
<!-- include header end -->