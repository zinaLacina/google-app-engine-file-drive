<%-- 
    Document   : upload
    Created on : May 24, 2016, 3:09:55 PM
    Author     : Lacina ZINA
--%>


<%@page import="config.Defs"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%
    User currentUser = (User) session.getAttribute(Defs.SESSION_USER_STRING);
    String name = currentUser.getFirstName() + " " + currentUser.getLastName();
%>

<jsp:include page="header/header.jsp"/>
<jsp:include page="header/menu-top.jsp">
    <jsp:param name="currentUsername" value="<%=name%>" />
</jsp:include>
<jsp:include page="header/aside.jsp"/>
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">

        <ul class="menu-home">
            <li><a href="home.jsp" class="btn btn-default"><i class="fa fa-home fa-2x"></i></a></li>
            <li><i class="fa fa-2x fa-angle-right"></i></li>
            <li class="dropdown">
                <a href="#" class="btn btn-default dropdown-toggle" type="button" id="menu1" data-toggle="dropdown"><i class="fa fa-2x fa-plus"></i></a>
                <ul class="dropdown-menu" role="menu" aria-labelledby="menu1">
                    <li><a href="upload.jsp"><i class="fa fa-upload"></i>Upload file</a></li>
                    <li><a href="folder.jsp"><i class="fa fa-folder"></i>New folder</a></li>
                    <li><a href="file.jsp"><i class="fa fa-file-text"></i>New text file</a></li>
                </ul>
            </li>
        </ul>
    </section>

    <!-- Main content -->
    <section class="content container-fluid">

        <!--------------------------
        | Your Page Content Here |
        -------------------------->
        <%
            User thisUser = (User) session.getAttribute(Defs.SESSION_USER_STRING);
            if (thisUser != null) {
                pageContext.setAttribute("userIn", thisUser);
        %>
        <p id="welcome"></p>
        <div class="box box-success">
            <div class="box-header with-border">
              <h3 class="box-title">Upload a file</h3>
            </div>
        <form class="form-horizontal formulaire" method="post" action="file">
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <label class="file-upload btn btn-success">File name</label>
                        <input type="text" id="fileName" name="fileName"/>
                    
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <label class="file-upload btn btn-success" for="fileContent">Content</label>
                    <textarea type="text" id="fileContent" name="fileContent" class="textarea"></textarea>
                    
                </div>
            </div>
            <div class="form-group">        
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">Create</button>
                </div>
            </div>
        </form>
        </div>

    </section>
    <!-- /.content -->
</div>

<script>
    document.getElementById("Welcome").innerHTML = "${fn:escapeXml(userIn.firstName)}";
    <%
        } else {
            session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Please login first!");
            response.sendRedirect(Defs.LOGIN_PAGE_STRING);
        }
    %>
</script>
<jsp:include page="footer/footer.jsp"/>
