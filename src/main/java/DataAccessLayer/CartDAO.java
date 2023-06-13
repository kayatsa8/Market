package DataAccessLayer;

import BusinessLayer.CartAndBasket.Basket;
import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Market;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.List;
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

    public void removeBasket(Basket basket) throws Exception {
        DBConnector<Basket.ItemWrapper> wrapperConnector =
                new DBConnector<>(Basket.ItemWrapper.class, Market.getInstance().getConfigurations());

        for(Basket.ItemWrapper wrapper : basket.getItems()){
            wrapperConnector.delete(wrapper.getId());
        }

        DBConnector<CartItemInfo> infoConnector =
                new DBConnector<>(CartItemInfo.class, Market.getInstance().getConfigurations());

        for(Basket.ItemWrapper wrapper : basket.getItems()){
            infoConnector.delete(wrapper.info.getId());
        }

        DBConnector<Basket> basketConnector =
                new DBConnector<>(Basket.class, Market.getInstance().getConfigurations());
        basketConnector.delete(basket.getId());
    }

    public void empty(List<Basket> baskets) throws Exception {
        for(Basket basket : baskets){
            removeBasket(basket);
        }

        //TODO: after add coupon is persisted, delete coupons
    }

}
