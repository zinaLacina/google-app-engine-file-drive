package controller;

import config.Defs;
import model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Muhammad Wannous
 * This Servlet accepts 3 parameters for the user name password and password retyping.
 * It uses the parameters to register a new user.
 */
public class Register extends HttpServlet {

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   *
   * This Servlet will receive the registration information (user name, password, and repeat password) from register.jsp
   * page, validate it and save it to GAE Datastore.
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    //Prepare the session context.
    HttpSession session = request.getSession(true);
    //Get the parameters.
    String userName = request.getParameter(Defs.PARAM_USERNAME_STRING);
    String passWord = request.getParameter(Defs.PARAM_PASSWORD_STRING);
    String rPassWord = request.getParameter(Defs.PARAM_RETRYPASSWORD_STRING);
    String firstName = request.getParameter(Defs.ENTITY_PROPERTY_FIRSTNAME_STRING);
    String lastName = request.getParameter(Defs.ENTITY_PROPERTY_LASTNAME_STRING);
    String photo = "";
    //Check the validity of the user name (should be between 5 and 30 characters) and the password
    if (userName.length() <= 30
            && userName.matches("[a-zA-Z0-9]{1,}@[a-zA-Z0-9]{1,}.[a-zA-Z0-9]{1,}")
            && passWord.length() >= 7
            && passWord.length() <= 15
            && passWord.equals(rPassWord)) {
      //The input is valid.
      User newUser = new User(firstName, lastName, userName, passWord);
      //Prepare the Datastore service.
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      //Check if the user name is already used.
      //We will serach in the 'Users' table for a match.
      Query userQuery = new Query(Defs.DATASTORE_KIND_USER_STRING);
      //Set a filter on the user name.
      Query.Filter nameFilter = new Query.FilterPredicate(Defs.ENTITY_PROPERTY_USERNAME_STRING,
              Query.FilterOperator.EQUAL, newUser.getUserName());
      userQuery.setFilter(nameFilter);
      //Run the query.
      List<Entity> dbUsers = datastore.prepare(userQuery).asList(FetchOptions.Builder.withDefaults());
      if (dbUsers.isEmpty()) {
        //The username is vacant.
        Entity userEntity = new Entity(Defs.DATASTORE_KIND_USER_STRING);
        userEntity.setProperty(Defs.ENTITY_PROPERTY_FIRSTNAME_STRING, newUser.getFirstName());
        userEntity.setProperty(Defs.ENTITY_PROPERTY_LASTNAME_STRING, newUser.getLastName());
        userEntity.setProperty(Defs.ENTITY_PROPERTY_USERNAME_STRING, newUser.getUserName());
        userEntity.setProperty(Defs.ENTITY_PROPERTY_PASSWORD_STRING, newUser.getPassWord());
        userEntity.setProperty(Defs.ENTITY_PROPERTY_PHOTO, photo);
        userEntity.setProperty(Defs.ENTITY_PROPERTY_QUOTA, Defs.DATASTORE_MAX_STORAGE_USER);
        userEntity.setProperty(Defs.ENTITY_PROPERTY_DATE_CREATION, new Date());
        //Save the information of the new user in Datastore.
        datastore.put(userEntity);
        //Set an appropriate message in the session context.
        session.setAttribute(Defs.SESSION_MESSAGE_STRING, "Registration completed.");
      } else {
        //The user name has already been used.
        session.setAttribute(Defs.SESSION_MESSAGE_STRING, "The username specified is already in use!");
      }
    } else {
      //The input is not valid.
      session.setAttribute(Defs.SESSION_MESSAGE_STRING, "The username/passowrd specified is not valid!<br>"
      + "The username should be in the format xxxxx@yyyyyy.zzz and the password should be 7 to 15 places.");
    }
    //In all cases, send the user to the log in page.
    response.sendRedirect(Defs.LOGIN_PAGE_STRING);
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
