<%@ page import="com.shaun.knowledgetree.domain.SingularWikiEntityDto" %>
<%@ page import="com.shaun.knowledgetree.domain.Relationship" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- include header start (leave me alone) -->
<jsp:include page='header.jsp'/>
<!-- include header end -->

<!-- page content start (customise) -->

<%
    SingularWikiEntityDto article = (SingularWikiEntityDto) request.getAttribute("article");
    List<Relationship> relationshipList = article.getRelatedEntities();
%>

<head>
    <title>Knowledge Tree - Article : <%=article.getTitle()%></title>
</head>

<h1>Article info for <%=article.getTitle()%></h1>

<button class="btn btn-info" data-toggle="collapse" data-target="#articleContent">Show Article Content</button>
<button class="btn btn-info" data-toggle="collapse" data-target="#relationships">Show Relationships</button>

<div class="row collapse" id="articleContent">
    <%=article.getPageContent().getHtml()%>
</div>

<div class="row collapse" id="relationships">
    <div class="col-md-12">
        <table class="table">
            <thead>
            <tr>
                <th>Relationship End Node</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <%
                for (Relationship relationship : relationshipList) {
            %>
            <form target="_blank" role="form" method="get" action="<%=request.getContextPath()%>/results/relationship/<%=article.getTitle()%>">
                <tr>
                    <td><%=relationship.getEndNode().getTitle()%>
                    </td>
                    <td>
                        <button type="submit" class="btn btn-success">View Relationship</button>
                    </td>
                </tr>
                <input type="hidden" name="endNodeTitle" value="<%=relationship.getEndNode().getTitle()%>">
            </form>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
</div>

<!-- page content end -->

<!-- include footer start (leave me alone) -->
<jsp:include page='footer.jsp'/>
<!-- include header end -->