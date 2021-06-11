/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Furniture;
import entity.History;
import entity.Customer;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import session.FurnitureFacade;
import session.HistoryFacade;
import session.CustomerFacade;
import session.UserFacade;
import session.UserRolesFacade;

/**
 *
 * @author Melnikov
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {
    "/addToBasket",
    "/removeFurnitureFromBasket",
    "/showBasket",
    "/buyFurnitures",
    "/purchasedFurnitures",
    "/editProfile",
    "/changeProfile",

})
public class CustomerServlet extends HttpServlet {
    @EJB
    private FurnitureFacade furnitureFacade;
    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private HistoryFacade historyFacade;
    @EJB
    private UserFacade userFacade;
    @EJB private UserRolesFacade userRolesFacade;
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
        HttpSession session = request.getSession(false);
        if(session == null){
            request.setAttribute("info", "У вас нет права использовать этот ресурс. Войдите!");
            request.getRequestDispatcher("/loginForm").forward(request, response);
            return;
        }
        User user = (User) session.getAttribute("user");
        if (user == null){
            request.setAttribute("info", "У вас нет права использовать этот ресурс. Войдите!");
            request.getRequestDispatcher("/loginForm").forward(request, response);
            return;
        }
        boolean isRole = userRolesFacade.isRole("CUSTOMER",user);
        if(!isRole){
            request.setAttribute("info", "У вас нет права использовать этот ресурс. Войдите с соответствующими правами!");
            request.getRequestDispatcher("/loginForm").forward(request, response);
            return;
        }
        request.setAttribute("role", userRolesFacade.getTopRoleForUser(user));
        List<Furniture> basketList = (List<Furniture>) session.getAttribute("basketList");
        if(basketList != null){
            request.setAttribute("basketListCount", basketList.size());
        }
        String path = request.getServletPath();
        switch (path) {
            case "/addToBasket":
                String furnitureId = request.getParameter("furnitureId");
                if("".equals(furnitureId) || furnitureId==null){
                    request.setAttribute("info", "Что то пошло не так");
                    request.getRequestDispatcher("/listFurnitures").forward(request, response);
                    break;
                }
                Furniture furniture = furnitureFacade.find(Long.parseLong(furnitureId));
                basketList = (List<Furniture>) session.getAttribute("basketList");
                if(basketList == null) basketList = new ArrayList<>();
                basketList.add(furniture);
                session.setAttribute("basketList", basketList);
                request.setAttribute("basketListCount", basketList.size());
                request.getRequestDispatcher("/listFurnitures").forward(request, response);
                break;
            case "/removeFurnitureFromBasket":
                furnitureId = request.getParameter("furnitureId");
                if("".equals(furnitureId) || furnitureId==null){
                    request.setAttribute("info", "Что то пошло не так");
                    request.getRequestDispatcher("/showBasket").forward(request, response);
                    break;
                }
                furniture = furnitureFacade.find(Long.parseLong(furnitureId));
                basketList = (List<Furniture>) session.getAttribute("basketList");
                if(basketList.contains(furniture)){
                    basketList.remove(furniture);
                    session.setAttribute("basketList", basketList);
                }
                request.setAttribute("basketListCount", basketList.size());
                request.getRequestDispatcher("/showBasket").forward(request, response);
                break;
            case "/showBasket":
                List<Furniture> listFurnituresInBasket = (List<Furniture>) session.getAttribute("basketList");
                request.setAttribute("today", new Date());
                request.setAttribute("listFurnituresInBasket", listFurnituresInBasket);
                if(listFurnituresInBasket == null || listFurnituresInBasket.isEmpty()){
                    request.getRequestDispatcher("/listFurnitures").forward(request, response);
                    break;
                }
                request.getRequestDispatcher(LoginServlet.pathToFile.getString("showBasket")).forward(request, response);
                break;
            case "/buyFurnitures":
                user = userFacade.find(user.getId());
                //Получаем список книг в корзине из сессии
                listFurnituresInBasket = (List<Furniture>) session.getAttribute("basketList");
                //Получаем массив отмеченных для покупки книг в корзине или нажатия ссылки при прочтении отрывка
                String[] selectedFurnitures = request.getParameterValues("selectedFurnitures");
                if(selectedFurnitures == null){
                    request.setAttribute("info", "Чтобы купить выберите кухонную мебель.");
                    request.getRequestDispatcher("/listFurnitures").forward(request, response);
                    break;
                }
                int userMoney = user.getCustomer().getMoney();
                Calendar c = new GregorianCalendar();
                List<Furniture> buyFurnitures = new ArrayList<>();
                int totalPricePurchase = 0;
                //Считаем стоимость покупаемых книг, которые отмечены в корзине
                for(String selectedFurnitureId : selectedFurnitures){
                    Furniture furn = furnitureFacade.find(Long.parseLong(selectedFurnitureId));
                    long today = c.getTimeInMillis();
                    if(furn.getDiscountDate() == null){
                        totalPricePurchase += furn.getPrice();
                    }else{
                       if( today > furn.getDiscountDate().getTime()){
                        totalPricePurchase += furn.getPrice() - furn.getPrice()*furn.getDiscount()/100;
                    }
                    buyFurnitures.add(furn);
                }
                }
                if(userMoney < totalPricePurchase){
                    request.setAttribute("info", "Недостаточно денег для покупки");
                    request.getRequestDispatcher("/listFurnitures").forward(request, response);
                    break;
                }
                //Покупаем книгу
                for(Furniture buyFurniture : buyFurnitures){
                    if(listFurnituresInBasket != null) {
                        listFurnituresInBasket.remove(buyFurnitures);
                        historyFacade.create(new History(buyFurniture,user.getCustomer(), new GregorianCalendar().getTime()));
                    } //если запрос пришел из корзины - удаляем из корзины купленную книгу
                }
                //Списываем у читателя деньги за купленные книги
                Customer cust = customerFacade.find(user.getCustomer().getId());
                cust.setMoney(cust.getMoney()-totalPricePurchase);
                customerFacade.edit(cust);
                //Редактируем данные вошедшего читателя в сессии
                User furnUser = userFacade.find(user.getId());
                session.setAttribute("user", furnUser);
                userFacade.edit(furnUser);
                if(listFurnituresInBasket != null){
                    //есои запрос из корзины
                    request.setAttribute("listFurnituresInBasket", listFurnituresInBasket);
                    request.setAttribute("basker", listFurnituresInBasket.size());
                }
                request.setAttribute("info", "Куплено кухонная мебели: "+selectedFurnitures.length);
                request.getRequestDispatcher("/listFurnitures").forward(request, response);
                break;
            case "/purchasedFurnitures":
                request.setAttribute("activePurchasedFurnitures", "true");
                List<Furniture> purchasedFurnitures = historyFacade.findPurchasedFurniture(user.getCustomer());
                request.setAttribute("listFurnitures", purchasedFurnitures);
                request.getRequestDispatcher(LoginServlet.pathToFile.getString("purchasedFurnitures")).forward(request, response);
                break;
            case "/editProfile":
                user = (User) session.getAttribute("user");
                request.setAttribute("user", user);
                request.getRequestDispatcher(LoginServlet.pathToFile.getString("editProfile")).forward(request, response);
                break;
            case "/changeProfile":
                User pUser = userFacade.find(user.getId());
                Customer pCustomer = customerFacade.find(user.getCustomer().getId());
                String firstname = request.getParameter("firstname");
                if(pCustomer != null && !"".equals(firstname)) pCustomer.setFirstname(firstname);
                String lastname = request.getParameter("lastname");
                if(pCustomer != null && !"".equals(lastname)) pCustomer.setLastname(lastname);
                String phone = request.getParameter("phone");
                if(pCustomer != null && !"".equals(phone)) pCustomer.setPhone(phone);
                String money = request.getParameter("money");
                if(pCustomer != null && !"".equals(money)) pCustomer.setMoney(money);
//                String login = request.getParameter("login");
//                if(pUser != null && !"".equals(login)) pUser.setLogin(login);
                String password = request.getParameter("password");
                if(pUser != null && !"".equals(password)){
                    //здесь шифруем пароль и получаем соль
                    pUser.setPassword(password);
                    //user.setSalt(salt);
                    
                }
                customerFacade.edit(pCustomer);
                pUser.setCustomer(pCustomer);
                userFacade.edit(pUser);
                session.setAttribute("user", null);//эта строка может быть избыточной
                session.setAttribute("user", pUser);
                session.setAttribute("info", "Профиль читателя изменен");
                request.getRequestDispatcher("/editProfile").forward(request, response);
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
