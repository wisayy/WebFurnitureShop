/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsoncovertors;

import entity.Furniture;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author jvm
 */
public class JsonFurnitureBuilder {
    public JsonObject createJsonBook(Furniture furniture){
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", furniture.getId())
                .add("kitchenName", furniture.getKitchenName())
                .add("material", furniture.getMaterial())
                .add("width", furniture.getWidth())
                .add("height", furniture.getHeight())
                .add("price", furniture.getPrice())
                .add("cover", new JsonCoverBuilder().createJsonCover(furniture.getCover()))
                .add("text", new JsonTextBuilder().createJsonText(furniture.getText()))
                .add("discount", furniture.getDiscount())
                .add("discountDuration", furniture.getDiscountDuration());
        if(furniture.getDiscountDate() == null){
            job.add("discountDate", "null");
        }else{
            job.add("discountDate", furniture.getDiscountDate().getTime());
        }
                
        return job.build();
    }
}
