package controller;

import config.Defs;
import model.User;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Muhammad Wannous This Servlet accepts one parameter for the file name
 * and deletes its name from the Datastore (the actual file is not deleted)
 */
public class Delete extends HttpServlet {

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
        String fileName = request.getParameter(Defs.PARAM_FILENAME_STRING);
        long fileId = new Long(request.getParameter("fileId"));
        //Make sure that the user has already loggedin and that the fileName parameter is not empty/null.
        if (currentUSer != null
                && fileName != null
                && !fileName.equals("")&&fileId>=0) {
            //Prepare the Datastore service.
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            
            //get the file using the key
            Key fileKey = KeyFactory.createKey(Defs.DATASTORE_KIND_FILES_STRING, fileId);
            
            //We will serach in the 'Files' table for the file name.
            //Query fileQuery = new Query(Defs.DATASTORE_KIND_FILES_STRING);
            //Set a filetr on the file name.
            //Query.Filter fileFilter = new Query.FilterPredicate(Defs.ENTITY_PROPERTY_FILENAME_STRING,
                    //Query.FilterOperator.EQUAL, fileName);
            //fileQuery.setFilter(fileFilter);
            //Run the query.
            //List<Entity> dbFiles = datastore.prepare(fileQuery).asList(FetchOptions.Builder.withDefaults());
            Entity dbFiles = datastore.get(fileKey);
            
            if (dbFiles!=null) {
                
                //Create entity Trah and save over there before remove
                Entity trash = new Entity(Defs.DATASTORE_KIND_TRASH_STRING, fileId);
                trash.setProperty(Defs.ENTITY_PROPERTY_FILENAME_STRING, fileName);
                trash.setProperty(Defs.ENTITY_PROPERTY_OWNER, dbFiles.getProperty(Defs.ENTITY_PROPERTY_OWNER));
                trash.setProperty(Defs.ENTITY_PROPERTY_CREATED, dbFiles.getProperty(Defs.ENTITY_PROPERTY_CREATED));
                trash.setProperty(Defs.ENTITY_PROPERTY_FULLACCESS, dbFiles.getProperty(Defs.ENTITY_PROPERTY_FULLACCESS));
                trash.setProperty(Defs.ENTITY_PROPERTY_ACCESSREAD, dbFiles.getProperty(Defs.ENTITY_PROPERTY_ACCESSREAD));
                trash.setProperty(Defs.ENTITY_PROPERTY_FILETYPE, dbFiles.getProperty(Defs.ENTITY_PROPERTY_FILETYPE));
                trash.setProperty(Defs.ENTITY_PROPERTY_FOLDER, dbFiles.getProperty(Defs.ENTITY_PROPERTY_FOLDER));
                trash.setProperty(Defs.ENTITY_PROPERTY_PARENT, dbFiles.getProperty(Defs.ENTITY_PROPERTY_PARENT));
                trash.setProperty(Defs.ENTITY_PROPERTY_FILESIZE, dbFiles.getProperty(Defs.ENTITY_PROPERTY_FILESIZE));
                trash.setProperty(Defs.ENTITY_PROPERTY_FAVORITE, dbFiles.getProperty(Defs.ENTITY_PROPERTY_FAVORITE));
                trash.setProperty(Defs.ENTITY_PROPERTY_FOLDER_NAME, dbFiles.getProperty(Defs.ENTITY_PROPERTY_FOLDER_NAME));
                trash.setProperty(Defs.ENTITY_PROPERTY_TRASH_CREATED_NAME, new Date());
                //fileEntity.setProperty(Defs.ENTITY_PROPERTY_FOLDER_NAME, "none");
                datastore.put(trash);
                //If the file name was found then delete it from the Datastore.
                datastore.delete(dbFiles.getKey());

                session.setAttribute(Defs.SESSION_MESSAGE_STRING, "The file indicated was deleted!");
                response.sendRedirect(Defs.LIST_PAGE_STRING);
            } else {
                //There was no such file name.
                session.setAttribute(Defs.SESSION_MESSAGE_STRING, "No such file!");
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
            Logger.getLogger(Delete.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Delete.class.getName()).log(Level.SEVERE, null, ex);
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
