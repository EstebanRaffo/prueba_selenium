
import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BookingTest {
    WebDriver driver;

    @BeforeMethod
    public void setup(){
        System.setProperty("webdriver.opera.driver", "drivers/operadriver.exe");
        driver = new OperaDriver();
        driver.get("https://www.booking.com/index.es.html");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    @Test
    public void validarTituloTest(){
        System.out.println(driver.getTitle());
        String title = "Booking.com | Web oficial | Los mejores hoteles y alojamientos";
        Assert.assertEquals(driver.getTitle(), title);
    }

    @Test
    public void mostrarLinksTest(){
        List<WebElement> links = driver.findElements(By.tagName("a"));
        for(WebElement unLink : links){
            System.out.println(unLink.getText());
        }
    }

    @Test
    public void mostrarH1sTest(){
        List<WebElement> h1s = driver.findElements(By.tagName("h1"));
        for(WebElement unh1 : h1s){
            System.out.println(unh1.getText());
        }
    }

    @Test
    public void buscarGenteViajeraTest(){
        List<WebElement> h2s = driver.findElements(By.tagName("h2"));
        String text = "Conecta con gente viajera";
        boolean found_text = false;
        for(WebElement unh2 : h2s){
            if(unh2.getText().contains(text)){
                found_text = true;
            }
        }
        Assert.assertTrue(found_text);
    }

    @Test
    public void registroUsuarioTests(){
        driver.findElement(By.xpath("//span[contains(text(), \"Inicia sesión\")]")).click();
        driver.findElement(By.xpath("//span[contains(text(), \"Continuar con e-mail\")]")).click();
        String message_error = "Introduce tu dirección de e-mail";
        WebElement error = driver.findElement(By.xpath("//div[contains(text(), \"Introduce tu dirección de e-mail\")]"));
        Assert.assertEquals(error.getText(), message_error);

        Faker faker_data = new Faker();
        String email = faker_data.internet().emailAddress();

        driver.findElement(By.id("username")).sendKeys(email);
        driver.findElement(By.xpath("//span[contains(text(), \"Continuar con e-mail\")]")).click();

        driver.findElement(By.id("new_password")).sendKeys("abc123");
        driver.findElement(By.id("confirmed_password")).sendKeys("abc12");
        driver.findElement(By.xpath("//span[contains(text(), \"Crear una cuenta\")]")).click();


        String error_pass_confirm = "Las contraseñas no coinciden. Inténtalo de nuevo.";

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("confirmed_password-description"))));

        WebElement message_pass_confirm = driver.findElement(By.id("confirmed_password-description"));
        Assert.assertEquals(message_pass_confirm.getText(), error_pass_confirm);

    }

    @Test
    public void crearCuentaTest(){
        driver.findElement(By.xpath("//span[contains(text(), \"Hazte una cuenta\")]")).click();
        driver.findElement(By.id("username")).sendKeys("e.fraffo@hotmail.com");
        driver.findElement(By.xpath("//span[contains(text(), \"Continuar con e-mail\")]")).click();

        driver.findElement(By.id("password")).sendKeys("abc123");
        driver.findElement(By.xpath("//span[contains(text(), \"Iniciar sesión\")]")).click();

        String error_pass = "La combinación de e-mail y contraseña que has introducido no coinciden";
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[contains(text(), \"La combinación de e-mail y contraseña que has introducido no coinciden\")]"))));

        WebElement error_pass_message = driver.findElement(By.xpath("//div[contains(text(), \"La combinación de e-mail y contraseña que has introducido no coinciden\")]"));
        Assert.assertEquals(error_pass_message.getText(), error_pass);
    }

    @Test(dataProvider = "emails", dataProviderClass = DataGenerator.class)
    public void iniciarSesion(String email){
        driver.findElement(By.xpath("//span[contains(text(), \"Inicia sesión\")]")).click();
        driver.findElement(By.id("username")).sendKeys(email);
        driver.findElement(By.xpath("//span[contains(text(), \"Continuar con e-mail\")]")).click();

        String error_mail = "Comprueba si el e-mail que has introducido es correcto";
        WebElement error_mail_message = driver.findElement(By.id("username-description"));
        Assert.assertEquals(error_mail_message.getText(), error_mail);
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {
        Thread.sleep(5000);
        driver.close();
    }
}
