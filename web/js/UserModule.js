import {authModule} from './AuthModule.js';
class UserModule{
    async printAdminPanel(){
      document.getElementById('context').innerHTML=
      `<h3  class="w-100 my-5 text-center">Панель администратора</h3>
      <div class="w-100 d-flex justify-content-center m-2">
              <form id="form1"action="setRole" method="POST">
                  <p>
                      Список пользователей: 
                      <select id="userId" class="form-select">
                          <option value="" selected>Выберите пользователя</option>
                          
                      </select>
                  </p>
                  <p>
                    Список ролей: 
                    <select id="roleId" class="form-select">
                      <option value="" selected>Выберите роль</option>
                        
                    </select>
                  </p>
                  <p><input id="btnSetRole" class="btn btn-primary w-100" type="submit" value="Назначить роль пользователю"></p>
              </form>`;
      document.getElementById('form1').addEventListener('onsubmit', userModule.setRoleToUser);        
      const listUsersWithRole = await userModule.getListUsersWithRole();
      const selectUserIdOptions = document.getElementById('userId');
      for(let user of listUsersWithRole){
        selectUserIdOptions.add(new Option(user.user.login+', роль: '+user.role, user.id));
      }
      const listRoles = await userModule.getListRoles();
      const selectRoleIdOptions = document.getElementById('roleId');
      for(let role of listRoles){
        selectRoleIdOptions.add(new Option(role.roleName, role.id));
      }
    }
    async getListUsersWithRole(){
      let response = await fetch('listUsersWithRoleJson',{
        method: 'GET',
        headers:{
          'Content-Type': 'aplication/json;charser=utf-8'
        }
      })
      if(response.ok){
        let result = await response.json();
        return result;
      }else{
        document.getElementById('info').innerHTML="Ошибка сервера";
        return null;
      }
    }
    async getListRoles(){
      let response = await fetch('listRolesJson',{
        method: 'GET',
        headers:{
          'Content-Type': 'aplication/json;charser=utf-8'
        }
      })
      if(response.ok){
        let result = await response.json();
        return result;
      }else{
        document.getElementById('info').innerHTML="Ошибка сервера";
        return null;
      }
    }
    setRoleToUser(){
      const userId = document.getElementById('userId').value;
      const roleId = document.getElementById('roleId').value;
      const data ={
        'userId':userId,
        'roleId':roleId
      }
      let response = fetch('setRoleToUserJson',{
        method: 'POST',
        headers:{
          'Content-Type': 'aplication/json;charser=utf-8'
        },
        body: JSON.stringify(data)
      })
    }
    registration(){
        document.getElementById('context').innerHTML=
        `<h3 class="w-100 my-5 text-center">Регистрация пользователя</h3>
        <div class="w-100 d-flex justify-content-center m-2">
          <form action="createUser" method="POST" onsubmit="false">
              <div class="row">
                  <div class="col">
                    <input type="text" id="firstname" class="form-control my-2 g-2" placeholder="Имя" aria-label="Имя">
                  </div>
                  <div class="col">
                    <input type="text" id="lastname" class="form-control my-2 g-2" placeholder="Фамилия" aria-label="Фамилия">
                  </div>
                </div>
                <div class="row">
                  <div class="col">
                    <input type="text" id="phone" class="form-control my-2 g-2" placeholder="Телефон" aria-label="Телефон">
                  </div>
                  <div class="col">
                    <input type="text" id="money" class="form-control my-2 g-2" placeholder="Деньги" aria-label="Деньги">
                  </div>
                </div>
                <div class="row">
                  <div class="col">
                    <input type="text" id="login" class="form-control my-2 g-2" placeholder="Логин" aria-label="Логин">
                  </div>
                  <div class="col">
                    <input type="text" id="password" class="form-control my-2 g-2" placeholder="Пароль" aria-label="Пароль">
                  </div>
                </div>
                <div class="row">
                  <div class="col">
                    <input type="button" id="btnRegistration" class="form-control my-2 g-2 text-white bg-dark" value="Зарегистрировать">
                  </div>
                </div>
          
          </form>
      </div>`;

      document.getElementById('info').innerHTML='';
      document.getElementById('btnRegistration').addEventListener('click', userModule.createUser); 
    }

    async createUser(){
        document.getElementById('info').innerHTML='';
        const firstname = document.getElementById("firstname").value;
        const lastname = document.getElementById("lastname").value;
        const phone = document.getElementById("phone").value;
        const money = document.getElementById("money").value;
        const login = document.getElementById("login").value;
        const password = document.getElementById("password").value;
        const user = {
            "firstname": firstname,
            "lastname": lastname,
            "phone": phone,
            "money": money,
            "login": login,
            "password": password
        };

       const response = await fetch('createUserJson',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
            },
            body: JSON.stringify(user)
        })
        if(response.ok){
          const result = await response.json();
          document.getElementById('info').innerHTML=result.info;
          console.log("Request status: "+result.requestStatus);
          authModule.printLoginForm();
        }else{
          console.log("Ошибка получения данных");
        }
        
    }
    async printListUsers(){
      let result = await userModule.loadListUsers();
      /*result = {
                  requestStatus: 'true',
                  info: 'Список пользователей',
                  listUsers: [
                    {
                      user: { id: 1, 
                              login: 'admin', 
                              customer:{
                                id=1,
                                firstname: 'Juri', 
                                ...}
                            },
                      role: 'ADMIN',
                    },
                    {
                      user: { id: 1, 
                              login: 'ivan', 
                              customer:{
                                id=1,
                                firstname: 'Иван', 
                                ...}
                            },
                      role: 'READER',
                    }
                    
                  ]
      }
      */
      const count = result.listUsers.length;
      let context = document.getElementById('context');
      context.innerHTML='';
      context.insertAdjacentHTML('afterBegin', 
      `<h3 class="w-100 my-5 text-center">Список покупателей</h3>
        <p class="">Всего пользователей: ${count}<p>
        <table id="tableListCustomers" class="table table-striped">
            <thead>
            <th>№</th>
            <th>Id</th>
            <th>Логин</th>
            <th>Имя</th>
            <th>Фамилия</th>
            <th>Телефон</th>
            <th>Деньги (EUR)</th>
            <th>Роль</th>
            <th>Активность</th>
            <th></th>
            </thead>
            <tbody>
            </tbody>
        </table>`);
        let tbody = document.getElementById('tableListCustomers')
                            .getElementsByTagName('tbody')[0]; // элементов может быть несколько
        let i = 1;  
        for(let users of result.listUsers){
          let row = document.createElement('tr');
          let td = document.createElement('td');
          td.appendChild(document.createTextNode(i++));
          row.appendChild(td);
          let userId = null;
          for(let userField in users.user){
            let td = document.createElement('td');
            if(userField === 'id') userId = users.user[userField];
            if(typeof users.user[userField] === 'object'){
              for(let customerField in users.user[userField]){
                if(customerField === 'id') continue;
                td = document.createElement('td');
                if(customerField === 'money'){
                  td.appendChild(document.createTextNode(users.user[userField][customerField]/100));
                  row.appendChild(td);
                }else{
                  td.appendChild(document.createTextNode(users.user[userField][customerField]));
                  row.appendChild(td);
                }
              }
            }else{
              td.appendChild(document.createTextNode(users.user[userField]));
              row.appendChild(td);
            }
          }
          td=document.createElement('td');
          td.appendChild(document.createTextNode(users.role));
          row.appendChild(td);
          td=document.createElement('td');
          td.appendChild(document.createTextNode('Yes'));
          row.appendChild(td);
          td=document.createElement('td');
          let span = document.createElement('span');
          span.classList.add('btn');
          span.classList.add('text-white');
          span.classList.add('bg-dark');
          span.classList.add('p-2');
          span.appendChild(document.createTextNode('Изменить'));
          span.onclick = function(){userModule.changeUser(userId)};
          td.appendChild(span);
          row.appendChild(td);
          tbody.appendChild(row);
        }
    }

    async loadListUsers(){
      let response = await fetch('listUsersJson',{
        method: 'GET',
        headers:{
          'Content-Type': 'aplication/json;charser=utf-8'
        }
      })
      if(response.ok){
        let result = await response.json();
        console.log('listUsers: '+result.listUsers.length);
        return result;
      }else{
        document.getElementById('info').innerHTML="Ошибка сервера";
        return null;
      }
    }
    async changeUser(userId){
      //alert('userId='+userId);
      let data = {'userId' : userId};
      let response = await fetch('getUserJson',{
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8'},
        body: JSON.stringify(data)
      });
      if(response.ok){
        let result = await response.json();
        document.getElementById('info').innerHTML=result.info;
        let user = result.user;
        let money = user.customer.money/100;
        document.getElementById('context').innerHTML=
      `<h3 class="w-100 my-5 text-center">Редактирование профиля пользователя</h3>
        <div class="w-100 d-flex justify-content-center m-2">
          <form action="createUser" method="POST" onsubmit="false">
              <div class="row">
                  <div class="col">
                    <input type="hidden" id="userId" value="${user.id}">
                    <input type="text" id="firstname" class="form-control my-2 g-2" placeholder="Имя" aria-label="Имя" value="${user.customer.firstname}">
                  </div>
                  <div class="col">
                    <input type="text" id="lastname" class="form-control my-2 g-2" placeholder="Фамилия" aria-label="Фамилия" value="${user.customer.lastname}">
                  </div>
                </div>
                <div class="row">
                  <div class="col">
                    <input type="text" id="phone" class="form-control my-2 g-2" placeholder="Телефон" aria-label="Телефон" value="${user.customer.phone}">
                  </div>
                  <div class="col">
                    <input type="text" id="money" class="form-control my-2 g-2" placeholder="Деньги" aria-label="Деньги" value="${money}">
                  </div>
                </div>
                <div class="row">
                  <div class="col">
                    <input type="text" readonly id="login" class="form-control my-2 g-2" placeholder="Логин" aria-label="Логин" value="${user.login}">
                  </div>
                  <div class="col">
                    <input type="text" id="password" class="form-control my-2 g-2" placeholder="Пароль" aria-label="Пароль" value="">
                  </div>
                </div>
                <div class="row">
                  <div class="col">
                    <input type="button" id="btnChangeUser" class="form-control my-2 g-2 text-white bg-dark" value="Изменить">
                  </div>
                </div>
        `;
        
        document.getElementById('btnChangeUser').addEventListener('click', userModule.editUser); 
      }else{
        document.getElementById('info').innerHTML='Ошибка сервера';
      }
    }
    async editUser(){
      let userId = document.getElementById('userId').value;
      let firstname = document.getElementById('firstname').value;
      let lastname = document.getElementById('lastname').value;
      let phone = document.getElementById('phone').value;
      let money = document.getElementById('money').value;
      let login = document.getElementById('login').value;
      let password = document.getElementById('password').value;
      const editUser = {
        'userId': userId,
        'firstname': firstname,
        'lastname': lastname,
        'phone': phone,
        'money': money,
        'login': login,
        'password': password,
      }
      let response = await fetch('editUserJson', {
        method: 'POST',
        headers: {'Content-Type': 'application/json;charset:utf8'},
        body: JSON.stringify(editUser)
      });
      if(response.ok){
        const result = await response.json();
        userModule.changeUser(result.userId);
        document.getElementById('info').innerHTML=result.info;
      }else{
        document.getElementById('info').innerHTML='Ошибка серевера';
      }
    }
}
const userModule = new UserModule();
export {userModule};