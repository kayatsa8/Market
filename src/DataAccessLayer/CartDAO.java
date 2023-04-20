package DataAccessLayer;

import BusinessLayer.Basket;
import BusinessLayer.Cart;
import BusinessLayer.Stores.CatalogItem;

import java.util.concurrent.ConcurrentHashMap;

/**
 * the class will be changed in next versions
 */
public class CartDAO {
    private static ConcurrentHashMap<Integer, Cart> cartMap; //<userID, Cart>

    public CartDAO(){
        cartMap = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Integer, Cart> getAllCarts(){
        return cartMap;
    }

    public void addCart(int userID, Cart cart){
        cartMap.putIfAbsent(userID, cart);
    }

    public void removeCart(int userID){
        cartMap.remove(userID);
    }

    public void addBasketToCart(int storeID){
        /*
            The method assumes the cart in user is the same cart in here,
            and therefore a Basket shouldn't be added to the cart (since it already happened
            in BasketsRepository.
            Instead, the DAO will search for the Basket inside the cart and put it to the DB
         */
    }

    public void addItemToCart(int storeID, Basket.ItemWrapper item){

    }

    public void removeItemFromCart(int storeID, int itemID){

    }

    public void changeItemQuantity(int storeID, int itemID, int quantity){

    }



}
