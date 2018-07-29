<%-- 
    Document   : index
    Created on : May 24, 2016, 12:21:05 PM
    Author     : Muhammad Wannous
--%>

<%@page import="config.Defs"%>
<%@ page import="model.User" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%

    User thisUser = (User) session.getAttribute(Defs.SESSION_USER_STRING);
    if (thisUser != null) {
        response.sendRedirect(Defs.LIST_PAGE_STRING);
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Cloud storage | Log in</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.7 -->
        <link rel="stylesheet" href="lib/bower_components/bootstrap/dist/css/bootstrap.min.css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="lib/bower_components/font-awesome/css/font-awesome.min.css">

        <!-- Theme style -->
        <link rel="stylesheet" href="lib/dist/css/AdminLTE.min.css">
        <link rel="stylesheet" href="assets/css/style.css">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

        <!-- Google Font -->
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
    </head>
    <body class="hold-transition login-page">

        <div class="login-box">
            <div class="login-logo">
                <a href="#"><b>CLOUD</b>Storage</a><br>
                <i style="font-size: 0.4em">A Cloud-based application for storing files.</i>
            </div>
            <!-- /.login-logo -->
            <div class="login-box-body">
                <p class="login-box-msg">Sign in to start your session</p>
                <p align="center" class="error blink_me">
                    <%=(session.getAttribute(Defs.SESSION_MESSAGE_STRING) == null ? ""
                            : session.getAttribute(Defs.SESSION_MESSAGE_STRING))%>
                </p>

                <form method="post" action="validate">
                    <div class="form-group has-feedback">
                        <input type="email" class="form-control" placeholder="Email" id="userName" name="userName">
                        <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
                    </div>
                    <div class="form-group has-feedback">
                        <input type="password" class="form-control" placeholder="Password" id="passWord" name="passWord">
                        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                    </div>
                    <div class="row">
                        <div class="col-xs-1"></div>
                        <div class="col-xs-6">
                            <div class="checkbox icheck">
                                <label>
                                    <input type="checkbox"> Remember Me
                                </label>
                            </div>
                        </div>
                        <!-- /.col -->
                        <div class="col-xs-4">
                            <button type="submit" class="btn btn-primary btn-block btn-flat" value="Login">Login</button>
                        </div>
                        <!-- /.col -->
                    </div>
                </form>

                <!-- <a href="#">I forgot my password</a><br>
                 <a href="register.html" class="text-center">New user? Register</a>-->
                <span class="text-center">New user? Register <a href="register.jsp">here.</span>

            </div>
            <!-- /.login-box-body -->
        </div>



        <script src="lib/bower_components/jquery/dist/jquery.min.js"></script>
        <!-- Bootstrap 3.3.7 -->
        <script src="lib/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    </body>
</html>
