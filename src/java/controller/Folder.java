/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import config.Defs;
import config.GsHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
public class Folder extends HttpServlet {

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
        HttpSession session = request.getSession(true);

        //Get the user information from the session context.
        User currentUSer = (User) session.getAttribute(Defs.SESSION_USER_STRING);
        List<Long> fullAccess = new ArrayList<>();
        List<Long> readAccess = new ArrayList<>();
        String folderName = request.getParameter("folderName");
        GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
                .initialRetryDelayMillis(10)
                .retryMaxAttempts(10)
                .totalRetryPeriodMillis(15000)
                .build());
        GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
        //Butcket name
        String bucket = Defs.BUCKET_STRING;
        if (currentUSer != null&&!folderName.isEmpty()) {
            //System.out.println(fileUpload.getFileSizeMax());
            //File size controle
            fullAccess.add(currentUSer.getUserId());
            readAccess.add(currentUSer.getUserId());
            

            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

            Entity fileEntity = new Entity(Defs.DATASTORE_KIND_FILES_STRING);
            fileEntity.setProperty(Defs.ENTITY_PROPERTY_FILENAME_STRING, folderName);
            fileEntity.setProperty(Defs.ENTITY_PROPERTY_OWNER, currentUSer.getUserId());
            fileEntity.setProperty(Defs.ENTITY_PROPERTY_CREATED, new Date());
            fileEntity.setProperty(Defs.ENTITY_PROPERTY_FULLACCESS, fullAccess);
            fileEntity.setProperty(Defs.ENTITY_PROPERTY_ACCESSREAD, readAccess);
            fileEntity.setProperty(Defs.ENTITY_PROPERTY_FILETYPE, "none");
            fileEntity.setProperty(Defs.ENTITY_PROPERTY_FILESIZE, 0);
            fileEntity.setProperty(Defs.ENTITY_PROPERTY_FOLDER, 1);
            fileEntity.setProperty(Defs.ENTITY_PROPERTY_PARENT, 0);
            fileEntity.setProperty(Defs.ENTITY_PROPERTY_FAVORITE, 0);
            fileEntity.setProperty(Defs.ENTITY_PROPERTY_FOLDER_NAME, "none");
            

            //GcsFilename fileName = new GcsFilename(bucket, currentUSer.getUserName()+"/"+ folderName);
            //gcsService.createOrReplace(fileName, instance);
            //No need for filters.
            String folderNa = currentUSer.getUserName()+"/"+ folderName;
            GsHelper.createFolder(folderNa);
            
            datastore.put(fileEntity);

            session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Folder successfully created.");
            response.sendRedirect(Defs.LIST_PAGE_STRING);

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
