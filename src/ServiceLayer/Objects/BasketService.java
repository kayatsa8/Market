package ServiceLayer.Objects;

import BusinessLayer.Basket;

public class BasketService {

    private Basket basket;

    public BasketService(Basket basket) {
        this.basket = basket;
    }

    public boolean hasItem(int itemId){
        //return true if I have in this basket item with this itemId
        return true;
    }
}
