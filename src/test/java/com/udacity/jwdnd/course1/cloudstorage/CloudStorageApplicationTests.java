package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.aspectj.weaver.ast.Not;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import sun.rmi.runtime.Log;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;
	private static String userName = "admin";
	private static String password = "admin";
	private static String credURL = "google.com";
	private static WebDriver driver;

	private AuthenticationPage authenticationPage;




	@BeforeEach
	public void beforeEach() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		Thread.sleep(3000);
	}

	@AfterEach
	public void afterEach() throws InterruptedException {
		Thread.sleep(3000);
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * No user was created before trying to reach the home page.
	 * You won't be able to reach home page because it's authenticated
	 * Instead it takes you back to login.
	 *
	 */
	@Test
	public void getUnauthenticated() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
		Thread.sleep(1000);
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		Thread.sleep(1000);
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}


	/**
	 * This test signs up a user,
	 * logs in,
	 * verifies that the homepage is accessible,
	 * logouts,
	 * and finally verifies that the home is inaccessible.
	 *
	 */
	@Test
	public void userCreationWorkFlow() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/signup");
		SignUp signUpPage = new SignUp(driver);
		Login login1 = signUpPage.signUpUser("Nana", "Hawau", "Nana", "Nana");
		Assertions.assertEquals(driver.getTitle(), "Login");

		Thread.sleep(1000);

		driver.get("http://localhost:" + this.port + "/login");

		Login login = new Login(driver);
		HomePage homePage = login.loginValidUser("Nana", "Nana");
		Assertions.assertEquals(driver.getTitle(), "Home");

		Thread.sleep(2000);

		//Click on logout Button
		driver.findElement(By.name("logout")).sendKeys(Keys.ENTER);

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals(driver.getTitle(), "Home");
	}




	@Test
	public void createNote() throws InterruptedException {
		String noteTitle = "new";
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, 30);
		driver.get("http://localhost:" + this.port + "/login");

		Login login = new Login(driver);
		HomePage homePage = login.loginValidUser("admin", "admin");

		WebElement notes = driver.findElement(By.xpath("//a[@href='#nav-notes']"));
		jse.executeScript("arguments[0].click()", notes);

		WebElement addNoteButton = driver.findElement(By.xpath("//button[@id='addNoteButton']"));
		wait.until(ExpectedConditions.elementToBeClickable(addNoteButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(noteTitle);;

		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("Note creation test");
		WebElement noteSubmit = driver.findElement(By.id("save-note-id"));
		noteSubmit.click();

		driver.get("http://localhost:" + this.port + "/result?error=false&success=true");

		driver.findElement(By.xpath("//a[contains(.,'here')]")).click();

		//check for note
		driver.get("http://localhost:" + this.port + "/home");
	  WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
		Boolean created = false;
		for (int i=0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			if (element.getAttribute("innerHTML").equals(noteTitle)) {
				System.out.println("got here 2 too");
				created = true;
				break;
			}
		}
		Assertions.assertTrue(created);
	}


	@Test
	public void updateNote () throws InterruptedException {

		String noteTitle = "new";
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, 30);
		driver.get("http://localhost:" + this.port + "/login");

		Login login = new Login(driver);
		HomePage homePage = login.loginValidUser("admin", "admin");

		WebElement notes = driver.findElement(By.xpath("//a[@href='#nav-notes']"));
		jse.executeScript("arguments[0].click()", notes);

		//Create new note
		WebElement addNoteButton = driver.findElement(By.xpath("//button[@id='addNoteButton']"));
		wait.until(ExpectedConditions.elementToBeClickable(addNoteButton)).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(noteTitle);;

		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("Note creation test");
		WebElement noteSubmit = driver.findElement(By.id("save-note-id"));
		noteSubmit.click();

		driver.get("http://localhost:" + this.port + "/result?error=false&success=true");

		driver.findElement(By.xpath("//a[contains(.,'here')]")).click();

		//Find Note Nav Link
		driver.get("http://localhost:" + this.port + "/home");
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);

		//Click on Edit Note
		WebElement editButton = driver.findElement(By.xpath("//button[@id='edit']"));
		wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
		WebElement newTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
		newTitle.clear();
		newTitle.sendKeys("I'm edited");;


		noteSubmit = driver.findElement(By.id("save-note-id"));
		noteSubmit.click();


		driver.get("http://localhost:" + this.port + "/result?error=false&success=true");

		driver.findElement(By.xpath("//a[contains(.,'here')]")).click();


		driver.get("http://localhost:" + this.port + "/home");
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);




		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
		Boolean edited = false;
		for (int i=0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			if (element.getAttribute("innerHTML").equals("I'm edited")) {
				System.out.println("got here 2 too");
				edited = true;
				break;
			}
		}
		Assertions.assertTrue(edited);
	}


	@Test
	public void deleteNote () throws InterruptedException {
		String noteTitle = "new";
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, 30);
		driver.get("http://localhost:" + this.port + "/login");

		Login login = new Login(driver);
		HomePage homePage = login.loginValidUser("admin", "admin");

		WebElement notes = driver.findElement(By.xpath("//a[@href='#nav-notes']"));
		jse.executeScript("arguments[0].click()", notes);

		WebElement addNoteButton = driver.findElement(By.xpath("//button[@id='addNoteButton']"));
		wait.until(ExpectedConditions.elementToBeClickable(addNoteButton)).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(noteTitle);;

		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("Note creation test");
		WebElement noteSubmit = driver.findElement(By.id("save-note-id"));
		noteSubmit.click();

		driver.get("http://localhost:" + this.port + "/result?error=false&success=true");

		driver.findElement(By.xpath("//a[contains(.,'here')]")).click();

		driver.get("http://localhost:" + this.port + "/home");
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);

		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
		WebElement deleteElement = null;
		for (int i = 0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			deleteElement = element.findElement(By.id("delete"));
			if (deleteElement != null){
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
		Assertions.assertEquals("Result", driver.getTitle());

	}



	@Test
	public void createCredential() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		//login

		driver.get("http://localhost:" + this.port + "/login");

		Login login = new Login(driver);
		HomePage homePage = login.loginValidUser("admin", "admin");

		Assertions.assertEquals("Home", driver.getTitle());

		WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credTab);
		wait.withTimeout(Duration.ofSeconds(30));
		WebElement newCred = driver.findElement(By.id("newcred"));
		wait.until(ExpectedConditions.elementToBeClickable(newCred)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(credURL);
		WebElement credUsername = driver.findElement(By.id("credential-username"));
		credUsername.sendKeys("google");
		WebElement credPassword = driver.findElement(By.id("credential-password"));
		credPassword.sendKeys("google");
		WebElement submit = driver.findElement(By.id("save-credential"));
		submit.click();

		driver.get("http://localhost:" + this.port + "/result?error=false&success=true");
		driver.findElement(By.xpath("//a[contains(.,'here')]")).click();


		//check for credential
		driver.get("http://localhost:" + this.port + "/home");
		credTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credTab);
		WebElement credsTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credsList = credsTable.findElements(By.tagName("td"));
		Boolean created = false;
		for (int i=0; i < credsList.size(); i++) {
			WebElement element = credsList.get(i);
			if (element.getAttribute("innerHTML").equals("google")) {
				created = true;
				break;
			}
		}
		Assertions.assertTrue(created);
	}

	@Test
	public void updateCredential() throws InterruptedException {

		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		//login

		driver.get("http://localhost:" + this.port + "/login");

		Login login = new Login(driver);
		HomePage homePage = login.loginValidUser("admin", "admin");

		Assertions.assertEquals("Home", driver.getTitle());

		WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credTab);
		wait.withTimeout(Duration.ofSeconds(100));

		//Create Credential
		WebElement newCred = driver.findElement(By.id("newcred"));
		wait.withTimeout(Duration.ofSeconds(30));
		wait.until(ExpectedConditions.elementToBeClickable(newCred)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(credURL);
		WebElement credUsername = driver.findElement(By.id("credential-username"));
		credUsername.clear();
		credUsername.sendKeys("Google");
		WebElement credPassword = driver.findElement(By.id("credential-password"));
		credPassword.sendKeys("Google");
		WebElement submit = driver.findElement(By.id("save-credential"));
		submit.click();

		//result page on success
		driver.get("http://localhost:" + this.port + "/result?error=false&success=true");
		driver.findElement(By.xpath("//a[contains(.,'here')]")).click();


		//Click on credential nav link
		driver.get("http://localhost:" + this.port + "/home");
		credTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credTab);


		//Click on Edit Note
		WebElement editButton = driver.findElement(By.xpath("//button[@id='editCred']"));
		wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
		WebElement url = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
		url.clear();
		url.sendKeys("Netflix.com");
	;
		WebElement submitEdit = driver.findElement(By.id("save-credential"));
		submitEdit.click();

		// Result page on success
		driver.get("http://localhost:" + this.port + "/result?error=false&success=true");

		driver.findElement(By.xpath("//a[contains(.,'here')]")).click();

		driver.get("http://localhost:" + this.port + "/home");
		credTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credTab);



		//Check if edit was successful

		WebElement credsTables = driver.findElement(By.id("credentialTable"));
		List<WebElement> credsList = credsTables.findElements(By.tagName("td"));
		Boolean edited = false;
		for (int i=0; i < credsList.size(); i++) {
			WebElement element = credsList.get(i);
			if (element.getAttribute("innerHTML").equals("Netflix.com")) {
				System.out.println("got here 2 too");
				edited = true;
				break;
			}
		}
		Assertions.assertTrue(edited);
	}

	@Test
	public void deleteCredential() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		//login
		driver.get("http://localhost:" + this.port + "/login");

		Login login = new Login(driver);
		HomePage homePage = login.loginValidUser("admin", "admin");
		Assertions.assertEquals("Home", driver.getTitle());

		WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credTab);
		wait.withTimeout(Duration.ofSeconds(30));

		//Create Credential
		WebElement newCred = driver.findElement(By.id("newcred"));
		wait.until(ExpectedConditions.elementToBeClickable(newCred)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(credURL);
		WebElement credUsername = driver.findElement(By.id("credential-username"));
		credUsername.clear();
		credUsername.sendKeys("Google");
		WebElement credPassword = driver.findElement(By.id("credential-password"));
		credPassword.sendKeys("Google");
		WebElement submit = driver.findElement(By.id("save-credential"));
		submit.click();

		//result page on success and redirect to home
		driver.get("http://localhost:" + this.port + "/result?error=false&success=true");
		driver.findElement(By.xpath("//a[contains(.,'here')]")).click();

		driver.get("http://localhost:" + this.port + "/home");
		WebElement credTabLink = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credTabLink);

		WebElement credentialTable1 = driver.findElement(By.id("credentialTable"));
		List<WebElement> credList1 = credentialTable1.findElements(By.tagName("td"));
		WebElement deleteElement = null;
		for (int i = 0; i < credList1.size(); i++) {
			WebElement element = credList1.get(i);
			deleteElement = element.findElement(By.id("delete"));
			if (deleteElement != null){
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
		Assertions.assertEquals("Result", driver.getTitle());

	}


}
