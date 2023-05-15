package ServiceLayer.Objects;

import BusinessLayer.CartAndBasket.Basket;
import BusinessLayer.CartAndBasket.Cart;
import PresentationLayer.views.MainLayout;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;

import com.vaadin.flow.component.notification.Notification;

import java.util.ArrayList;
import java.util.List;

public class CartService {

    //service object for cart, includes a list of basketsService
    private List<BasketService> baskets;
    static ShoppingService shoppingService;

    public CartService(Cart cart) {
        baskets = new ArrayList<>();
        for (Basket basket : cart.getBaskets().values()) {
            baskets.add(new BasketService(basket));
        }
    }


    public BasketService getBasketOfStore(int storeId){
        for(BasketService basket: baskets){
            if(basket.getStoreId() == storeId)
                return basket;
        }
        return null;
    }

    public boolean isEmpty(){
        boolean found = true;
        if(baskets.isEmpty())
            return found;
        for(BasketService basketService : baskets){
            if(!basketService.isEmpty()){
                found = false;
            }
        }
        return found;
    }

    public static void setAmount(CatalogItemService catalogItemService, Integer amount) {

        try {
            shoppingService = new ShoppingService();
            Result<CartService> result = shoppingService.addItemToCart(MainLayout.getCurrUserID(),catalogItemService.getStoreID(), catalogItemService.getItemID(), amount);
            if (!result.isError()){
                Notification.show("Successfully added to " + MainLayout.getCurrUserID()+"'s cart\n");
            }
            else {
                Notification.show(MainLayout.getCurrUserID() + result.getMessage());
            }
        }
        catch (Exception e) {}
    }
    public List<BasketService> getAllBaskets() {
        return baskets;
    }
}
