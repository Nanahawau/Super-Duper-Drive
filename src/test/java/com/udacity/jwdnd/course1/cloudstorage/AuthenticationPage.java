package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AuthenticationPage {

    public AuthenticationPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "username")
    private WebElement username;

    @FindBy(id = "password")
    private WebElement password;


    @FindBy(id = "firstName")
    private WebElement firstName;


    @FindBy(id = "lastName")
    private WebElement lastName;

    @FindBy(id = "loginButton")
    private WebElement loginButton;

    @FindBy(id = "signupButton")
    private WebElement signupButton;


    public void login() {
        loginButton.click();
    }

    public void signup() {
        signupButton.click();
    }
}

