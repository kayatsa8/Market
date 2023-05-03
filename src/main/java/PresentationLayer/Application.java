package PresentationLayer;

import ServiceLayer.ShoppingService;
import ServiceLayer.UserService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "Market")
public class Application implements AppShellConfigurator {
    public static void main(String[] args) {

        try {
            ShoppingService shoppingService = new ShoppingService();
            UserService userService = new UserService();
            int amirID = userService.register("Amir", "amirsPass").getValue();
            int tomerID = userService.register("Tomer", "tomersPass").getValue();
            int yonatanID = userService.register("Yonatan", "yonantansPass").getValue();
            int sagiID = userService.register("Sagi", "sagisPass").getValue();
            int meitarID = userService.register("Meitar", "meitarsPass").getValue();
            int maorID = userService.register("Maor", "maorsPass").getValue();
            int amirStoreID = shoppingService.createStore(amirID, "amirs Store").getValue();
            int maorStoreID = shoppingService.createStore(amirID, "maors Store").getValue();
            userService.addOwner(amirID, tomerID, amirStoreID);
            userService.addManager(amirID, meitarID, amirStoreID);
            shoppingService.addItemToStore(amirStoreID, "Bread", 5, "Wheat");
            shoppingService.addItemToStore(amirStoreID, "Milk", 6, "Dairy");
            shoppingService.addItemToStore(amirStoreID, "Butter", 7, "Dairy");
        }
        catch (Exception e) {
            System.out.println("Problem initiating Market");
            return;
        }
        SpringApplication.run(Application.class, args);
    }

}
