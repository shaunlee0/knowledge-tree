<head>
    <head>
        <title>Knowledge Tree : Graph</title>
    </head>
</head>
<jsp:include page='header.jsp'/>
</div>
<%--Graph begin--%>
<div class="alchemy" id="alchemy"></div>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/jscript/alchemy/0.4.1/alchemy.min.css"/>
<script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.16.4/lodash.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/jscript/alchemy/0.4.1/scripts/vendor.js"></script>
<script src="<%=request.getContextPath()%>/resources/jscript/alchemy/0.4.1/alchemy.js"></script>
<script src="<%=request.getContextPath()%>/resources/jscript/alchemy/0.4.1/alchemy.min.js"></script>

<script type="text/javascript">
    var config = {
        dataSource: '<%=request.getContextPath()%>/resources/data/graphData.json',
        directedEdges: true
    };

    alchemy = new Alchemy(config)
</script>
<%--Graph End--%>

<!-- include footer start (leave me alone) -->
<jsp:include page='footer.jsp'/>
<!-- include header end -->