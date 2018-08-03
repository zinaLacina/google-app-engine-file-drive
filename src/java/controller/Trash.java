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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import config.Defs;
import helper.Help;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
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
            throws ServletException, IOException, EntityNotFoundException {

        //Prepare the session context.
        HttpSession session = request.getSession(true);
        //Get the user information from the session context.
        User currentUSer = (User) session.getAttribute(Defs.SESSION_USER_STRING);
        //Get the file name from the URL
        String action = request.getParameter("action");
        

        //Preparing a GcsService
        //Prepare the GCS service.
        GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
                .initialRetryDelayMillis(10)
                .retryMaxAttempts(10)
                .totalRetryPeriodMillis(15000)
                .build());
        //Butcket name
        String bucket = Defs.BUCKET_STRING;

        //Make sure that the user has already loggedin and that the fileName parameter is not empty/null.
        if (currentUSer != null
                && action != null
                && !action.equals("")) {

            //Let undo the file
            if (action.equals("undo")) {
                long fileId = new Long(request.getParameter("fileId"));
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
                    fichier.setProperty(Defs.ENTITY_PROPERTY_FILESIZE, result.getProperty(Defs.ENTITY_PROPERTY_FILESIZE));
                    fichier.setProperty(Defs.ENTITY_PROPERTY_FAVORITE, result.getProperty(Defs.ENTITY_PROPERTY_FAVORITE));
                    fichier.setProperty(Defs.ENTITY_PROPERTY_FOLDER_NAME, result.getProperty(Defs.ENTITY_PROPERTY_FOLDER_NAME));
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
                long fileId = new Long(request.getParameter("fileId"));
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
                    Key userKey = KeyFactory.createKey(Defs.DATASTORE_KIND_USER_STRING, currentUSer.getUserId());

                    Entity user = datastore.get(userKey);
                    long fileSize = (long) result.getProperty(Defs.ENTITY_PROPERTY_FILESIZE);

                    //Update the remain memory of the user in the database
                    //long resultQuota = (long) user.getProperty(Defs.ENTITY_PROPERTY_QUOTA) + fileSize;
                    long resultQuota = currentUSer.getRemainMemory() + fileSize;
                    user.setProperty(Defs.ENTITY_PROPERTY_QUOTA, resultQuota);
                    datastore.put(user);
                    currentUSer.setRemainMemory(currentUSer.getRemainMemory() + fileSize);
                    session.setAttribute(Defs.SESSION_USER_STRING, currentUSer);
                    String folder = currentUSer.getFirstName()+""+currentUSer.getUserId();
                    try {
                        //delete completely the file
                        String filename = (String) result.getProperty(Defs.ENTITY_PROPERTY_FILENAME_STRING);
                        gcsService.delete(new GcsFilename(bucket, folder + "/" + filename));
                    } catch (IOException e) {
                        //Error handling
                        System.out.println(e);
                    }

                    datastore.delete(result.getKey());
                    //Delete file from the bucket

                    session.setAttribute(Defs.SESSION_MESSAGE_STRING, "The file indicated was deleted!");
                    response.sendRedirect(Defs.LIST_PAGE_STRING);
                } else {
                    //There was no such file name.
                    session.setAttribute(Defs.SESSION_MESSAGE_STRING, "No such file!");
                    response.sendRedirect(Defs.LIST_PAGE_STRING);
                }
            }

            if (action.equals("cleanup")) {
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                long currentUserId = currentUSer.getUserId();
                Filter fileFilter = new Query.FilterPredicate(Defs.ENTITY_PROPERTY_OWNER, Query.FilterOperator.EQUAL, currentUserId);
                Query fileQuery = new Query(Defs.DATASTORE_KIND_TRASH_STRING);
                fileQuery.setFilter(fileFilter);
                ///Get the result
                List<Entity> files = datastore.prepare(fileQuery).asList(FetchOptions.Builder.withDefaults());
                if (!files.isEmpty()) {
                    Iterator<Entity> allFiles = files.iterator();
                    while (allFiles.hasNext()) {
                        Entity log = allFiles.next();
                        Date created = (Date) log.getProperty(Defs.ENTITY_PROPERTY_CREATED);
                        int minutes = Help.minutesDiff(created, new Date());
                        if (minutes > 10) {
                            Key userKey = KeyFactory.createKey(Defs.DATASTORE_KIND_USER_STRING, currentUSer.getUserId());

                            Entity user = datastore.get(userKey);
                            long fileSize = (long) log.getProperty(Defs.ENTITY_PROPERTY_FILESIZE);

                            //Update the remain memory of the user in the database
                            //long resultQuota = (long) user.getProperty(Defs.ENTITY_PROPERTY_QUOTA) + fileSize;
                            long resultQuota = currentUSer.getRemainMemory() + fileSize;
                            user.setProperty(Defs.ENTITY_PROPERTY_QUOTA, resultQuota);
                            datastore.put(user);
                            currentUSer.setRemainMemory(currentUSer.getRemainMemory() + fileSize);
                            session.setAttribute(Defs.SESSION_USER_STRING, currentUSer);
                            String folder = currentUSer.getFirstName()+""+currentUSer.getUserId();
                            try {
                                //delete completely the file
                                String filename = (String) log.getProperty(Defs.ENTITY_PROPERTY_FILENAME_STRING);
                                gcsService.delete(new GcsFilename(bucket, folder + "/" + filename));
                            } catch (IOException e) {
                                //Error handling
                                System.out.println(e);
                            }

                            datastore.delete(log.getKey());
                            //Delete file from the bucket

                            session.setAttribute(Defs.SESSION_MESSAGE_STRING, "The files are deleted!");
                            response.sendRedirect("trash.jsp");
                        }
                    }

                } else {
                    //If the user has not logged in then return him/her to the login page.
                    session.setAttribute(Defs.SESSION_MESSAGE_STRING, "No files in the trash!");
                    response.sendRedirect(Defs.HOME_PAGE_STRING);
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
        try {
            processRequest(request, response);
        } catch (EntityNotFoundException ex) {
            Logger.getLogger(Trash.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Trash.class.getName()).log(Level.SEVERE, null, ex);
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
