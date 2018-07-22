package controller;

import config.Defs;
import model.User;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Muhammad Wannous
 * This Servlet accepts two parameters for the user name password
 * It uses the parameters to validate a registered user.
 */
public class Validate extends HttpServlet {

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
    //Get the user name and password from the request.
    String userName = request.getParameter(Defs.PARAM_USERNAME_STRING);
    String passWord = request.getParameter(Defs.PARAM_PASSWORD_STRING);
    //See if we have a corresponding entity in datastore.
    if (!userName.isEmpty() && !passWord.isEmpty()) {
      //Prepare the Datastore service.
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      //We will serach in the 'Users' table for a match.
      Query userQuery = new Query(Defs.DATASTORE_KIND_USER_STRING);
      //Set two filetrs on the user name and password
      Query.Filter nameFilter = new Query.FilterPredicate(Defs.ENTITY_PROPERTY_USERNAME_STRING,
              Query.FilterOperator.EQUAL, userName);
      Query.Filter passwordFilter = new Query.FilterPredicate(Defs.ENTITY_PROPERTY_PASSWORD_STRING,
              Query.FilterOperator.EQUAL, passWord);
      Query.Filter userFilter = Query.CompositeFilterOperator.and(nameFilter, passwordFilter);
      userQuery.setFilter(userFilter);
      //Run the query.
      List<Entity> dbUsers = datastore.prepare(userQuery).asList(FetchOptions.Builder.withDefaults());
      if (!dbUsers.isEmpty()) {
        //We have a match.
        Entity userEntity = dbUsers.get(0);
        User user = new User((String) userEntity.getProperty(Defs.ENTITY_PROPERTY_FIRSTNAME_STRING),
                (String) userEntity.getProperty(Defs.ENTITY_PROPERTY_LASTNAME_STRING),
                (String) userEntity.getProperty(Defs.ENTITY_PROPERTY_USERNAME_STRING),
                "");
        //Set the user information in the session context for future requests.
        //The password is not included.
        session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Welcome, " + user.getUserName());
        session.setAttribute(Defs.SESSION_USER_STRING, user);
        //Send the user to the page which lists the files.
        response.sendRedirect(Defs.HOME_PAGE_STRING);
      } else {
        //There was no match in the 'Users'!
        //Take the user back to the log in page.
        session.setAttribute(Defs.SESSION_MESSAGE_STRING, "We couldn't match your input to a registered user!");
        response.sendRedirect(Defs.LOGIN_PAGE_STRING);
      }
    } else {
      //Invalid input 
      session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Check your input!");
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
