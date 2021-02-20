import org.testng.annotations.DataProvider;

public class DataGenerator {

    @DataProvider(name="emails")
    public Object[][] emailInfo(){
        return new Object[][] {
                {"test@test.com"},
                {"e.fraffo@hotmail.com"},
                {"e.fraffo@gmail.com"}
        };
    }
}
