<%-- 
    Document   : discountForm
    Created on : Apr 14, 2021, 12:35:13 PM
    Author     : jvm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<h3 class="w-100 my-5 text-center">Скидка для товаров кухонной мебели</h3>
    <div class="w-50 d-flex justify-content-center mx-auto">
    <form action="setDiscount" class="row g-2" method="POST">
        <div class="col-md-6">
          <label for="listFurnitures" class="form-label">Выберите товар кухонной мебели</label>
          <select id="listFurnitures" name="furnitureId" class="form-select" >
              <c:forEach var="furniture" items="${listFurnitures}">
                  <option value="${furniture.id}">${furniture.kitchenName}. ${furniture.material}. ${furniture.width}. ${furniture.height}</option>
              </c:forEach>
          </select>
        </div>
        <div class="col-md-6">
          <label for="discount" class="form-label">Процент скидки</label>
          <input type="text" class="form-control" id="discount" name="discount"  value="">
        </div>
        <div class="col-md-6">
          <label for="dateDiscount" class="form-label">Начало скидки</label>
          <input type="date" value="" class="form-control" id="dateDiscount" name="dateDiscount">
        </div>
        <div class="col-md-6">
          <label for="duration" class="form-label">Длительность скидки</label>
          <input type="text" class="form-control" id="duration" name="duration"  value="">
        </div>
        <div class="col-md-6">
          <label for="durationType" class="form-label">Изменение длительности</label>
          <select id="durationType" name="durationType" class="form-select">
              <option value="MINUTE">Минута</option>
              <option value="HOUR">Час</option>
              <option value="DAY">День</option>
          </select>
        </div>
        <div class="col-6 text-center">
            <label for="durationType" class="form-label">&nbsp;</label>
            <input type="submit" class="btn btn-primary form-control" name="submit" value="Назначить скидку">
        </div>
    </form>
</div>