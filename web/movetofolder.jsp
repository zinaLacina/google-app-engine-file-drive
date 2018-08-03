<%-- 
    Document   : upload
    Created on : May 24, 2016, 3:09:55 PM
    Author     : Lacina ZINA
--%>


<%@page import="helper.Help"%>
<%@page import="model.Files"%>
<%@page import="java.util.List"%>
<%@page import="config.Defs"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%
    //User currentUser = (User) session.getAttribute(Defs.SESSION_USER_STRING);
    String name;
    User thisUser = (User) session.getAttribute(Defs.SESSION_USER_STRING);
    if (thisUser != null) {
        name = thisUser.getFirstName() + " " + thisUser.getLastName();
        pageContext.setAttribute("userIn", thisUser);
        String message = (String)session.getAttribute(Defs.SESSION_MESSAGE_STRING);
        
        String fileId = (String)request.getAttribute("fileId");
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

        <p id="welcome"></p>
        <p id="message"><%=message%></p>
        <div class="box box-success">
            <div class="box-header with-border">
                <h3 class="box-title">Move file to folder</h3>
            </div>
            <form class="form-horizontal formulaire" method="post" action="movetofolder">
                
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <label for="folderName">Folder</label>
                        <input type="hidden" name="folderId" value="<%=fileId%>">
                        <select name="folderName" class="form-control" id="folderName" required="true">
                            <option value="" selected>No folder</option>
                            <%
                                List<Files> list = Help.folder(thisUser.getUserId());
                                for (int i = 0; i < list.size(); i++) {
                                    long folderId = list.get(i).getFileId();
                                    String folderName = list.get(i).getFileName();
                            %>
                            <option value="<%=folderId%>"><%=folderName%></option>
                            <%} %>
                        </select>
                    </div>
                </div>
                <div class="form-group">        
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-default">Move</button>
                    </div>
                </div>
            </form>
        </div>

    </section>
    <!-- /.content -->
</div>


<jsp:include page="footer/footer.jsp"/>

<jsp:include page="footer/close.jsp"/>
<%
    } else {
        session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Please login first!");
        response.sendRedirect(Defs.LOGIN_PAGE_STRING);
    }
%>
