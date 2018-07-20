package ccDocStrg;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Muhammad Wannous
 * This Servlet accepts one parameter for the file name and deletes its name from the Datastore
 * (the actual file is not deleted)
 */
public class Delete extends HttpServlet {

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
    String fileName = request.getParameter(Defs.PARAM_FILENAME_STRING);
    //Make sure that the user has already loggedin and that the fileName parameter is not empty/null.
    if (currentUSer != null
            && fileName != null
            && !fileName.equals("")) {
      //Prepare the Datastore service.
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      //We will serach in the 'Files' table for the file name.
      Query fileQuery = new Query(Defs.DATASTORE_KIND_FILES_STRING);
      //Set a filetr on the file name.
      Query.Filter fileFilter = new Query.FilterPredicate(Defs.ENTITY_PROPERTY_FILENAME_STRING,
              Query.FilterOperator.EQUAL, fileName);
      fileQuery.setFilter(fileFilter);
      //Run the query.
      List<Entity> dbFiles = datastore.prepare(fileQuery).asList(FetchOptions.Builder.withDefaults());
      if (!dbFiles.isEmpty()) {
        //If the file name was found then delete it from the Datastore.
        datastore.delete(dbFiles.get(0).getKey());
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
