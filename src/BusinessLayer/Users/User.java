package BusinessLayer.Users;

import BusinessLayer.Cart;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;

import java.util.HashMap;
import java.util.List;

public abstract class User {
    private Cart cart;

    public User() {
        this.cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }

    public void addItem(Store store, CatalogItem item, int quantity) throws Exception {
        cart.addItem(store, item, quantity);
    }

    public void removeItem(int storeID, int itemID) throws Exception {
        cart.removeItem(storeID, itemID);
    }

    public void changeItemQuantity(int storeID, int itemID, int quantity) throws Exception {
        cart.changeItemQuantity(storeID, itemID, quantity);
    }

    /**
     * this method is used to show the costumer all the stores he added,
     * he can choose one of them and see what is inside with getItemsInBasket
     *
     * @return List<String> @TODO maybe should be of some kind of object?
     */
    public List<String> getStoresOfBaskets() {
        return cart.getStoresOfBaskets();
    }

    public HashMap<CatalogItem, Integer> getItemsInBasket(String storeName) throws Exception {
        return cart.getItemsInBasket(storeName);
    }

    public void buyCart() throws Exception {
        cart.buyCart();
    }

    /**
     * empties the cart
     */
    public void empty() {
        cart.empty();
    }
}
