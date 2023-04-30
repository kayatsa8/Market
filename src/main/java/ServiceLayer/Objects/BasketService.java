package ServiceLayer.Objects;

import BusinessLayer.CartAndBasket.Basket;

public class BasketService {

    private Basket basket;

    public BasketService(Basket basket) {
        this.basket = basket;
    }

    public boolean hasItem(int itemId){
        return basket.isItemInBasket(itemId);
    }

    public boolean isEmpty() {
        return basket.getItems().isEmpty();
    }

    public int getStoreId(){
        return basket.getStore().getStoreID();
    }

}
