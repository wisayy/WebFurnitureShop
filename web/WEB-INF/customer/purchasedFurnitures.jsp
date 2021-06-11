<%-- 
    Document   : listFurnitures
    Created on : 04.12.2020, 10:03:17
    Author     : jvm
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<h3 class="w-100 my-5 text-center">Список купленной кухонной мебели</h3>

<div class="w-100 d-flex justify-content-center m-2">
  <c:forEach var="furniture" items="${listFurnitures}">
    <div class="card m-2 border" style="max-width: 12rem; max-height: 25rem">
        <img src="insertFile/${furniture.cover.path}" class="card-img-top" style="max-width: 12rem; max-height: 15rem" alt="...">
        <div class="card-body">
          <h5 class="card-title">${furniture.name}</h5>
          <p class="card-text">${furniture.author}</p>
          <p class="card-text">${furniture.publishedYear}</p>
          <p class="d-inline">
          </p>
        </div>
    </div>
  </c:forEach>
</div>