package PresentationLayer;

import BusinessLayer.ExternalSystems.PurchaseInfo;
import BusinessLayer.ExternalSystems.SupplyInfo;
import ServiceLayer.Objects.CatalogItemService;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;
import ServiceLayer.UserService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
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
@Push
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

            userService.login("Amir", "amirsPass");
            userService.login("Maor", "maorsPass");

            int amirStoreID = shoppingService.createStore(amirID, "amirs Store").getValue();
            int maorStoreID = shoppingService.createStore(maorID, "maors Store").getValue();

            userService.addOwner(amirID, tomerID, amirStoreID);
            userService.addManager(amirID, meitarID, amirStoreID);
            Result<CatalogItemService> result = shoppingService.addItemToStore(amirStoreID, "Bread", 5, "Wheat", 50);
            shoppingService.addItemToStore(amirStoreID, "Milk", 6, "Dairy", 10);
            shoppingService.addItemToStore(amirStoreID, "Butter", 7, "Dairy", 30);
            int itemId = result.getValue().getItemID();
            shoppingService.addItemAmount(amirStoreID, itemId, 50);
            shoppingService.addItemAmount(amirStoreID, itemId + 1, 30);
            shoppingService.addItemAmount(amirStoreID, itemId + 2, 5);

            shoppingService.addItemToCart(maorID, amirStoreID, itemId, 5);
            shoppingService.buyCart(maorID, getPurchaseInfo(), getSupplyInfo());

            userService.logout(amirID);
            userService.logout(maorID);
        }
        catch (Exception e) {
            System.out.println("Problem initiating Market");
            return;
        }
        SpringApplication.run(Application.class, args);
    }


    public static PurchaseInfo getPurchaseInfo(){
        return new PurchaseInfo("123", 1, 2222, "asd", 1222, 1);
    }

    public static SupplyInfo getSupplyInfo(){
        return new SupplyInfo("Name", "address", "city", "counyrt", "asd");
    }

}
