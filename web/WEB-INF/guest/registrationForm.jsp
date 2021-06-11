<%-- 
    Document   : page1
    Created on : Nov 25, 2020, 10:14:04 AM
    Author     : Melnikov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
   <h3 class="w-100 my-5 text-center">Регистрация пользователя</h3>
   <div class="w-100 d-flex justify-content-center m-2">
    <form action="createUser" method="POST">
      Имя покупателя <input type="text" name="firstname" value="${user.customer.firstname}"><br>
      Фамилия покупателя <input type="text" name="lastname"  value="${user.customer.lastname}"><br>
      Телефон <input type="text" name="phone"  value="${user.customer.phone}"><br>
      Деньги <input type="text" name="money"  value="${user.customer.money}"><br>
      Логин <input type="text" name="login"  value="${user.login}"><br>
      Пароль <input type="text" name="password" value=""><br>
      <input type="submit" name="submit" value="Зарегистрировать">
    </form>
   </div>
  
