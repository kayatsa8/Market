package BusinessLayer;

import BusinessLayer.ExternalSystems.Purchase.PurchaseClient;
import BusinessLayer.ExternalSystems.Supply.SupplyClient;
import BusinessLayer.Receipts.ReceiptHandler;
import BusinessLayer.Stores.CartItemInfo;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {

    //fields
    private final int userID;
    private final ConcurrentHashMap<Integer, Basket> baskets; // <storeID, Basket>

    //methods

    /**
     * a constructor for registered user
     * @param _userID the id of the user
     */
    public Cart(int _userID){
        userID = _userID;
        baskets = new ConcurrentHashMap<>();
    }

    /**
     * a constructor for not registered user
     */
    public Cart(){
        userID = 999999; // a convention
        baskets = new ConcurrentHashMap<>();
    }

    public void addItem(Store store, CatalogItem item, int quantity) throws Exception {
        baskets.putIfAbsent(store.getStoreID(), new Basket(store));
        baskets.get(store.getStoreID()).addItem(item, quantity);
    }

    public void removeItem(int storeID, int itemID) throws Exception {
        if(!baskets.containsKey(storeID)){
            //LOG
            throw new Exception("Cart::removeItem: the store " + storeID + " was not found!");
        }

        baskets.get(storeID).removeItem(itemID);
    }

    public void changeItemQuantity(int storeID, int itemID, int quantity) throws Exception {
        if(!baskets.containsKey(storeID)){
            //LOG
            throw new Exception("Cart::changeItemQuantity: the store " + storeID + " was not found!");
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

    public HashMap<CatalogItem, CartItemInfo> getItemsInBasket(String storeName) throws Exception {
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

    /**
     * HashMap <k,v> = <StoreID, <CatalogItem, quantity>>
     * @return a HashMap to give the ReceiptHandler in order to make a receipt
     * @throws Exception if the store throw exception for some reason
     */
    public HashMap<Integer, HashMap<CatalogItem, CartItemInfo>> buyCart(PurchaseClient purchase, SupplyClient supply, String address) throws Exception {
        HashMap<Integer, HashMap<CatalogItem, CartItemInfo>> receiptData = new HashMap<>();

        for(Basket basket : baskets.values()){
            basket.saveItems();
        }

        //TODO: should change in future versions
        double finalPrice = calculateTotalPrice();
        boolean purchaseSuccess = purchase.pay(userID, finalPrice);

        //TODO: should change in future versions
        supply.chooseService();
        boolean supplySuccess = supply.supply(userID, address);


        if(!purchaseSuccess || !supplySuccess){
            for(Basket basket : baskets.values()){
                basket.releaseItems();
            }
            return null;
        }


        for(Basket basket : baskets.values()){
            receiptData.putIfAbsent(basket.getStore().getStoreID(), basket.buyBasket(userID));
        }

        empty();

        //LOG: buy method completed
        return receiptData;
    }

    /**
     * empties the cart
     */
    public void empty(){
        baskets.clear();
    }

    public double calculateTotalPrice(){
        double price = 0;

        for(Basket basket : baskets.values()){
            price += basket.calculateTotalPrice();
        }

        return price;
    }

    public boolean isItemInCart(int itemID, int storeID){
        return baskets.get(storeID).isItemInBasket(itemID);
    }


}
