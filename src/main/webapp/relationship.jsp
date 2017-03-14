<%@ page import="com.shaun.knowledgetree.domain.SingularWikiEntityDto" %>
<%@ page import="com.shaun.knowledgetree.domain.Relationship" %>
<%@ page import="java.util.List" %>
<!-- include header start (leave me alone) -->
<jsp:include page='header.jsp'/>
<!-- include header end -->

<!-- page content start (customise) -->
<%
    SingularWikiEntityDto startNode = (SingularWikiEntityDto) request.getAttribute("startNode");
    SingularWikiEntityDto endNode = (SingularWikiEntityDto) request.getAttribute("endNode");
    List<Relationship> relationshipList = (List<Relationship>) request.getAttribute("relationships");
%>

<head>
    <title>Knowledge Tree - Relationship : <%=startNode.getTitle()%> &#8594; <%=endNode.getTitle()%></title>
</head>

<h1 align="center">Relationship : <a href="<%=request.getContextPath()%>/graph/article/<%=startNode.getTitle()%>"><%=startNode.getTitle()%></a> &#8594; <a href="<%=request.getContextPath()%>/graph/article/<%=endNode.getTitle()%>"><%=endNode.getTitle()%></a></h1>
<h3>Relationships Table</h3>

<div class="col-md-12">
    <table class="table">
        <thead>
        <tr>
            <th width="15%">Type</th>
            <th width="75%">Relationship Content</th>
            <th width="10%">Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            int relationshipIndex = 1;

            for (Relationship relationship : relationshipList) {
        %>
        <tr>
            <td>
                <%if(relationship.getExplicitConnection()!=null){
                    out.print("<b>Explicit</b>");
                }%>
                <%if(relationship.getOnlyEndNodeConnection()!=null){
                    out.print("<b>Only End Node</b>");
                }%>
            </td>
            <td>
                <%if(relationship.getExplicitConnection()!=null){
                out.print(relationship.getExplicitConnection());
                }%>
                <%if(relationship.getOnlyEndNodeConnection()!=null){
                out.print(relationship.getOnlyEndNodeConnection());
                }%>
            </td>
            <td>
                <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#sysnsetModal<%=relationshipIndex%>">View Synset of Relationship</button>
                <!-- Modal -->
                <div id="sysnsetModal<%=relationshipIndex%>" class="modal fade" role="dialog">
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Synset for Relationship</h4>
                            </div>
                            <div class="modal-body">
                                <p><%
                                    int count = 0;
                                    String output = "";
                                    for (String s : relationship.getSynsetFromConnectingSentence()) {
                                        s = s.replace("+"," ");
                                        if (!output.equals("")){
                                            output = output + ", " + s;
                                        }else{
                                            output = s;
                                        }if(count % 10 == 0){
//                                            output = output + "<br /> ";
                                        }
                                        count ++;
                                    }
                                %>
                                    <%=output%></p>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>

                    </div>
                </div>
            </td>
            <%
              relationshipIndex ++;  }
            %>
        </tr>
        </tbody>
    </table>
</div>

<!-- page content end -->

<!-- include footer start (leave me alone) -->
<jsp:include page='footer.jsp'/>
<!-- include header end -->