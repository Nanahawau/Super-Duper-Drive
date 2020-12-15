package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class SignUp {

    private By firstNameBy = By.name("firstName");
    private By lastNameBy = By.name("lastName");
    private By usernameBy = By.name("username");
    private By passwordBy = By.name("password");
    private By signupBy = By.name("signup");


    protected static WebDriver driver;

    public SignUp (WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Signup a user
     *
     * @param firstName
     * @param lastName
     * @param username
     * @param password
     * @return Login object
     */
    public Login signUpUser(String firstName, String lastName, String username, String password) {
        driver.findElement(firstNameBy).sendKeys(firstName);
        driver.findElement(lastNameBy).sendKeys(lastName);
        driver.findElement(usernameBy).sendKeys(username);
        driver.findElement(passwordBy).sendKeys(password);
        driver.findElement(signupBy).sendKeys(Keys.ENTER);
        return new Login(driver);
    }

}
