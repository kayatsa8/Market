package DataAccessLayer;

import BusinessLayer.CartAndBasket.Basket;
import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.Market;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.concurrent.ConcurrentHashMap;

/**
 * the class will be changed in next versions
 */
public class CartDAO {

    public CartDAO(){

    }

    public void addItem(Cart cart, Basket basket) throws Exception {
        DBConnector<Basket> basketConnector =
                new DBConnector<>(Basket.class, Market.getInstance().getConfigurations());
        basketConnector.insert(basket);

        DBConnector<Cart> cartConnector =
                new DBConnector<>(Cart.class, Market.getInstance().getConfigurations());
        cartConnector.saveState(cart);
    }



}
