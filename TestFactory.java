import org.testng.annotations.Factory;

public class TestFactory {

    @Factory
    public Object[] facebookFactoryTest(){
        return new Object[]{
                new prueba_netflix(),
                new prueba_netflix()
        };
    }
}
