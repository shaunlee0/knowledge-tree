<%@ page import="com.shaun.knowledgetree.domain.Graph" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<!-- include header start (leave me alone) -->
<jsp:include page='header.jsp'/>
<!-- include header end -->

<%
    Graph graph = (Graph) session.getAttribute("graph");
    HashMap<String, Double> entitiesAndRelevance = (HashMap<String, Double>) session.getAttribute("relevanceRankings");
%>
<head>
    <title>Knowledge Tree - Relevance Rankings For : <%=graph.getSearchTerm()%>
    </title>
</head>

<h1 align="center">Relevance Rankings For : <%=graph.getSearchTerm()%>
</h1>
<br>
<div align="center">

    <div class="col-md-12">
        <table class="table">
            <thead>
            <tr>
                <th>#</th>
                <th>Article Title</th>
                <th>Cosine Similarity</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <%

                int counter = 1;
                for (Map.Entry<String, Double> entry : entitiesAndRelevance.entrySet()) {
                    if (counter > 10) {
                        break;
                    }


            %>
            <tr>
                <td><b><%=counter%>.</b></td>
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
                    counter++;
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