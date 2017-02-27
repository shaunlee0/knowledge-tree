




<!-- include header start (leave me alone) -->
<jsp:include page='header.jsp'/>
<!-- include header end -->
<head>
    <title>Knowledge Tree : Graph</title>
</head>
<!-- page content start (customise) -->
<div class="alchemy" id="alchemy"></div>


<script type="text/javascript">
    var config = {
        dataSource: '<%=request.getContextPath()%>/resources/data/charlize.json',
    };

    alchemy = new Alchemy(config)
</script>
<!-- page content end -->

<!-- include footer start (leave me alone) -->
<jsp:include page='footer.jsp'/>
<!-- include header end -->
