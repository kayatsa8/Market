package PresentationLayer;

import BusinessLayer.ExternalSystems.PurchaseInfo;
import BusinessLayer.ExternalSystems.SupplyInfo;
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

    public static void main(String[] args) {
        String addressOk="addressOk";
        LocalDate bDayOk=LocalDate.of(2022, 7, 11);

        ConfigReader configReader=new ConfigReader();
        String relativePath = configReader.getInitializePath();

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

}
