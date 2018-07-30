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
public class Favoris extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, EntityNotFoundException {
        //Prepare the session context.
        HttpSession session = request.getSession(true);
        //Get the user information from the session context.
        User currentUSer = (User) session.getAttribute(Defs.SESSION_USER_STRING);
        //Get the file name from the URL
        String action = request.getParameter("action");
        long fileId = new Long(request.getParameter("fileId"));
        if (currentUSer != null
                && action != null
                && !action.equals("")) {
            if (fileId != 0) {
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                Key fileKey = KeyFactory.createKey(Defs.DATASTORE_KIND_FILES_STRING, currentUSer.getUserId());
                //Favoriting
                if (action.equals("favorite")) {
                    //Update the remain memory of the user in the database

                    Entity files = datastore.get(fileKey);
                    files.setProperty(Defs.ENTITY_PROPERTY_FAVORITE, 1);
                    datastore.put(files);
                    response.sendRedirect(Defs.LIST_PAGE_STRING);
                }

                //Unfavoriting
                if (action.equals("unfavorite")) {
                    //Update the remain memory of the user in the database

                    Entity files = datastore.get(fileKey);
                    files.setProperty(Defs.ENTITY_PROPERTY_FAVORITE, 0);
                    datastore.put(files);
                    response.sendRedirect(Defs.LIST_PAGE_STRING);
                }

            } else {
                session.setAttribute(Defs.SESSION_MESSAGE_STRING, "The File doesnt exist.");
                response.sendRedirect(Defs.LIST_PAGE_STRING);
            }

        } else {
            //If the user has not logged in then return him/her to the login page.
            session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Please login firt!");
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
        try {
            processRequest(request, response);
        } catch (EntityNotFoundException ex) {
            Logger.getLogger(Favorite.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            Logger.getLogger(Favorite.class.getName()).log(Level.SEVERE, null, ex);
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
