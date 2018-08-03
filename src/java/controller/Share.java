/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import config.Defs;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author lacinazina
 */
public class Share extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, EntityNotFoundException {
        //Prepare the session context.
        HttpSession session = request.getSession(true);
        //Get the user information from the session context.
        User currentUSer = (User) session.getAttribute(Defs.SESSION_USER_STRING);
        //Get the file name from the URL
        String fileId = request.getParameter("fileId");
        String userName = request.getParameter("userName");
        Long userid = new Long(userName);

        //request.setAttribute("fileid", 0);
        if (currentUSer != null && fileId != null) {
            Key fileKey = KeyFactory.createKey(Defs.DATASTORE_KIND_FILES_STRING, new Long(fileId));
            Entity dbFiles = datastore.get(fileKey);
            //Get list of sharing user
            List<Long> fullAccessUsers = (ArrayList<Long>) dbFiles.getProperty(Defs.ENTITY_PROPERTY_FULLACCESS);
            
            if(fullAccessUsers.contains(userid)==false){
                fullAccessUsers.add(userid);
                dbFiles.setProperty(Defs.ENTITY_PROPERTY_FULLACCESS, fullAccessUsers);
                datastore.put(dbFiles);
                session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Successfully shared");
                response.sendRedirect("home.jsp");
                
            }else{
                //If the user has not logged in then return him/her to the login page.
            session.setAttribute(Defs.SESSION_MESSAGE_STRING, "User is already in the sharing list");
            response.sendRedirect("share?fileId="+fileId+"&&useid="+fullAccessUsers.toString());
            }

        } else {
            //If the user has not logged in then return him/her to the login page.
            session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Please login first!");
            response.sendRedirect(Defs.LOGIN_PAGE_STRING);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        String fileId = request.getParameter("fileId");
        request.setAttribute("fileId", fileId);

        request.getRequestDispatcher("/share.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (EntityNotFoundException ex) {
            Logger.getLogger(Share.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
