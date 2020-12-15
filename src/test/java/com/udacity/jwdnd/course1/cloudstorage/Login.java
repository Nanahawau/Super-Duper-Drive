package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class Login {
    private By usernameBy = By.name("username");
    private By passwordBy = By.name("password");
    private By loginBy = By.name("login");



    protected static WebDriver driver;

    public Login(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Login as valid user
     *
     * @param username
     * @param password
     * @return HomePage object
     */
    public HomePage loginValidUser(String username, String password) throws InterruptedException {
        driver.findElement(usernameBy).sendKeys(username);
        driver.findElement(passwordBy).sendKeys(password);
        driver.findElement(loginBy).sendKeys(Keys.ENTER);
        return new HomePage(driver);
    }


}
