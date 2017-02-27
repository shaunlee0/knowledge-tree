<%@ page import="com.shaun.knowledgetree.domain.Category" %>
<%@ page import="com.shaun.knowledgetree.domain.Graph" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<!-- include header start (leave me alone) -->
<jsp:include page='header.jsp'/>
<!-- include header end -->
<%
    Graph graph = (Graph) session.getAttribute("graph");
    Set<Category> categories = graph.getAllCategories();
    HashMap<String, Integer> allLinksAndOccurrences = (HashMap<String, Integer>) session.getAttribute("allLinksAndOccurrences");
%>
<head>
    <title>Knowledge Tree - Results : <%=graph.getSearchTerm()%>
    </title>
</head>

<!-- page content start (customise) -->
<h1 align="center">Results for <%=graph.getSearchTerm()%>
</h1>
<br>
<div align="center">
    <button onclick="forwardToRelevancePage()" id="goToRankingsPage" data-toggle="collapse" class="btn btn-info">Relevance Rankings Page</button>
    <button class="btn btn-info" data-toggle="collapse" data-target="#topOccurringArticlesTable">Show Top Occurring Links</button>
    <button onclick="forwardToRootPage()" id="goToRootPage" data-toggle="collapse" class="btn btn-info">Root Article Page</button>
    <button class="btn btn-info" data-toggle="collapse" data-target="#categoriesTable">Show Categories</button>
    <div class="row collapse" id="categoriesTable">
        <div class="col-md-12">
            <table class="table">
                <thead>
                <tr>
                    <th>Categories</th>
                    <th>View Category</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for (Category category : categories) {
                %>
                <form role="form" method="get" action="category">
                    <tr>
                        <td><%=category.getName()%>
                        </td>
                        <td>
                            <button class="btn btn-success">View</button>
                        </td>
                    </tr>
                </form>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
    <div class="row collapse" id="topOccurringArticlesTable">
        <div class="col-md-12">
            <table class="table">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Article Title</th>
                    <th>Occurrences</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <%
                    int count = 1;
                    for (Map.Entry<String, Integer> entry : allLinksAndOccurrences.entrySet()) {
                        if (count > 20) {
                            break;
                        }


                %>
                <tr>
                    <td><b><%=count%>.</b></td>
                    <td><%=entry.getKey()%>
                    </td>
                    <td><%=entry.getValue()%>
                    </td>
                    <td>
                        <form target="_blank" action="<%=request.getContextPath()%>/graph/article/<%=entry.getKey()%>"
                              method="get">
                            <button type="submit" class="btn btn-success">View Article</button>
                        </form>
                    </td>
                </tr>
                <%
                        count++;
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- page content end -->

<!-- include footer start (leave me alone) -->
<jsp:include page='footer.jsp'/>
<!-- include header end -->