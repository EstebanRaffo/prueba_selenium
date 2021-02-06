
import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class prueba_netflix {
    WebDriver driver;

    @BeforeMethod
    public void setup(){
        System.setProperty("webdriver.opera.driver", "drivers/operadriver.exe");
        driver = new OperaDriver();
        driver.get("https://www.netflix.com");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    @Test (priority = 5)
    public void validarTituloTest(){
        System.out.println(driver.getTitle());
        String title = "Netflix Argentina: Ve series online, ve películas online";
        Assert.assertEquals(driver.getTitle(), title);
    }

    @Test (priority = 4)
    public void iniciarSesionPageTest(){
        driver.findElement(By.linkText("Iniciar sesión")).click();
        String title = "Netflix";
        Assert.assertEquals(driver.getTitle(), title);

        List<WebElement> h1s = driver.findElements(By.tagName("h1"));
        String text = "Inicia sesión";
        boolean found_text = false;
        for(WebElement unh1 : h1s){
            if(unh1.getText().contains(text)){
                found_text = true;
            }
        }
        Assert.assertTrue(found_text);

        String session_fb_text = "Iniciar sesión con Facebook";
        WebElement text_fb = driver.findElement(By.xpath("//span[contains(text(), \"Iniciar sesión con Facebook\")]"));
        Assert.assertEquals(text_fb.getText(), session_fb_text);
    }

    @Test (priority = 3)
    public void loginToNetflixErrorTest() throws InterruptedException {
        // test@test.com
        Thread.sleep(4000);
        driver.findElement(By.id("id_userLoginId")).sendKeys("test@test.com");
        driver.findElement(By.id("id_password")).sendKeys("holamundo");
        driver.findElement(By.xpath("//span[contains(text(), \"Recuérdame\")]")).click();
        driver.findElement(By.xpath("//button[contains(text(), \"Iniciar sesión\")]")).click();

        String pass_error_message = "Contraseña incorrecta";
        WebElement pass_error = driver.findElement(By.xpath("//*[contains(text(), \"Contraseña incorrecta\")]"));
        boolean message_error = pass_error.getText().contains(pass_error_message);
        Assert.assertTrue(message_error);

        boolean isSelected = driver.findElement(By.xpath("//span[contains(text(), \"Recuérdame\")]")).isSelected();
        Assert.assertTrue(isSelected);
    }

    @Test (priority = 2)
    public void fakeEmailTest() throws InterruptedException {
        Faker faker_data = new Faker();
        String email = faker_data.internet().emailAddress();

        driver.findElement(By.name("email")).sendKeys(email);
        Thread.sleep(3000);
        driver.findElement(By.xpath("//span[contains(text(), \"COMENZÁ YA\")]")).click();

        Thread.sleep(3000);
        String url = driver.getCurrentUrl();
        System.out.println(url);
        boolean signup = url.contains("signup");
        Assert.assertTrue(signup);
    }

    @Test(priority = 1, dataProvider = "emails", dataProviderClass = DataGenerator.class)
    public void dataProviderEmailTest(String email){
        // ACLARACION: EN LA PANTALLA DE INICIAR SESION NO HAY UN BOTON "COMENZA YA"
        // ESE BOTON SE ENCUENTRA EN LA PRIMER PANTALLA

        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.xpath("//span[contains(text(), \"COMENZÁ YA\")]")).click();

    }

    @Test (priority = 0)
    @Parameters({"tagName"})
    public void printTagsTest(@Optional("h2") String tag){
        List<WebElement> listaElementos = driver.findElements(By.tagName(tag));

        if(listaElementos.size() > 0){
            for(WebElement elemento : listaElementos){
                System.out.println(elemento.getText());
            }
        }else{
            System.out.println("No se encontraron elementos con la etiqueta " + tag);
        }
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {
        Thread.sleep(5000);
        driver.close();
    }

}
