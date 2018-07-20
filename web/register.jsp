<%-- 
    Document   : register
    Created on : May 24, 2016, 12:36:55 PM
    Author     : Muhammad Wannous
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cloud Storage (Registration)</title>
    </head>
    <body>
        <h1>Cloud Storage</h1>
        <h2>A Cloud-based application for storing files.</h2>
        <hr>
        <br><br>
        <form method="post" action="register">
            <table width="100%" border="0">
                <tr>
                    <td colspan="3" align="center"><b>Register</b><br></td>
                </tr>
                <tr>
                    <td align="right"><b>Username:</b></td>
                    <td align="left"><input type="text" id="userName" name="userName"></td>
                    <td>Required. 5~30 characters in the format <i>name</i>@<i>company</i>.<i>com</i> </td>
                </tr>
                <tr>
                    <td align="right"><b>Password:</b></td>
                    <td align="left"><input type="password" id="passWord" name="passWord"></td>
                    <td>Required. 7~15 characters.</td>
                </tr>
                <tr>
                    <td align="right"><b>Password (again):</b></td>
                    <td align="left"><input type="password" id="rePassWord" name="rPassWord"></td>
                    <td>Required. Retype the same password for confirmation.</td>
                </tr>
                <tr>
                    <td colspan="3" align="center"><br><input type="submit" value="Register"></td>
                </tr>
            </table>
        </form>
        <br>
        <hr>
        <a href="/">Home</a>
    </body>
</html>
