package BusinessLayer;

import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {

    public ConcurrentHashMap<Integer, Basket> getBaskets() {
        return baskets;
    }

    //fields
    private final ConcurrentHashMap<Integer, Basket> baskets; // <storeID, Basket>


    //methods
    public Cart(){
        baskets = new ConcurrentHashMap<>();
    }

    public void addItem(Store store, CatalogItem item, int quantity) throws Exception {
        baskets.putIfAbsent(store.getStoreID(), new Basket(store));
        baskets.get(store.getStoreID()).addItem(item, quantity);
    }

    public void removeItem(int storeID, int itemID) throws Exception {
        if(!baskets.containsKey(storeID)){
            //LOG
            throw new Exception("Cart::removeItemFromCart: the store " + storeID + " was not found!");
        }

        baskets.get(storeID).removeItem(itemID);
    }

    public void changeItemQuantity(int storeID, int itemID, int quantity) throws Exception {
        if(!baskets.containsKey(storeID)){
            //LOG
            throw new Exception("Cart::changeItemQuantityInCart: the store " + storeID + " was not found!");
        }

        baskets.get(storeID).changeItemQuantity(itemID, quantity);
    }

    /**
     * this method is used to show the costumer all the stores he added,
     * he can choose one of them and see what is inside with getItemsInBasket
     * @return List<String>
     */
    public List<String> getStoresOfBaskets(){
        List<String> names = new ArrayList<>();

        for(Basket basket : baskets.values()){
            names.add(basket.getStore().getStoreName());
        }

        return names;
    }

    public HashMap<CatalogItem, Integer> getItemsInBasket(String storeName) throws Exception {
        int storeID = findStoreWithName(storeName);

        if(storeID == -1){
            //LOG
            throw new Exception("Cart::getItemsInBasket: the store " + storeName + " was not found!");
        }

        return baskets.get(storeID).getItems();
    }

    private int findStoreWithName(String name){
        for(Basket basket : baskets.values()){
            if(basket.getStore().getStoreName().equals(name)){
                return basket.getStore().getStoreID();
            }
        }

        return -1; //not found
    }

    public void buyCart() throws Exception {
        //TODO: this method should return receipt to user
        //TODO: this method should get purchase object in order to pay
        //TODO: make list of List<receiptItem> returned by baskets

        for(Basket basket : baskets.values()){
            basket.saveItems();
        }

        //TODO: here the payment will take place

        for(Basket basket : baskets.values()){
            //TODO: add the list to the receipt list
            basket.buyBasket();
        }

        //TODO: create the receipt for user

        empty();

        //LOG: buy method completed
        //TODO: return the receipt

    }

    /**
     * empties the cart
     */
    public void empty(){
        baskets.clear();
    }



    //REMEMBER: Cart should make receipts and send to both store and user
}
