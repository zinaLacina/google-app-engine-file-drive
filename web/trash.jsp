<%-- 
    Document   : upload
    Created on : May 24, 2016, 3:09:55 PM
    Author     : Lacina ZINA
--%>


<%@page import="helper.Help"%>
<%@page import="com.google.appengine.repackaged.org.apache.commons.logging.Log"%>
<%@page import="java.util.NoSuchElementException"%>
<%@page import="com.google.appengine.api.datastore.Query.Filter"%>
<%@page import="com.google.appengine.api.datastore.Query.FilterPredicate"%>
<%@page import="com.google.appengine.api.datastore.Query.FilterOperator"%>
<%@page import="com.google.appengine.api.datastore.Key"%>
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
    //User currentUser = (User) session.getAttribute(Defs.SESSION_USER_STRING);
    String name;
    User thisUser = (User) session.getAttribute(Defs.SESSION_USER_STRING);

    long userId;
    if (thisUser != null) {
        name = thisUser.getFirstName() + " " + thisUser.getLastName();
        pageContext.setAttribute("userIn", thisUser);
        userId = thisUser.getUserId();
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
        <%
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Filter currentIdUser = new FilterPredicate(Defs.ENTITY_PROPERTY_OWNER, FilterOperator.EQUAL, userId);
            Query fileQuery = new Query(Defs.DATASTORE_KIND_TRASH_STRING).setFilter(currentIdUser);
            List<Entity> files = datastore.prepare(fileQuery).asList(FetchOptions.Builder.withDefaults());
            if (!files.isEmpty()) {
                Iterator<Entity> allFiles = files.iterator();
        %>
        <table class="table table-hover">
            <thead>
                <tr>
                    <td><input type="checkbox"  id="all"></td>
                    <td>Type</td>
                    <td><b>File name</b></td>
                    <td>Size</td>
                    <td></td>
                    <td></td>
                </tr>
            </thead>

            <%
                while (allFiles.hasNext()) {
                    Entity log = allFiles.next();
                    long isFolder = (long) log.getProperty(Defs.ENTITY_PROPERTY_FOLDER);
                    String fileName = (String) log.getProperty(Defs.ENTITY_PROPERTY_FILENAME_STRING);
                    String extension = (String) log.getProperty(Defs.ENTITY_PROPERTY_FILETYPE);
                    Long fileId = (long) log.getKey().getId();

            %>
            <tbody>
                <%                    
                    if (isFolder == 0) {
                        double fileSize = (long) log.getProperty(Defs.ENTITY_PROPERTY_FILESIZE);
                        String size = Help.format(fileSize, 2);
                %>
                <tr>
                    <td><input type="checkbox" name="file[]"></td>
                    <td><i class="fa fa-file"></i></td>
                    <td><%=fileName%></td>
                    <td><%=size%></td>
                    <td><a href='trash?fileId=<%=fileId%>&&action=undo'>Undo</a></td>
                    <td><a href='trash?fileId=<%=fileId%>&&action=delete'>definitely delete</a></td>
                </tr>
                <% } else {%>

                <tr>
                    <td><input type="checkbox" name="file[]"></td>
                    <td><i class="fa fa-folder-o"></i></td>
                    <td><%=fileName%></td>
                    <td></td>
                    <td></td>
                </tr>
                <% } %>
            </tbody>
            <%
                }
            } else {
            %>

        </table>
        <div class="alert alert-warning">
            <strong>No deleted files</strong>
        </div>
        <%
            }
        %>

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