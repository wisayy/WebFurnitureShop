<%-- 
    Document   : page1
    Created on : Nov 25, 2020, 10:14:04 AM
    Author     : Melnikov
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<p class="w-100 text-center my-5">Добро пожаловать в наш магазин кухонной мебели!</p>
<div class="w-100 d-flex justify-content-center m-2">
    <c:forEach var="furniture" items="${listFurnitures}">
      <div class="card m-2" style="max-width: 12rem; max-height: 18rem" >
        <img src="insertFile/${furniture.cover.path}" class="card-img d-block" alt="..." style="min-width: 10rem; min-height: 10rem">
      </div>
    </c:forEach>
</div>
    
    
  
