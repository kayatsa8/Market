package PresentationLayer;

import BusinessLayer.ExternalSystems.PurchaseInfo;
import BusinessLayer.ExternalSystems.SupplyInfo;
import BusinessLayer.Market;
import DataAccessLayer.Hibernate.ConnectorConfigurations;
import PresentationLayer.initialize.ConfigReader;
import PresentationLayer.initialize.Loader;
import ServiceLayer.Objects.CatalogItemService;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;
import ServiceLayer.UserService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "Market")
@Push
public class Application implements AppShellConfigurator {
    private static final String relativePath = "src/main/java/PresentationLayer/initialize/data.json";

    public static void main(String[] args) throws Exception {
        ConfigReader configReader=new ConfigReader();
        String relativePath = configReader.getInitializePath();

//        try{
//            readDBConfigurations(configReader);
//        }
//        catch(Exception e){
//            System.out.println("ERROR: unable to load the DB configurations to the system!");
//            System.exit(1);
//        }

        Loader loader=new Loader();
        loader.load(relativePath);

        SpringApplication.run(Application.class, args);
    }


    public static PurchaseInfo getPurchaseInfo(){
        return new PurchaseInfo("123", 1, 2222, "asd", 1222, 1, LocalDate.of(2000, 1, 1));
    }

    public static SupplyInfo getSupplyInfo(){
        return new SupplyInfo("Name", "address", "city", "counyrt", "asd");
    }

    public static void readDBConfigurations(ConfigReader configReader) throws Exception {
        String name = configReader.getDBName();
        String url = configReader.getDBUrl();
        String username = configReader.getDBUsername();
        String password = configReader.getDBPassword();
        String driver = configReader.getDBDriver();
        ConnectorConfigurations conf = new ConnectorConfigurations(name, url, username, password, driver);

        Market.getInstance().setConfigurations(conf);
    }

}
