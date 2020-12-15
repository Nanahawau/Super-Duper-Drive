package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Note {

    private By noteTitleBy = By.name("noteTitle");
    private By noteDescriptionBy = By.name("noteDescription");
    private By saveBy = By.name("saveNote");



    protected static WebDriver driver;

    public Note(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Login as valid user
     *
     * @param noteTitle
     * @param noteDesc
     */
    public Note createNote(String noteTitle, String noteDesc) throws InterruptedException {
        JavascriptExecutor jse =(JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement notes = driver.findElement(By.xpath("//a[@href='#nav-notes']"));
        jse.executeScript("arguments[0].click()", notes);

        WebElement addNoteButton = driver.findElement(By.xpath("//button[@id='addNoteButton']"));
        wait.until(ExpectedConditions.elementToBeClickable(addNoteButton)).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(noteTitle);;

        WebElement noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.click();
        noteDescription.sendKeys(noteDesc);
        WebElement noteSubmit = driver.findElement(By.id("save-note-id"));
        noteSubmit.click();
        return new Note(driver);
    }
}
