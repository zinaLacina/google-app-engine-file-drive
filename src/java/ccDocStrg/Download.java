package ccDocStrg;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Muhammad Wannous
 * This Servlet accepts one parameter for the file name and downloads the corresponding file to the client.
 */
public class Download extends HttpServlet {

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
    //Get the user information from the session context.
    User currentUSer = (User) session.getAttribute(Defs.SESSION_USER_STRING);
    //Get the file name from the URL
    String fileNameParam = request.getParameter(Defs.PARAM_FILENAME_STRING);
    //Make sure that the user has already loggedin and that the fileName parameter is not empty/null.
    if (currentUSer != null
            && fileNameParam != null
            && !fileNameParam.equals("")) {
      //Prepare Google Cloud Storage service.
      GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
              .initialRetryDelayMillis(10)
              .retryMaxAttempts(10)
              .totalRetryPeriodMillis(15000)
              .build());
      //Prepare the file name in GCS format.
      GcsFilename fileName = new GcsFilename(Defs.BUCKET_STRING, fileNameParam);
      GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
      byte[] byteBuffer = new byte[BUFFER_SIZE];
      ServletOutputStream outStream;
      //Set the necessary headers in the response.
      try (DataInputStream in = new DataInputStream(Channels.newInputStream(readChannel))) {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameParam + "\"");
        int length = 0;
        outStream = response.getOutputStream();
        //Read the contents from GCS and write them to the output stream (the response).
        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
          outStream.write(byteBuffer, 0, length);
        }
      }
      //Close the output stream.
      outStream.close();
      //Set a confirmation message in the session (won't be displayed because the 
      //Servlet does not display any thing in the browser).
      //Return to the page which lists files.
      session.setAttribute(Defs.SESSION_MESSAGE_STRING, "File downloaded.");
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
