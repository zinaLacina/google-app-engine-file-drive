/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import config.Defs;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
public class Trash extends HttpServlet {

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
            throws ServletException, IOException {

        //Prepare the session context.
        HttpSession session = request.getSession(true);
        //Get the user information from the session context.
        User currentUSer = (User) session.getAttribute(Defs.SESSION_USER_STRING);
        //Get the file name from the URL
        String action = request.getParameter("action");
        long fileId = new Long(request.getParameter("fileId"));
        //Make sure that the user has already loggedin and that the fileName parameter is not empty/null.
        if (currentUSer != null
                && action != null
                && !action.equals("")) {

            //Let undo the file
            if (action.equals("undo")) {
                //Prepare the Datastore service.
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

                Key trashKey = KeyFactory.createKey("Trash", fileId);

                Filter filter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, trashKey);
                //We will serach in the 'Files' table for the file name.
                Query fileQuery = new Query(Defs.DATASTORE_KIND_TRASH_STRING);
                //Set a filetr on the file name.
                /*Query.Filter fileFilter = new Query.FilterPredicate(Defs.ENTITY_PROPERTY_FILENAME_STRING,
                    Query.FilterOperator.EQUAL, fileName);*/
                fileQuery.setFilter(filter);
                //Run the query.
                //List<Entity> dbFiles = datastore.prepare(fileQuery).asList(FetchOptions.Builder.withDefaults());
                PreparedQuery pq0 = datastore.prepare(fileQuery);
                Entity result = pq0.asSingleEntity();

                if (result != null) {

                    //Create entity Trah and save over there before remove
                    Entity fichier = new Entity(Defs.DATASTORE_KIND_FILES_STRING);
                    fichier.setProperty(Defs.ENTITY_PROPERTY_FILENAME_STRING, result.getProperty(Defs.ENTITY_PROPERTY_FILENAME_STRING));
                    fichier.setProperty(Defs.ENTITY_PROPERTY_OWNER, result.getProperty(Defs.ENTITY_PROPERTY_OWNER));
                    fichier.setProperty(Defs.ENTITY_PROPERTY_CREATED, result.getProperty(Defs.ENTITY_PROPERTY_CREATED));
                    fichier.setProperty(Defs.ENTITY_PROPERTY_FULLACCESS, result.getProperty(Defs.ENTITY_PROPERTY_FULLACCESS));
                    fichier.setProperty(Defs.ENTITY_PROPERTY_ACCESSREAD, result.getProperty(Defs.ENTITY_PROPERTY_ACCESSREAD));
                    fichier.setProperty(Defs.ENTITY_PROPERTY_FILETYPE, result.getProperty(Defs.ENTITY_PROPERTY_FILETYPE));
                    fichier.setProperty(Defs.ENTITY_PROPERTY_FOLDER, result.getProperty(Defs.ENTITY_PROPERTY_FOLDER));
                    fichier.setProperty(Defs.ENTITY_PROPERTY_PARENT, result.getProperty(Defs.ENTITY_PROPERTY_PARENT));
                    datastore.put(fichier);
                    //If the file name was found then delete it from the Datastore.
                    datastore.delete(result.getKey());

                    session.setAttribute(Defs.SESSION_MESSAGE_STRING, "The file indicated was deleted!");
                    response.sendRedirect(Defs.LIST_PAGE_STRING);
                } else {
                    //There was no such file name.
                    session.setAttribute(Defs.SESSION_MESSAGE_STRING, "No such file!");
                    response.sendRedirect(Defs.LIST_PAGE_STRING);
                }
            }
            //Let us delete
            if (action.equals("delete")) {
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

                Key trashKey = KeyFactory.createKey("Trash", fileId);

                Filter filter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, trashKey);
                //We will serach in the 'Files' table for the file name.
                Query fileQuery = new Query(Defs.DATASTORE_KIND_TRASH_STRING);
                //Set a filetr on the file name.
                /*Query.Filter fileFilter = new Query.FilterPredicate(Defs.ENTITY_PROPERTY_FILENAME_STRING,
                    Query.FilterOperator.EQUAL, fileName);*/
                fileQuery.setFilter(filter);
                //Run the query.
                //List<Entity> dbFiles = datastore.prepare(fileQuery).asList(FetchOptions.Builder.withDefaults());
                PreparedQuery pq0 = datastore.prepare(fileQuery);
                Entity result = pq0.asSingleEntity();
                if (result != null) {
                    datastore.delete(result.getKey());
                    
                    session.setAttribute(Defs.SESSION_MESSAGE_STRING, "The file indicated was deleted!");
                    response.sendRedirect(Defs.LIST_PAGE_STRING);
                } else {
                    //There was no such file name.
                    session.setAttribute(Defs.SESSION_MESSAGE_STRING, "No such file!");
                    response.sendRedirect(Defs.LIST_PAGE_STRING);
                }
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
        processRequest(request, response);
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
        processRequest(request, response);
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
