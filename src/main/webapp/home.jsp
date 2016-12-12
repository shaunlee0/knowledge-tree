<html>
<head>
    <title>The Knowledge Tree</title>
    <%--<style type="text/css">--%>
    <%--body {--%>
    <%--background-image: url('https://www.bhmpics.com/wallpapers/rives_and_forest_in_the_fog-1920x1200.jpg');--%>
    <%--}--%>
    <%--</style>--%>
</head>
<body>
<!-- include header start (leave me alone) -->
<jsp:include page='header.jsp'/>
<!-- include header end -->

<!-- page content start (customise) -->
<div class="col-lg-6">
    <form action="<%=request.getContextPath()%>/validate" method="post" id="search-form">
        <label for="searchTerm">Make a Search</label>
        <div class="input-group">
            <input type="text" id="searchTerm" name="searchTerm" class="form-control" placeholder="Search for...">

            <span class="input-group-btn">
        <button id="submitSearch" for="searchTerm" class="btn btn-default" type="submit">Go!</button>
      </span>
        </div><!-- /input-group -->
        <br>
        <div id="validationErrorMsg" class="alert alert-danger" role="alert" style="display: none">
            <strong>Search failed!</strong> Please enter an existing wikipedia article as a search term.
        </div>
        <%--<div style="text-align: center; display: none; color:red" id="validationErrorMsg">Please enter an existing valid wikipedia article as a search term</div>--%>
    </form>
    <%--<div class="form-group has-error has-feedback">--%>
    <%--<label class="col-sm-2 control-label" for="inputError">Input with error and glyphicon</label>--%>
    <%--<div class="col-sm-10">--%>
    <%--<input type="text" class="form-control" id="inputError">--%>
    <%--<span class="glyphicon glyphicon-remove form-control-feedback"></span>--%>
    <%--</div>--%>
    <%--</div>--%>
</div><!-- /.col-lg-6 -->

<!-- page content end -->

<!-- include footer start (leave me alone) -->
<jsp:include page='footer.jsp'/>
<!-- include header end -->
</body>
</html>