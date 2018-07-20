package ccDocStrg;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Muhammad Wannous
 * This Servlet accepts a file from the user and saves it to Google Cloud Storage.
 * It uses the parameters to register a new user.
 */
public class Upload extends HttpServlet {

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  private static final int BUFFER_SIZE = 2 * 1024 * 1024;

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    //Prepare the session context.
    HttpSession session = request.getSession(true);
    //Prepare a special Servlet to handle a multi-part file upload request.
    ServletFileUpload fileUpload = ServletFileUpload.isMultipartContent(request)
            ? new ServletFileUpload() : null;
    //Get the user information from the session context.
    User currentUSer = (User) session.getAttribute(Defs.SESSION_USER_STRING);
    //Make sure that the user has already loggedin and that the file uploaded is not null.
    if (currentUSer != null && fileUpload != null) {
      //Prepare the GCS service.
      GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
              .initialRetryDelayMillis(10)
              .retryMaxAttempts(10)
              .totalRetryPeriodMillis(15000)
              .build());
      //Start preparing the file object.
      GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
      FileItemIterator fileItemIterator;
      try {
        fileItemIterator = fileUpload.getItemIterator(request);
        //Get the actual file name from the request.
        if (fileItemIterator.hasNext()) {
          FileItemStream fileItem = fileItemIterator.next();
          String fileNameparam = fileItem.getName();
          //Prepare the file name in GCS format.
          GcsFilename fileName = new GcsFilename(Defs.BUCKET_STRING, fileNameparam);
          GcsOutputChannel outputChannel;
          //Read the contents from the request and send them to GCS.
          outputChannel = gcsService.createOrReplace(fileName, instance);
          copy(fileItem.openStream(), Channels.newOutputStream(outputChannel));
          //Prepare Datastore service to save the file name in it.
          DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
          //We will use the table 'Files' to save the file name.
          Entity fileEntity = new Entity(Defs.DATASTORE_KIND_FILES_STRING);
          fileEntity.setProperty(Defs.ENTITY_PROPERTY_FILENAME_STRING, fileNameparam);
          //No need for filters.
          datastore.put(fileEntity);
          //Place a suitable message in the session context and redirect the browser to the page which
          //lists the files.
          session.setAttribute(Defs.SESSION_MESSAGE_STRING, "File upload completed.");
          response.sendRedirect(Defs.LIST_PAGE_STRING);
        }
      } catch (FileUploadException ex) {
        //There was a problem in uploading the contents.
        ex.printStackTrace(System.out);
        session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Problem in uploading the file!");
        response.sendRedirect(Defs.LOGIN_PAGE_STRING);
      }
    } else {
      //If the user has not logged in then return him/her to the login page.
      session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Please login firt!");
      response.sendRedirect(Defs.LOGIN_PAGE_STRING);
    }
  }

  private void copy(InputStream input, OutputStream output) throws IOException {
    try {
      byte[] buffer = new byte[BUFFER_SIZE];
      int bytesRead = input.read(buffer);
      while (bytesRead != -1) {
        output.write(buffer, 0, bytesRead);
        bytesRead = input.read(buffer);
      }
    } finally {
      input.close();
      output.close();
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
