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
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import config.Defs;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
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
public class MoveToFolder extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, EntityNotFoundException {
        //Prepare the session context.
        HttpSession session = request.getSession(true);
        //Get the user information from the session context.
        User currentUSer = (User) session.getAttribute(Defs.SESSION_USER_STRING);
        //Get the file name from the URL
        long fileId = new Long(request.getParameter("folderId"));
        

        String folderN = request.getParameter("folderName");
        //request.setAttribute("fileid", 0);
        if (currentUSer != null && fileId >= 0 && !folderN.isEmpty()) {
            //Prepare the Datastore service.
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

            //
            String baseFolder = currentUSer.getFirstName() + "" + currentUSer.getUserId();
            //get the file using the key
            Key fileKey = KeyFactory.createKey(Defs.DATASTORE_KIND_FILES_STRING, fileId);
            Entity dbFiles = datastore.get(fileKey);
            String folderName = (String) dbFiles.getProperty(Defs.ENTITY_PROPERTY_FOLDER_NAME);
            String fileN = (String) dbFiles.getProperty(Defs.ENTITY_PROPERTY_FILENAME_STRING);

            //Prepare Google Cloud Storage service.
            GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
                    .initialRetryDelayMillis(10)
                    .retryMaxAttempts(10)
                    .totalRetryPeriodMillis(15000)
                    .build());
            GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
            //Prepare the file name in GCS format.
            //String filename = folderName+"/"+fileNameParam;
            GcsFilename fileNameSource = new GcsFilename(Defs.BUCKET_STRING, folderName + "/" + fileN);
            GcsFilename fileNameDestionation = new GcsFilename(Defs.BUCKET_STRING, baseFolder + "/" + fileN);
            //GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
            //byte[] byteBuffer = new byte[BUFFER_SIZE];
           gcsService.copy(fileNameSource, fileNameDestionation);
           //byte[] buffer = new byte[BUFFER_SIZE];
           //ByteBuffer buf = ByteBuffer.wrap(buffer);
           //gcsService.createOrReplace(fileNameDestionation, instance,buf);
                   

            //Update the database
            dbFiles.setProperty(Defs.ENTITY_PROPERTY_FOLDER_NAME, baseFolder);
            datastore.put(dbFiles);

            session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Files completly moved.");
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
        //processRequest(request, response);
        long fileId = new Long(request.getParameter("fileId"));
        request.setAttribute("fileId", "0");
        if (fileId >= 0) {
            request.setAttribute("fileId", "" + fileId);
        }
        request.getRequestDispatcher("/movetofolder.jsp").forward(request, response);
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
            Logger.getLogger(MoveToFolder.class.getName()).log(Level.SEVERE, null, ex);
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
