<%@ page import="com.shaun.knowledgetree.domain.Category" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.shaun.knowledgetree.domain.Graph" %>
<!-- include header start (leave me alone) -->
<jsp:include page='header.jsp'/>
<!-- include header end -->

<!-- page content start (customise) -->
<h1>Results</h1>
<div class="row" id="graphTable">
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
                Graph graph = (Graph)request.getAttribute("graph");
                Set<Category> categories = graph.getAllCategories();
                for (Category category : categories) {
            %>
            <form role="form" method="get" action="category">
                <tr>
                    <td><%=category.getName()%></td>
                    <td><button class="btn btn-success">View</button></td>
                </tr>
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