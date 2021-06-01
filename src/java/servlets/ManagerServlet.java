/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Furniture;
import entity.Cover;
import entity.Text;
import entity.User;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
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
import session.TextFacade;
import session.UserFacade;
import session.UserRolesFacade;
import tools.SheduleDiscount;

/**
 *
 * @author jvm
 */
@WebServlet(name = "ManagerServlet", urlPatterns = {
     "/addFurniture",
    "/createFurniture",
    "/discountForm",
    "/setDiscount",
   
    

})
public class ManagerServlet extends HttpServlet {
    @EJB
    private FurnitureFacade furnitureFacade;
    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private HistoryFacade historyFacade;
    @EJB
    private UserFacade userFacade;
    @EJB private UserRolesFacade userRolesFacade;
    @EJB private CoverFacade coverFacade;
    @EJB private TextFacade textFacade;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, 
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if(session == null){
            request.setAttribute("info", 
                    "У вас нет права использовать этот ресурс. Войдите!");
            request.getRequestDispatcher("/loginForm").forward(request, response);
            return;
        }
        User user = (User) session.getAttribute("user");
        if(user == null){
            request.setAttribute("info", 
                    "У вас нет права использовать этот ресурс. Войдите!");
            request.getRequestDispatcher("/loginForm").forward(request, response);
            return;
        }
        boolean isRole = userRolesFacade.isRole("MANAGER",user);
        if(!isRole){
            request.setAttribute("info", 
            "У вас нет права использовать этот ресурс. Войдите с соответствующими правами!");
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
            case "/addBook":
                List<Cover> listCovers = coverFacade.findAll();
                List<Text> listTexts = textFacade.findAll();
                request.setAttribute("listCovers", listCovers);
                request.setAttribute("listTexts", listTexts);
                request.setAttribute("activeAddBook", "true");
                request.getRequestDispatcher(
                        LoginServlet.pathToFile.getString("addBook"))
                        .forward(request, response);
                break;
            case "/createFurniture":
                String kitchenName = request.getParameter("kitchenName");
                String material = request.getParameter("material");
                String width = request.getParameter("width");
                String height = request.getParameter("height");
                String price = request.getParameter("price");
                String coverId = request.getParameter("coverId");
                String textId = request.getParameter("textId");
                if(coverId==null || "".equals(coverId)  
                       || kitchenName==null || "".equals(kitchenName) 
                       || material==null || "".equals(material) 
                       || width==null || "".equals(width) 
                       || height==null || "".equals(height) 
                       || price==null|| "".equals(price) 
                       || coverId==null || "".equals(coverId) 
                       || textId==null || "".equals(textId)){
                    request.setAttribute("info", "Выберите файл обложки");
                    request.getRequestDispatcher("/addBook")
                            .forward(request, response);
                }
                request.setAttribute("info", 
                        "Добавлена книга "+kitchenName+
                        ", ширина: " + width +
                        ", высота: "+ height
                );
                Cover cover = coverFacade.find(Long.parseLong(coverId));
                Text text = textFacade.find(Long.parseLong(textId));
                Furniture furniture = new Furniture(
                        kitchenName, 
                        material, 
                        width, 
                        height, 
                        price, 
                        cover,
                        text
                );
                furnitureFacade.create(furniture);
                request.getRequestDispatcher("/listFurnitures")
                        .forward(request, response);
                break;
            case "/discountForm":
                request.setAttribute("activeDiscountForm", "true");
                List<Furniture> listFurnitures = furnitureFacade.findNotDiscountBook();
                request.setAttribute("listFurnitures", listFurnitures);
                request.getRequestDispatcher(LoginServlet
                                .pathToFile
                                .getString("discountForm")
                        )
                        .forward(request, response);
                break;
            case "/setDiscount":
                String bookId = request.getParameter("furnitureId");
                String discount = request.getParameter("discount");
                String dateDiscount = request.getParameter("dateDiscount");//format yyyy-mm-dd
                String duration = request.getParameter("duration");
                String durationType = request.getParameter("durationType");
                if(bookId==null || "".equals(bookId)  
                       || discount==null || "".equals(discount) 
                       || dateDiscount==null || "".equals(dateDiscount) 
                       || duration==null || "".equals(duration) 
                       || durationType == null || "".equals(durationType)
                       ){
                    request.setAttribute("info", "Заполние все поля");
                    request.getRequestDispatcher("/discountForm")
                            .forward(request, response);
                }
                furniture = furnitureFacade.find(Long.parseLong(bookId));
                String year = dateDiscount.substring(0,4);
                String month = dateDiscount.substring(5,5+2);
                String day = dateDiscount.substring(8,8+2);
                Calendar cDateDiscount = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
                SheduleDiscount sheduleDiscount = new SheduleDiscount();
                Furniture discountBook = sheduleDiscount.setDiscount(
                        furniture, 
                        Integer.parseInt(discount),
                        cDateDiscount.getTime(), 
                        Integer.parseInt(duration),
                        durationType
                );
                furnitureFacade.edit(discountBook);
                request.getRequestDispatcher("/listFurnitures")
                        .forward(request, response);
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
        return "Servlet for MANAGER";
    }// </editor-fold>
}