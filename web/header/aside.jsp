<%-- 
    Document   : aside
    Created on : Jul 21, 2018, 11:08:51 PM
    Author     : lacinazina
--%>
<%@page import="helper.Help"%>
<%@page import="model.User"%>
<%@page import="config.Defs"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<% 
User thisUser = (User) session.getAttribute(Defs.SESSION_USER_STRING);
    
    long userId;
    if (thisUser != null) {
        userId = thisUser.getUserId();
        long resultQuota = thisUser.getRemainMemory();
        long totalSize = Defs.DATASTORE_MAX_STORAGE_USER;
        double percentage = ((totalSize-resultQuota)/totalSize)*100;
        String quota = Help.format((totalSize-resultQuota), 2);
%>

<aside class="main-sidebar">

    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">

        <!-- search form (Optional) -->
        <form action="#" method="get" class="sidebar-form">
            <div class="input-group">
                <input type="text" name="q" class="form-control" placeholder="Search...">
                <span class="input-group-btn">
                    <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                    </button>
                </span>
            </div>
        </form>
        <!-- /.search form -->

        <!-- Sidebar Menu -->
        <ul class="sidebar-menu" data-widget="tree">
            <li><a href="home.jsp"><i class="fa fa-folder"></i> <span>All Files</span></a></li>
            <li><a href="recent.jsp"><i class="fa fa-clock-o"></i> <span>Recent</span></a></li>
            <li><a href="favorite.jsp"><i class="fa fa-star"></i> <span>Favorites</span></a></li>
            <li><a href="shareMe.jsp"><i class="fa fa-share"></i> <span>Share with me</span></a></li>
            <li><a href="shareOther.jsp"><i class="fa fa-share"></i> <span>Share with others</span></a></li>
        </ul>
        <ul class="sidebar-menu menu-down">
            <li><a href="trash?action=cleanup" onclick="return confirm('This action will delete all the trash files up to 10 minutes.Are you sure is that you want?')">
                    <i class="fa fa-fire"></i> <span>Clean storage</span></a></li>
            <li><a href="trash.jsp"><i class="fa fa-trash"></i> <span>Trash</span></a></li>
            <li class="progress-size">
                <p><%=quota%> used /100 MB</p>
           
                <div class="progress">
                    <i class="fa fa-adjust"></i>
                    <div class="progress-bar progress-bar-striped bg-success" role="progressbar" style="width:<%=percentage%>%" aria-valuenow="10" aria-valuemin="0" aria-valuemax="300"></div>
                </div>
            </li>
        </ul>
        <!-- /.sidebar-menu -->
    </section>
    <!-- /.sidebar -->
</aside>
<% 
}
%>
