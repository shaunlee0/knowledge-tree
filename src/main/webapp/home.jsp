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
    <h1 align="center">Search
    </h1>
    <label for="searchTerm">Make a Search</label>
    <div class="input-group">
        <input type="text" id="searchTerm" name="searchTerm" class="form-control" aria-describedby="sizing-addon1"
               placeholder="Search for...">
            <span class="input-group-btn">
                <button id="submitSearch" for="searchTerm" class="btn btn-default" type="submit">Go!</button>
            </span>

    </div><!-- /input-group -->

    <br>

    <div class="input-group">
        <span class="input-group-addon" id="basic-addon1">Enter related articles limit:</span>
        <input type="number" id="linkDepthLimit" class="form-control" placeholder="20"
               aria-describedby="basic-addon1">
        <span class="input-group-addon" id="basic-addon2">Enter max generations:</span>

        <select class="selectpicker form-control" id="maxGenerations" aria-describedby="basic-addon2">
            <option selected="selected">1</option>
            <option>2</option>
        </select>
    </div>


    <div class="row">
        <br>
        <br>
        <div id="validationErrorMsg" class="alert alert-danger" role="alert" style="display: none">
            <strong>Search failed!</strong> Please enter an existing Wikipedia article as a search term.
        </div>
        <div id="systemErrorMsg" class="alert alert-danger" role="alert" style="display: none">
            <strong>Search failed!</strong> A system error has occurred.
        </div>
    </div>

</div><!-- /.col-lg-6 -->

<!-- page content end -->

<!-- include footer start (leave me alone) -->
<jsp:include page='footer.jsp'/>
<!-- include header end -->
</body>
</html>