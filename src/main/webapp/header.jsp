
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <!-- jQuery -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>

        <!-- jQuery UI -->        
        <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>

        <!-- BootStrap -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.min.js"></script>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>
        <script src="http://cdnjs.cloudflare.com/ajax/libs/modernizr/2.8.2/modernizr.js"></script>
        <%--Custom Scripts--%>
        <script src="<%=request.getContextPath()%>/resources/jscript/home-module.js"></script>

        <%--Alchemy--%>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/jscript/alchemy/0.4.1/alchemy.min.css"/>
        <script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.16.4/lodash.min.js"></script>
        <script src="<%=request.getContextPath()%>/resources/jscript/alchemy/0.4.1/scripts/vendor.js"></script>
        <script src="<%=request.getContextPath()%>/resources/jscript/alchemy/0.4.1/alchemy.js"></script>
        <script src="<%=request.getContextPath()%>/resources/jscript/alchemy/0.4.1/alchemy.min.js"></script>

        <style>
            .info-heading {
                color: green;
            }
            .user-information-section .user-information-block {
                display: block;
            }
            .information-value {
                margin-top: -5px;
                margin-bottom: 10px;
            }
        </style>

        <!-- Viewport -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
    </head>
    <body>
    <div class="se-pre-con"></div>
    <!-- navbar start -->
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="<%=request.getContextPath()%>">Knowledge Tree</a>
                </div>

                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">

                        <li><a href="<%=request.getContextPath()%>/results">Results</a></li>
                        <li><a href="<%=request.getContextPath()%>/graph/root">Root Entity</a></li>
                        <li><a href="<%=request.getContextPath()%>/graph/visual">Show Graph</a></li>

                    </ul>
                    <ul class="nav navbar-nav navbar-right">

                        <li>

                        </li>

                    </ul>
                </div>
            </div>
        </nav>
        <!-- navbar end -->

        <!-- main container -->
        <div class="container">