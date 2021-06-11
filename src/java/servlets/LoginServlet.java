/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Furniture;
import entity.Customer;
import entity.Role;
import entity.User;
import entity.UserRoles;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import session.FurnitureFacade;
import session.CoverFacade;
import session.HistoryFacade;
import session.CustomerFacade;
import session.RoleFacade;
import session.UserFacade;
import session.UserRolesFacade;
import tools.EncryptPassword;

/**
 *
 * @author jvm
 */
@WebServlet(name = "LoginServlet", loadOnStartup = 1, urlPatterns = {
    "/index",
    "/loginForm",
    "/login",
    "/logout",
    "/registrationForm",
    "/createUser",
    "/listFurnitures",
})
public class LoginServlet extends HttpServlet {
@EJB
    private UserFacade userFacade;
@EJB
    private CustomerFacade customerFacade;
@EJB
    private FurnitureFacade furnitureFacade;
@EJB private RoleFacade roleFacade;
@EJB private UserRolesFacade userRolesFacade;
@EJB private CoverFacade coverFacade;
@EJB private HistoryFacade historyFacade;

@Inject private EncryptPassword encryptPassword;
    

public static final ResourceBundle pathToFile = ResourceBundle.getBundle("property.pathToFile");
        
    @Override
    public void init() throws ServletException {
        if(userFacade.count() > 0) return;
        String salt = encryptPassword.createSalt();
        String password = encryptPassword.createHash("12345", salt);
        Customer customer = new Customer("Juri", "Melnikov", "56569987",1000);
        customerFacade.create(customer);
        User user = new User("admin", password, salt, customer);
        userFacade.create(user);
        Role role = new Role("ADMIN");
        roleFacade.create(role);
        UserRoles userRoles = new UserRoles(user, role);
        userRolesFacade.create(userRoles);
        role = new Role("MANAGER");
        roleFacade.create(role);
        userRoles = new UserRoles(user,role);
        userRolesFacade.create(userRoles);
        role = new Role("CUSTOMER");
        roleFacade.create(role);
        userRoles = new UserRoles(user,role);
        userRolesFacade.create(userRoles);
        
    }



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
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        List<Furniture> purchasedFurnitures = null;
        HttpSession session = request.getSession(false);
        User user=null;
        if(session != null){
            user = (User) session.getAttribute("user");
            List<Furniture> basketList = (List<Furniture>) session.getAttribute("basketList");
            if(basketList != null){
                request.setAttribute("basketListCount", basketList.size());
            }
            if(user != null) purchasedFurnitures = historyFacade.findPurchasedFurniture(user.getCustomer());
        }
        
        request.setAttribute("role", userRolesFacade.getTopRoleForUser(user));
    
        String path = request.getServletPath();
        switch (path) {
            case "/index":
                List<Furniture> listFurnitures = furnitureFacade.findAll();
                request.setAttribute("listFurnitures", listFurnitures);
                request.getRequestDispatcher(LoginServlet.pathToFile.getString("index")).forward(request, response);
                break;
            case "/loginForm":
                request.setAttribute("activeEnter", "true");
                //response.sendRedirect(LoginServlet.pathToFile.getString("login"));
                request.getRequestDispatcher(LoginServlet.pathToFile.getString("login")).forward(request, response);
                break;
            case "/login":
                String login = request.getParameter("login");
                String password = request.getParameter("password");
                if("".equals(login) || login == null
                       || "".equals(password) || password == null){
                    request.setAttribute("info","Заполните все поля");
                    request.getRequestDispatcher("/loginForm").forward(request, response);
                    break;
                }
                user = userFacade.findByLogin(login);
                if(user == null){
                    request.setAttribute("info","Нет такого пользователя");
                    request.getRequestDispatcher("/loginForm").forward(request, response);
                    break;
                }
                String salt = user.getSalt();
                String encryptPwd = encryptPassword.createHash(password, salt);
                if(!encryptPwd.equals(user.getPassword())){
                    request.setAttribute("info","Нет такого пользователя");
                    request.getRequestDispatcher("/loginForm").forward(request, response);
                    break;
                }
                session = request.getSession(true);
                session.setAttribute("user", user);
                request.setAttribute("info","Вы вошли как "+ user.getLogin());
                request.setAttribute("role", userRolesFacade.getTopRoleForUser(user));
                request.getRequestDispatcher("/index").forward(request, response);
                break;
            case "/logout":
                session = request.getSession(false);
                if(session != null){
                   session.invalidate();
                }
                request.setAttribute("basketListCount", 0);
                request.setAttribute("info", "Вы вышли");
                request.setAttribute("role", userRolesFacade.getTopRoleForUser(null));
                request.getRequestDispatcher("/index").forward(request, response);
                break;
            case "/registrationForm":
                request.setAttribute("activeRegistration", "true");
                request.getRequestDispatcher(LoginServlet.pathToFile.getString("registration")).forward(request, response);
                break;
            case "/createUser":
                String firstname = request.getParameter("firstname");
                String lastname = request.getParameter("lastname");
                String phone = request.getParameter("phone");
                String money = request.getParameter("money");
                login = request.getParameter("login");
                password = request.getParameter("password");
                if("".equals(firstname) || firstname == null
                       || "".equals(lastname) || lastname == null
                       || "".equals(phone) || phone == null
                       || "".equals(money) || money == null
                       || "".equals(login) || login == null
                       || "".equals(password) || password == null){
                    request.setAttribute("info","Заполните все поля");
                    request.getRequestDispatcher("/registrationForm").forward(request, response);
                    break;
                }
                
                Customer customer = new Customer(firstname, lastname, phone, money);
                customerFacade.create(customer);
                salt = encryptPassword.createSalt();
                encryptPwd = encryptPassword.createHash(password, salt);
                user = new User(login, encryptPwd, salt, customer);
                userFacade.create(user);
                //Здесь добавим роль пользователю.
                Role roleCustomer = roleFacade.findByName("CUSTOMER");
                UserRoles userRoles = new UserRoles(user, roleCustomer);
                userRolesFacade.create(userRoles);
                request.setAttribute("info", 
                        "Читатель "+user.getLogin()+" добавлен"     
                );
                request.getRequestDispatcher("/index").forward(request, response);
                break; 
            case "/listFurnitures":
                request.setAttribute("activeListFurniture", "true");
                request.setAttribute("today", new Date());
                listFurnitures = null;
                try {
                    listFurnitures = furnitureFacade.findAll();
                    if(purchasedFurnitures != null){
                        listFurnitures.removeAll(purchasedFurnitures);
                    }
                } catch (Exception e) {
                    listFurnitures = new ArrayList<>();
                }
                
                request.setAttribute("listFurnitures", listFurnitures);
                request.getRequestDispatcher(LoginServlet.pathToFile.getString("listFurnitures")).forward(request, response);
                break;    
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
