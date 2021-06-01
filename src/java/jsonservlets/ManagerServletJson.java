/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonservlets;

import entity.Furniture;
import entity.Cover;
import entity.Text;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import jsoncovertors.JsonFurnitureBuilder;
import session.FurnitureFacade;
import session.CoverFacade;
import session.TextFacade;

/**
 *
 * @author jvm
 */

@MultipartConfig()
@WebServlet(name = "ManagerServletJson", urlPatterns = {
  "/createFurnitureJson",

})
public class ManagerServletJson extends HttpServlet {
    @EJB private CoverFacade coverFacade;
    @EJB private TextFacade textFacade;
    @EJB private FurnitureFacade furnitureFacade;


    public static final ResourceBundle pathToFile = ResourceBundle.getBundle("property.pathToFile");
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
    String uploadFolder = ManagerServletJson.pathToFile.getString("dir");
    String json = null;
//    JsonReader jsonReader = Json.createReader(request.getReader());
    JsonObjectBuilder job = Json.createObjectBuilder();
    JsonObject jsonObject = null;
    String path = request.getServletPath();
    switch (path) {
        case "/createFurnitureJson":
            List<Part> fileParts = request
                        .getParts()
                        .stream()
                        .filter(part -> "file".equals(part.getName()))
                        .collect(Collectors.toList()
            );
            Set<String> imagesExtension = new HashSet<>();
            imagesExtension.add("jpg");
            imagesExtension.add("png");
            imagesExtension.add("gif");
            String fileFolder = "";
            Furniture furniture = null;
            Cover cover = null;
            Text text = null;
            for(Part filePart : fileParts){
                String fileName = getFileName(filePart);
                String fileExtension = fileName.substring(fileName.length()-3, fileName.length());
                if(imagesExtension.contains(fileExtension)){

                    fileFolder = "images";
                }else{
                    fileFolder = "texts";
                }
                StringBuilder sbFullPathToFile = new StringBuilder();

                sbFullPathToFile.append(uploadFolder)
                        .append(File.separator)
                        .append(fileFolder)
                        .append(File.separator)
                        .append(fileName);
                File file = new File(sbFullPathToFile.toString());
                file.mkdirs();
                try(InputStream fileContent = filePart.getInputStream()){
                    Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                if("images".equals(fileFolder)){
                    cover = new Cover(fileName,sbFullPathToFile.toString());

                    coverFacade.create(cover);
                }else{
                    text = new Text(fileName, sbFullPathToFile.toString());
                    textFacade.create(text);
                }

            }
            if(cover == null || text == null){
                json=job.add("requestStatus", "false")
                    .add("info", "Выберите файл обложки и текст книги")
                    .build()
                    .toString();
                break;   
            }
            String kitchenName = request.getParameter("kitchenName");
            String material = request.getParameter("material");
            String width = request.getParameter("width");
            String height = request.getParameter("height");
            String price = request.getParameter("price");
            if(kitchenName == null || "".equals(kitchenName)
                  || material == null || "".equals(material)
                  || width == null || "".equals(width)
                  || height == null || "".equals(height)
                  || price == null || "".equals(price)
                  ){
                json=job.add("requestStatus", "false")
                        .add("info", "Заполните все поля")
                        .build()
                       .toString();

                break;   
            }
        furniture = new Furniture(kitchenName, material, width, height,  price, cover, text);
        furnitureFacade.create(furniture);
        JsonFurnitureBuilder jbb = new JsonFurnitureBuilder();
        JsonObject jsonBook = jbb.createJsonBook(furniture);
        json=job.add("requestStatus", "true")
                    .add("info", "Добавлена книга \""+furniture.getKitchenName()+"\".")
                    .add("book", jsonBook.toString())
                    .build()
                    .toString();
        response.setContentType("application/json"); 
        break;  

    }
    if(json == null && "".equals(json)){
        json=job.add("requestStatus", "false")
                    .add("info", "Ошибка обработки запроса")
                    .build()
                    .toString();
    }
    try (PrintWriter out = response.getWriter()) {
        out.println(json);
    }
    
  }
private String getFileName(Part part){
    final String partHeader = part.getHeader("content-disposition");
    for (String content : part.getHeader("content-disposition").split(";")){
        if(content.trim().startsWith("filename")){
            return content
                    .substring(content.indexOf('=')+1)
                    .trim()
                    .replace("\"",""); 
        }
    }
    return null;
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
