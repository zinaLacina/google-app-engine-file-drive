<%-- 
    Document   : register
    Created on : May 24, 2016, 12:36:55 PM
    Author     : Muhammad Wannous
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Cloud storage | Registration</title>
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

            <div class="login-box-body">
                <p class="login-box-msg"><strong>Register</strong></p>

                <form method="post" action="register">
                    <div class="form-group has-feedback">

                        <input class="form-control" type="text" id="userName" name="firstName" placeholder="Fitst name">
                        <span class="glyphicon glyphicon-user form-control-feedback"></span>
                        <small class="text-muted">Required. 2~30 characters </small>
                    </div>
                    <div class="form-group has-feedback">

                        <input class="form-control" type="text" id="userName" name="lastName" placeholder="Last name">
                        <span class="glyphicon glyphicon-user form-control-feedback"></span>
                        <small class="text-muted">Required. 2~30 characters</small>
                    </div>
                    
                    <div class="form-group has-feedback">

                        <input class="form-control" type="email" id="userName" name="userName" placeholder="Username">
                        <span class="glyphicon glyphicon-user form-control-feedback"></span>
                        <small class="text-muted">Required. 5~30 characters in the format <i>name</i>@<i>company</i>.<i>com</i> </small>
                    </div>

                    <div class="form-group has-feedback">

                        <input type="password" class="form-control" placeholder="Password" id="passWord" name="passWord">
                        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                        <small class="text-muted">Required. 7~15 characters.</small>
                    </div>
                    <div class="form-group has-feedback">

                        <input type="password" class="form-control" placeholder="Retype password" id="rePassWord" name="rPassWord">
                        <span class="glyphicon glyphicon-log-in form-control-feedback"></span>
                        <small class="text-muted">Required. Retype the same password for confirmation.</small>
                    </div>
                    <div class="row">
                        <div class="col-xs-8">

                        </div>
                        <!-- /.col -->
                        <div class="col-xs-4">
                            <button type="submit" class="btn btn-primary btn-block btn-flat">Register</button>
                        </div>
                        <!-- /.col -->
                    </div>
                </form>

                <a href="/" class="text-center">Home</a>
            </div>
            <!-- /.form-box -->
        </div>

    </body>
</html>
