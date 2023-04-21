package BusinessLayer.Users;

import BusinessLayer.Cart;
import BusinessLayer.ExternalSystems.Purchase.PurchaseClient;
import BusinessLayer.ExternalSystems.Supply.SupplyClient;
import BusinessLayer.Stores.CartItemInfo;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;

import java.util.HashMap;
import java.util.List;

public abstract class User {
    protected Cart cart;

    public User() {
        this.cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }

    public Cart addItemToCart(Store store, CatalogItem item, int quantity) throws Exception {
        cart.addItem(store, item, quantity);
        return cart;
    }

    public Cart removeItemFromCart(int storeID, int itemID) throws Exception {
        cart.removeItem(storeID, itemID);
        return cart;
    }

    public Cart changeItemQuantityInCart(int storeID, int itemID, int quantity) throws Exception {
        cart.changeItemQuantity(storeID, itemID, quantity);
        return cart;
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

    public HashMap<CatalogItem, CartItemInfo> getItemsInBasket(String storeName) throws Exception {
        return cart.getItemsInBasket(storeName);
    }

    public Cart buyCart(String deliveryAddress) throws Exception {
        cart.buyCart(new PurchaseClient(), new SupplyClient(), deliveryAddress);
        return cart;
    }

    /**
     * empties the cart
     */
    public Cart emptyCart() {
        cart.empty();
        return cart;
    }
}
