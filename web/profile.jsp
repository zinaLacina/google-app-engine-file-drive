<%-- 
    Document   : profile
    Created on : May 24, 2016, 2:30:40 PM
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
        <p id="Welcome"></p>
        <br><br>
        <form method="post" action="update">
            <table width="100%" border="0">
                <tr>
                    <td colspan="2" align="center"><b>Profile</b><br></td>
                </tr>
                <tr>
                    <td align="right"><b>Username:</b></td>
                    <td align="left"><input type="text" id="userName" name="userName" readonly></td>
                </tr>
                <tr>
                    <td align="right"><b>First name:</b></td>
                    <td align="left"><input type="text" id="firstName" name="firstName"></td>
                </tr>
                <tr>
                    <td align="right"><b>Last name:</b></td>
                    <td align="left"><input type="text" id="lastName" name="lastName"></td>
                </tr>
                <tr>
                    <td align="right"><b>Current password:</b></td>
                    <td align="left"><input type="password" name="passWord"></td>
                </tr>
                <tr>
                    <td align="right"><b>New password:</b></td>
                    <td align="left"><input type="password" id="nPassWord" name="nPassWord"></td>
                </tr>
                                <tr>
                    <td align="right"><b>Retype new password:</b></td>
                    <td align="left"><input type="password" id="rPassWord" name="rPassWord"></td>
                </tr>
                <tr>
                    <td colspan="2" align="center"><br><input type="submit" value="Save"></td>
                </tr>
            </table>
        </form>
        <script>
            document.getElementById("Welcome").value = "${fn:escapeXml(userIn.firstName)}";
            document.getElementById("userName").value = "${fn:escapeXml(userIn.userName)}";
            document.getElementById("firstName").value = "${fn:escapeXml(userIn.firstName)}";
            document.getElementById("lastName").value = "${fn:escapeXml(userIn.lastName)}";
            <%
              } else {
                session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Please login first!");
                response.sendRedirect(Defs.LOGIN_PAGE_STRING);
              }
            %>
        </script>
        <br>
        <hr>
    </body>
</html>
