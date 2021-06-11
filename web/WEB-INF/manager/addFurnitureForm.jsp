<%-- 
    Document   : page1
    Created on : Nov 25, 2020, 10:14:04 AM
    Author     : Melnikov
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

    <h3 class="w-100 text-center my-5 ">Добавить новую кухонную мебель</h3>
    <div class="row w-50 mx-auto">
        <a href="uploadCoverForm" class="col-5 offset-4">Загрузить обложку для товара</a>
    </div>
    <form action="createFurniture" method="POST">
      <div class="row w-50 my-2 mx-auto">
        <div class="col-4 text-end">
            Название кухонной мебели 
        </div>
        <div class="col-8 text-start ">
          <input class="w-100" type="text" name="kitchenName">
        </div>
      </div>
      <div class="row w-50 my-2 mx-auto">
        <div class="col-4 text-end">
          Материал 
        </div>
        <div class="col-8 text-start">  
          <input class="col-8" type="text" name="material">
        </div>
      </div>
      <div class="row w-50 my-2 mx-auto">
        <div class="col-4 text-end">   
          Ширина
        </div>
        <div class="col-8 text-start">  
          <input class="col-4" type="text" name="width">
        </div>
      </div>
      <div class="row w-50 my-2 mx-auto">
        <div class="col-4 text-end">   
           Высота 
        </div>
        <div class="col-8 text-start">  
          <input class="col-8" type="text" name="height">
        </div>
      </div>
      <div class="row w-50 my-2 mx-auto">
        <div class="col-4 text-end">   
            Цена: 
        </div>
        <div class="col-8 text-start">  
          <input class="col-4" type="text" name="price">
        </div>
      </div>
      <div class="row w-50 my-2 mx-auto">
        <div class="col-4 text-end">
            Обложка 
        </div>
        <div class="col-8 text-start">     
          <select class="form-select" name="coverId" aria-label="Выбрать обложку">
            <option selected>Open this select menu</option>
            <c:forEach var="cover" items="${listCovers}">
                  <option value="${cover.id}">${cover.description}</option>
            </c:forEach>
          </select>
        </div>
      </div>
      <div class="row w-50 my-2 mx-auto">
        <div class="col-8 text-start mt-3">     
          <input class="w-50 bg-dark text-white" type="submit" name="submit" value="Добавить">
        </div>
      </div>
    </form>
 