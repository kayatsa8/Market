package BusinessLayer;

import BusinessLayer.Receipts.ReceiptHandler;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Basket {
    //fields
    private final Store store;
    private final ConcurrentHashMap<Integer, ItemPair> items; //<ItemID, ItemPair>
    private boolean itemsSaved; // true if the store saves the items inside the basket for the user
    private HashMap<CatalogItem, Integer> savedItems; //<CatalogItem, quantity


    //methods
    public Basket(Store _store){
        store = _store;
        items = new ConcurrentHashMap<>();
        itemsSaved = false;
        savedItems = null;
    }

    public void addItem(CatalogItem item, int quantity) throws Exception {
        validateAddItem(item, quantity);

        items.putIfAbsent(item.getItemID(), new ItemPair(item, quantity));

        releaseItems();
    }

    public void changeItemQuantity(int itemID, int quantity) throws Exception {
        validateChangeItemQuantity(itemID, quantity);

        items.get(itemID).quantity = quantity;

        releaseItems();
    }

    public void removeItem(int itemID) throws Exception {
        if(!items.containsKey(itemID)){
            //LOG
            throw new Exception("ERROR: Basket::removeItem: no such item in basket!");
        }

        items.remove(itemID);

        releaseItems();
    }

    private void validateAddItem(CatalogItem item, int quantity) throws Exception {
        if(item == null){
            //LOG
            throw new Exception("ERROR: Basket::addItem: given item is null!");
        }

        if(quantity < 1){
            //LOG
            throw new Exception("ERROR: Basket::addItem: given quantity is not valid!");
        }

        if(items.containsKey(item.getItemID())){
            //LOG
            throw new Exception("ERROR: Basket::addItem: the item is already in the basket!");
        }
    }

    private void validateChangeItemQuantity(int itemID, int quantity) throws Exception {
        if(quantity < 1){
            //LOG
            throw new Exception("ERROR: Basket::changeItemQuantity: given quantity is not valid!");
        }

        if(!items.containsKey(itemID)){
            //LOG
            throw new Exception("ERROR: Basket::changeItemQuantity: the item is not in the basket!");
        }
    }

    public Store getStore(){
        return store;
    }

    public HashMap<CatalogItem, Integer> getItems(){
        HashMap<CatalogItem, Integer> inBasket = new HashMap<>();

        for(Integer itemID : items.keySet()){
            inBasket.putIfAbsent(makeCopyOfCatalogItem(items.get(itemID).item),
                    items.get(itemID).quantity);
        }

        return inBasket;
    }

    private CatalogItem makeCopyOfCatalogItem(CatalogItem item){
        return new CatalogItem(item.getItemID(), item.getItemName(), item.getPrice(), item.getCategory());
    }

    public void saveItems() throws Exception{
        HashMap<CatalogItem, Integer> savedItems = getItems();

        try{

            StoreMock store = new StoreMock(); //TODO: remove the mock
            store.saveItems(savedItems);
        }
        catch(Exception e){
            //LOG

            /*
                NOTICE: the Store may throw an exception if Basket requests a certain
                item more than Store can provide.
            */
            e.printStackTrace();
        }

        itemsSaved = true;

    }

    /**
     * @return a HashMap of the bought items and their quantities
     * @throws Exception - the store can throw exceptions
     */
    public HashMap<CatalogItem, Integer> buyBasket() throws Exception{
        if(!itemsSaved){
            throw new Exception("The basket of store " + store.getStoreName() + " is not saved for buying");
        }

        try{
            StoreMock store = new StoreMock(); //TODO: remove the mock
            store.buyItems(savedItems);
        }
        catch(Exception e){
            //LOG
            e.printStackTrace();
        }

        return savedItems;
    }

    /**
     * if the basket contents had changed for some reason, the basket
     * asks the store to release the saved items
     */
    private void releaseItems(){
        if(itemsSaved){
            itemsSaved = false;
            StoreMock store = new StoreMock(); //TODO: remove the mock
            store.releaseItems(savedItems);
            savedItems = null;
        }
    }

    public double calculateTotalPrice(){
        //TODO: see how the discounts should take place here.
        /*
         *  POSSIBLE SOLUTION: a wrapper for catalogItem
         *  POSSIBLE SOLUTION 2: use receiptItem as that wrapper
         */
        double price = 0;

        for(ItemPair itemPair : items.values()){
            price += itemPair.item.getPrice();
        }

        return price;
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


    /**
     * temporary mocks, just to complete the code
     */

    private class StoreMock{
        public void saveItems(HashMap<CatalogItem, Integer> itemsToSave) throws Exception{
            throw new Exception("SWITCH THIS WITH THE REAL STORE");
        }

        public void buyItems(HashMap<CatalogItem, Integer> itemsToSave) throws Exception{
            throw new Exception("SWITCH THIS WITH THE REAL STORE");
        }

        public ReceiptHandler getReceiptHandler(){
            return null;
        }

        public int getStoreId(){
            return 9;
        }

        public void releaseItems(HashMap<CatalogItem, Integer> items){

        }

    }

}


