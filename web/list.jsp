<%-- 
    Document   : list
    Created on : May 25, 2016, 12:54:30 PM
    Author     : Muhammad Wannous
--%>

<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.google.appengine.api.datastore.FetchOptions"%>
<%@page import="com.google.appengine.api.datastore.Query"%>
<%@page import="com.google.appengine.api.datastore.Entity"%>
<%@page import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@page import="com.google.appengine.api.datastore.DatastoreService"%>
<%@page import="ccDocStrg.Defs"%>
<%@page import="ccDocStrg.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cloud Storage</title>
    </head>
    <body>
        <h1>Cloud Storage</h1>
        <h2>A Cloud-based application for storing files.</h2>
        <p align="right">
            <%=session.getAttribute(Defs.SESSION_MESSAGE_STRING)%>
        </p>
        <hr>
        <br><br>
        <table>
            <%
              User currentUser = (User) session.getAttribute(Defs.SESSION_USER_STRING);
              if (currentUser != null) {
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                Query fileQuery = new Query(Defs.DATASTORE_KIND_FILES_STRING);
                List<Entity> files = datastore.prepare(fileQuery).asList(FetchOptions.Builder.withDefaults());
                if (!files.isEmpty()) {
                  Iterator<Entity> allFiles = files.iterator();
            %>
            <tr>
                <td><b>File name</b></td><td></td><td></td>
            </tr>
            <%
                  while (allFiles.hasNext()) {
                    String fileName = (String)allFiles.next().getProperty(Defs.ENTITY_PROPERTY_FILENAME_STRING);
            %>
            <tr>
                <td><%=fileName%></td>
                <td><a href='download?fileName=<%=fileName%>'>download</a></td>
                <td><a href='delete?fileName=<%=fileName%>'>delete</a></td>
            </tr>
            <%
                }
              }
            %>
        </table>
        <br>
        <hr>
        <footer>
            <a href="/">Home</a> | 
            <a href="upload.jsp">Upload a file</a> | 
            <a href="logout">Logout</a> | 
            <a href="profile.jsp">Update profile</a></footer>
            <%              } else {
                session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Please login firt!");
                response.sendRedirect(Defs.LOGIN_PAGE_STRING);
              }
            %>
    </body>
</html>
