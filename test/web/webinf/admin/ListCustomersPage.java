/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.webinf.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 *
 * @author jvm
 */
public class ListCustomersPage {
    protected WebDriver driver;
    private final By tablelistfurnituresBy = By.id("tableListFurnitures");
    private final By tagA_By = By.tagName("a");
    public ListCustomersPage(WebDriver driver) {
        this.driver = driver;
    }

    public EditUserPage getEditUserPage(String login) {
        WebElement table = driver.findElement(tablelistfurnituresBy);
        WebElement tr = table.findElement(By.xpath("//tr[td[text()='"+login+"']]"));
        tr.findElement(tagA_By).click();
        return new EditUserPage(driver);
    }

   
    
}
