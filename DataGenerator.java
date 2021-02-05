import org.testng.annotations.DataProvider;

public class DataGenerator {

    @DataProvider(name="emails")
    public Object[][] emailInfo(){
        return new Object[][] {
                {"testing@test.com."},
                {"testing"},
                {"asdfasdfasdf"}
        };
    }
}
