package DataAccessLayer.CartAndBasketDAOs;

import BusinessLayer.CartAndBasket.Basket;
import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.CartAndBasket.Coupon;
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

    public void empty(List<Basket> baskets, List<Coupon> coupons) throws Exception {
        for(Basket basket : baskets){
            removeBasket(basket);
        }


        DBConnector<Coupon> couponConnector =
                new DBConnector<>(Coupon.class, Market.getInstance().getConfigurations());

        for(Coupon coupon : coupons){
            couponConnector.delete(coupon.getId());
        }
    }

    public void addCoupon(Cart cart, Coupon coupon) throws Exception {
        DBConnector<Coupon> couponConnector =
                new DBConnector<>(Coupon.class, Market.getInstance().getConfigurations());
        couponConnector.insert(coupon);

        DBConnector<Cart> cartConnector =
                new DBConnector<>(Cart.class, Market.getInstance().getConfigurations());
        cartConnector.saveState(cart);
    }

    public void removeCoupon(Coupon coupon) throws Exception {
        DBConnector<Coupon> couponConnector =
                new DBConnector<>(Coupon.class, Market.getInstance().getConfigurations());
        couponConnector.delete(coupon.getId());
    }

}
