/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsoncovertors;

import entity.Customer;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 *
 * @author jvm
 */
public class JsonCustomerBuilder {
    public JsonObject createJsonCustomer(Customer customer){
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", customer.getId())
                .add("firstname", customer.getFirstname())
                .add("lastname", customer.getLastname())
                .add("phone", customer.getPhone())
                .add("money", customer.getMoney());
        return job.build();
    }
}
