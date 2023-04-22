package BusinessLayer;

import BusinessLayer.CartAndBasket.Repositories.Baskets.ItemsRepository;
import BusinessLayer.CartAndBasket.Repositories.Baskets.SavedItemsRepository;
import BusinessLayer.Receipts.ReceiptHandler;
import BusinessLayer.Stores.CartItemInfo;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Basket {
    //fields
    private final Store store;
    private final ItemsRepository items;
    private boolean itemsSaved; // true if the store saves the items inside the basket for the user
    private SavedItemsRepository savedItems;



    //methods
    public Basket(Store _store){
        store = _store;
        items = new ItemsRepository();
        itemsSaved = false;
        savedItems = new SavedItemsRepository();
    }

    public void addItem(CatalogItem item, int quantity) throws Exception {
        validateAddItem(item, quantity);

        items.putIfAbsent(item.getItemID(), new ItemWrapper(item, quantity));

        releaseItems();
    }

    public void changeItemQuantity(int itemID, int quantity) throws Exception {
        validateChangeItemQuantity(itemID, quantity);

        items.get(itemID).info.setAmount(quantity);

        releaseItems();
    }

    public void removeItem(int itemID) throws Exception {
        if(!items.containsKey(itemID)){
            //LOG
            throw new Exception("ERROR: Basket::removeItemFromCart: no such item in basket!");
        }

        items.remove(itemID);

        releaseItems();
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

    public HashMap<CatalogItem, CartItemInfo> getItems(){
        HashMap<CatalogItem, CartItemInfo> inBasket = new HashMap<>();

        for(Integer itemID : items.keySet()){
            inBasket.putIfAbsent(makeCopyOfCatalogItem(items.get(itemID).item),
                    new CartItemInfo(items.get(itemID).info));
        }

        return inBasket;
    }

    private CatalogItem makeCopyOfCatalogItem(CatalogItem item){
        return new CatalogItem(item.getItemID(), item.getItemName(), item.getPrice(), item.getCategory());
    }

    public void saveItems() throws Exception{
        savedItems.set(getItemsInfo());

        try{
            store.saveItemsForUpcomingPurchase(getItemsInfo());
            itemsSaved = true;
        }
        catch(Exception e){
            //LOG

            /*
                NOTICE: the Store may throw an exception if Basket requests a certain
                item more than Store can provide.
            */
            e.printStackTrace();
            savedItems.set(null);
            itemsSaved = false;
        }



    }

    private List<CartItemInfo> getItemsInfo(){
        List<CartItemInfo> infos = new ArrayList<>();

        for(ItemWrapper wrapper : items.values()){
            infos.add(new CartItemInfo(wrapper.info));
        }

        return infos;
    }

    /**
     * @return a HashMap of the bought items and their quantities
     * @throws Exception - the store can throw exceptions
     */
    public HashMap<CatalogItem, CartItemInfo> buyBasket(int userID) throws Exception{
        if(!itemsSaved){
            throw new Exception("The basket of store " + store.getStoreName() + " is not saved for buying");
        }

        try{
            store.buyBasket(savedItems.getList(), userID);
        }
        catch(Exception e){
            //LOG
            e.printStackTrace();
        }

        return prepareItemsForReceipt();
    }

    private HashMap<CatalogItem, CartItemInfo> prepareItemsForReceipt(){
        HashMap<CatalogItem, CartItemInfo> data = new HashMap<>();

        for(ItemWrapper item : items.values()){
            data.putIfAbsent(item.item, item.info);
        }

        return data;
    }

    /**
     * if the basket contents had changed for some reason, the basket
     * asks the store to release the saved items
     */
    public void releaseItems(){
        if(itemsSaved){
            itemsSaved = false;
            //TODO: store.releaseItems(savedItems.getList);
            savedItems.set(null);
        }
    }

    public double calculateTotalPrice(){
        double price = 0;

        for(ItemWrapper wrapper : items.values()){
            price += wrapper.info.getFinalPrice();
        }

        return price;
    }

    public boolean isItemInBasket(int itemID){
        return items.containsKey(itemID);
    }









    /**
     * <CatalogItem, quantity>
     * this class is a wrapper for Basket use only
     */
    public class ItemWrapper{
        public CatalogItem item;
        public CartItemInfo info;

        public ItemWrapper(CatalogItem _item, int quantity){
            item = _item;
            info = new CartItemInfo(item.getItemID(), quantity, 0, item.getPrice());
        }
    }

}


