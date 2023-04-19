package BusinessLayer;

import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Basket {
    //fields
    private Store store;
    private ConcurrentHashMap<Integer, ItemPair> items; //<ItemID, ItemPair>
    private boolean saved;
    //TODO: List<receiptItem> for every saved item, if saved is false again list becomes emptyCart


    //methods
    public Basket(Store _store){
        store = _store;
        items = new ConcurrentHashMap<>();
        saved = false;
    }

    public void addItem(CatalogItem item, int quantity) throws Exception {
        validateAddItem(item, quantity);

        items.putIfAbsent(item.getItemID(), new ItemPair(item, quantity));

        saved = false;
    }

    public void changeItemQuantity(int itemID, int quantity) throws Exception {
        validateChangeItemQuantity(itemID, quantity);

        items.get(itemID).quantity = quantity;

        saved = false;
    }

    public void removeItem(int itemID) throws Exception {
        if(!items.containsKey(itemID)){
            //LOG
            throw new Exception("ERROR: Basket::removeItemFromCart: no such item in basket!");
        }

        items.remove(itemID);

        saved = false;
    }

    private void validateAddItem(CatalogItem item, int quantity) throws Exception {
        if(item == null){
            //LOG
            throw new Exception("ERROR: Basket::addItemToCart: given item is null!");
        }

        if(quantity < 1){
            //LOG
            throw new Exception("ERROR: Basket::addItemToCart: given quantity is not valid!");
        }

        if(items.containsKey(item.getItemID())){
            //LOG
            throw new Exception("ERROR: Basket::addItemToCart: the item is already in the basket!");
        }
    }

    private void validateChangeItemQuantity(int itemID, int quantity) throws Exception {
        if(quantity < 1){
            //LOG
            throw new Exception("ERROR: Basket::changeItemQuantityInCart: given quantity is not valid!");
        }

        if(!items.containsKey(itemID)){
            //LOG
            throw new Exception("ERROR: Basket::changeItemQuantityInCart: the item is not in the basket!");
        }
    }

    public Store getStore(){
        return store;
    }

    public HashMap<CatalogItem, Integer> getItems(){
        HashMap<CatalogItem, Integer> inBasket = new HashMap<>();

        for(Integer itemID : items.keySet()){
            inBasket.putIfAbsent(items.get(itemID).item, items.get(itemID).quantity);
        }

        return inBasket;
    }

    public void saveItems() throws Exception{
        HashMap<CatalogItem, Integer> toBuy = getItems();

        //TODO: ask store to save the items
        //TODO: Basket should make List<receiptItem> for every item saved

        saved = true;

    }

    public void buyBasket() throws Exception{
        if(!saved){
            throw new Exception("The basket of store " + store.getStoreName() + " is not saved for buying");
        }

        HashMap<CatalogItem, Integer> toBuy = getItems();

        //TODO: ask store to buy the items
        /*
            NOTICE: the Store may throw an exception if Basket requests a certain
            item more than Store can provide.
         */

        //TODO: send Store the receipt and return the list to cart
    }









    /**
     * <CatalogItem, quantity>
     * this class is a wrapper for Basket use only
     */
    private class ItemPair{
        public CatalogItem item;
        public int quantity;

        public ItemPair(CatalogItem _item, int _quantity){
            item = _item;
            quantity = _quantity;
        }
    }

}


