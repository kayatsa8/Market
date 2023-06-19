package PresentationLayer;

import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.ExternalSystems.PurchaseInfo;
import BusinessLayer.ExternalSystems.SupplyInfo;
import BusinessLayer.Market;
import BusinessLayer.NotificationSystem.UserMailbox;
import BusinessLayer.Stores.Store;
import BusinessLayer.Users.Guest;
import BusinessLayer.Users.RegisteredUser;
import DataAccessLayer.Hibernate.ConnectorConfigurations;
import DataAccessLayer.Hibernate.DBConnector;
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
import java.util.List;
import java.util.logging.ConsoleHandler;
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


    public static void main(String[] args) throws Exception {
        ConfigReader configReader = new ConfigReader();
        DBConnector<Store> connector = new DBConnector<>(Store.class, readDBConfigurations(configReader));

        List<Store> stores = connector.getAll();
        if (stores.size() > 0) {
            System.out.println("The system already has stores, not loading from loader.");
        } else {
            String relativePath = configReader.getInitializePath();

            Loader loader = new Loader();
            loader.load(relativePath);
        }

        SpringApplication.run(Application.class, args);

    }


    public static PurchaseInfo getPurchaseInfo(){
        return new PurchaseInfo("123", 1, 2222, "asd", 1222, 1, LocalDate.of(2000, 1, 1));
    }

    public static SupplyInfo getSupplyInfo(){
        return new SupplyInfo("Name", "address", "city", "counyrt", "asd");
    }

    public static ConnectorConfigurations readDBConfigurations(ConfigReader configReader) throws Exception {
        String name = configReader.getDBName();
        String url = configReader.getDBUrl();
        String username = configReader.getDBUsername();
        String password = configReader.getDBPassword();
        String driver = configReader.getDBDriver();

        return new ConnectorConfigurations(name, url, username, password, driver);
    }

}
