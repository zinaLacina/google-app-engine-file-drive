<%-- 
    Document   : upload
    Created on : May 24, 2016, 3:09:55 PM
    Author     : Muhammad Wannous
--%>

<%@page import="ccDocStrg.Defs"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="ccDocStrg.User" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cloud Storage</title>
    </head>
    <body>
        <h1>Cloud Storage</h1>
        <h2>A Cloud-based application for storing files.</h2>
        <hr>
        <%
          User thisUser = (User) session.getAttribute(Defs.SESSION_USER_STRING);
          if (thisUser != null) {
            pageContext.setAttribute("userIn", thisUser);
        %>
        <p id="welcome"></p>
        <form method="post" action="upload" enctype="multipart/form-data">
            <table width="100%" border="0">
                <tr>
                    <td colspan="2" align="center"><b>Upload a file</b><br></td>
                </tr>
                <tr>
                    <td align="right"><b>File name:</b></td>
                    <td align="left"><input type="file" id="fileName" name="fileName"></td>
                </tr>
                <tr><td></td><td></td></tr>
                <tr>
                    <td colspan="2" align="center"><br><input type="submit" value="Upload"></td>
                </tr>
            </table>
        </form>
        <script>
            document.getElementById("Welcome").innerHTML = "${fn:escapeXml(userIn.firstName)}";
            <%
              } else {
                session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Please login first!");
                response.sendRedirect(Defs.LOGIN_PAGE_STRING);
              }
            %>
        </script>
        <br><br>

    </body>
</html>
