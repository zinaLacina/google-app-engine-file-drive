<%-- 
    Document   : home
    Created on : Jul 22, 2018, 4:32:20 AM
    Author     : lacinazina
--%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.google.appengine.api.datastore.FetchOptions"%>
<%@page import="com.google.appengine.api.datastore.Query"%>
<%@page import="com.google.appengine.api.datastore.Entity"%>
<%@page import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@page import="com.google.appengine.api.datastore.DatastoreService"%>
<%@page import="config.Defs"%>
<%@page import="model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

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
        <table class="table table-hover">
            <%

                if (currentUser != null) {
                    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                    Query fileQuery = new Query(Defs.DATASTORE_KIND_FILES_STRING);
                    List<Entity> files = datastore.prepare(fileQuery).asList(FetchOptions.Builder.withDefaults());
                    if (!files.isEmpty()) {
                        Iterator<Entity> allFiles = files.iterator();
            %>
            <thead>
                <tr>
                    <td><b>File name</b></td><td></td><td></td>
                </tr>
            </thead>

            <%
                while (allFiles.hasNext()) {
                    String fileName = (String) allFiles.next().getProperty(Defs.ENTITY_PROPERTY_FILENAME_STRING);
            %>
            <tbody>
                <tr>
                    <td><%=fileName%></td>
                    <td><a href='download?fileName=<%=fileName%>'>download</a></td>
                    <td><a href='delete?fileName=<%=fileName%>'>delete</a></td>
                </tr>
            </tbody>
            <%
                    }
                }
            %>
        </table>


        <%              } else {
                session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Please login firt!");
                response.sendRedirect(Defs.LOGIN_PAGE_STRING);
            }
        %>
    </section>
    <!-- /.content -->
</div>

<jsp:include page="footer/footer.jsp"/>
