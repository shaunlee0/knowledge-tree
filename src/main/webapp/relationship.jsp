<%@ page import="com.shaun.knowledgetree.domain.SingularWikiEntityDto" %>
<!-- include header start (leave me alone) -->
<jsp:include page='header.jsp'/>
<!-- include header end -->

<!-- page content start (customise) -->
<%
    SingularWikiEntityDto startNode = (SingularWikiEntityDto) request.getAttribute("startNode");
    SingularWikiEntityDto endNode = (SingularWikiEntityDto) request.getAttribute("endNode");
%>

<head>
    <title>Knowledge Tree - Relationship Info for : <%=startNode.getTitle()%> -> <%=endNode.getTitle()%></title>
</head>

<h1>Relationship Info for : <%=startNode.getTitle()%> -> <%=endNode.getTitle()%></h1>
<!-- page content end -->

<!-- include footer start (leave me alone) -->
<jsp:include page='footer.jsp'/>
<!-- include header end -->