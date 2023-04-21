package ServiceLayer.Objects;

import BusinessLayer.CartAndBasket.Basket;
import BusinessLayer.CartAndBasket.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartService {

    //service object for cart, includes a list of basketsService
    private List<BasketService> baskets;
    public CartService(Cart cart) {
        baskets = new ArrayList<>();
        for (Basket basket : cart.getBaskets().values()) {
            baskets.add(new BasketService(basket));
        }
    }


    public BasketService getBasketOfStore(int storeId){
        //get the basket I have for this storeId
        return null;
    }
}
