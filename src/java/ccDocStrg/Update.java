package ccDocStrg;

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
 * This Servlet accepts a number of parameters for the user profile.
 * It uses the parameters to update the user's profile.
 */
public class Update extends HttpServlet {

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
    if (currentUSer != null) {
      //Get the parameters from the request.
      String userName = request.getParameter(Defs.PARAM_USERNAME_STRING);
      String passWord = request.getParameter(Defs.PARAM_PASSWORD_STRING);
      String firstName = request.getParameter(Defs.PARAM_FIRSTNAME_STRING);
      String lastName = request.getParameter(Defs.PARAM_LASTNAME_STRING);
      String newPassWord = request.getParameter(Defs.PARAM_NEWPASSWORD_STRING);
      String rPassWord = request.getParameter(Defs.PARAM_RETRYPASSWORD_STRING);
      //Prepare the Datastore service.
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      //Search for the user in Datastore by using a query.
      Query userQuery = new Query(Defs.DATASTORE_KIND_USER_STRING);
      //Set a proper filter for the user name only.
      Query.Filter nameFilter = new Query.FilterPredicate(Defs.ENTITY_PROPERTY_USERNAME_STRING,
              Query.FilterOperator.EQUAL, userName);
      userQuery.setFilter(nameFilter);
      //Run the query.
      List<Entity> dbUsers = datastore.prepare(userQuery).asList(FetchOptions.Builder.withDefaults());
      if (!dbUsers.isEmpty()) {
        //We have a match.
        Entity userEntity = dbUsers.get(0);
        userEntity.setProperty(Defs.ENTITY_PROPERTY_FIRSTNAME_STRING, firstName);
        userEntity.setProperty(Defs.ENTITY_PROPERTY_LASTNAME_STRING, lastName);
        //Check the validity of the user's input.
        if (!passWord.isEmpty()
                && !newPassWord.isEmpty()
                && !rPassWord.isEmpty()
                && newPassWord.length() >= 7
                && newPassWord.length() <= 15
                && newPassWord.equals(rPassWord)) {
          userEntity.setProperty(Defs.ENTITY_PROPERTY_PASSWORD_STRING, newPassWord);
        }
        datastore.put(userEntity);
        //Save the user information into Datastore.
        currentUSer.setFirstName(firstName);
        currentUSer.setLastName(lastName);
      }
      //Give a proper response message.
      //Send the browser to the page which lists the files.
      session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Profile saved.");
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
