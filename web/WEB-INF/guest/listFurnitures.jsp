<%-- 
    Document   : listFurnitures
    Created on : 04.12.2020, 10:03:17
    Author     : jvm
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<h3 class="w-100 my-5 text-center">Список кухонной мебели</h3>

<div class="w-100 d-flex justify-content-center">
  <c:forEach var="furniture" items="${listFurnitures}">
    <div class="card m-2" style="max-width: 12rem; max-height: 25rem; border:0">
        <p class="card-text text-danger w-100 d-flex justify-content-center"><c:if test="${furniture.discount > 0}">Скидка ${furniture.discount}%!</c:if>&nbsp;</p>
        <img src="insertFile/${furniture.cover.path}" class="card-img-top" style="max-width: 12rem; max-height: 15rem" alt="...">
        <div class="card-body">
          <h5 class="card-title m-0">${furniture.kitchenName}</h5>
          <p class="card-text m-0">Материал: ${furniture.material}</p>
          <p class="card-text m-0">Ширина: ${furniture.width} см</p>
          <p class="card-text m-0">Высота: ${furniture.height} см</p>
          <c:if test="${furniture.discount <= 0 || furniture.discountDate > today}">
              <p class="card-text m-0">${furniture.price/100} EUR</p>
          </c:if>
          <c:if test="${furniture.discount > 0 && furniture.discountDate < today}">
              <p class="card-text m-0 text-danger"><span class="text-decoration-line-through text-black-50">${furniture.price/100}</span> ${(furniture.price - furniture.price*furniture.discount/100)/100} EUR</p>
          </c:if>
          <p class="d-inline">
            <a href="addToBasket?furnitureId=${furniture.id}" class="link text-nowrap">В корзину</a>
          </p>
        </div>
    </div>
  </c:forEach>
        
    
</div>