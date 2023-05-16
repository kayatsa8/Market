package BusinessLayer.CartAndBasket;

import BusinessLayer.CartAndBasket.Repositories.Baskets.ItemsRepository;
import BusinessLayer.CartAndBasket.Repositories.Baskets.SavedItemsRepository;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public void addItem(CatalogItem item, int quantity, List<String> coupons) throws Exception {
        validateAddItem(item, quantity);
        if (items.containsKey(item.getItemID())){
            int prevAmount= items.getAmount(item.getItemID());
            items.put(item.getItemID(), new ItemWrapper(item, quantity+prevAmount));
        }
        else
            items.putIfAbsent(item.getItemID(), new ItemWrapper(item, quantity));

        releaseItems();

        updateBasketWithCoupons(coupons);
    }

    public void changeItemQuantity(int itemID, int quantity, List<String> coupons) throws Exception {
        validateChangeItemQuantity(itemID, quantity);

        items.get(itemID).info.setAmount(quantity);

        releaseItems();

        updateBasketWithCoupons(coupons);
    }

    public void removeItem(int itemID, List<String> coupons) throws Exception {
        if(!items.containsKey(itemID)){
            //LOG
            throw new Exception("ERROR: Basket::removeItemFromCart: no such item in basket!");
        }

        items.remove(itemID);

        releaseItems();

        updateBasketWithCoupons(coupons);
    }

    private void validateAddItem(CatalogItem item, int quantity) throws Exception {
        if(item == null){
            //LOG
            throw new Exception("ERROR: Basket:: the item ID you entered does not exist in the given store");
        }

        if(quantity < 1){
            //LOG
            throw new Exception("ERROR: Basket::addItemToCart: given quantity is not valid!");
        }

        if(items.containsKey(item.getItemID())){
            //LOG
            throw new Exception("ERROR: Basket::addItemToCart: the item is already in the basket!");
        }

        if(!store.isItemInCatalog(item.getItemID())){
            throw new Exception("ERROR: Basket::addItemToCart: the item is not in the store!");
            //throw new Exception("ERROR: Basket::addItemToCart: the item is already in the basket!");
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
        return new CatalogItem(item.getItemID(), item.getItemName(), item.getPrice(), item.getCategory(), item.getStoreName(), item.getItemID(), item.getWeight());
    }

    public void saveItems(List<String> coupons) throws Exception{
        savedItems.set(getItemsInfo());

        try{
            store.saveItemsForUpcomingPurchase(getItemsInfo(), coupons);
            itemsSaved = true;
        }
        catch(Exception e){
            //LOG

            /*
                NOTICE: the Store may throw an exception if Basket requests a certain
                item more than Store can provide.
            */
            savedItems.set(null);
            itemsSaved = false;
            throw e;
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
    public void releaseItems() throws Exception {
        if(itemsSaved){
            itemsSaved = false;
            store.reverseSavedItems(savedItems.getList());
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

    public void updateBasketWithCoupons(List<String> coupons) throws Exception {
        List<CartItemInfo> updatedBasketItems = getItemsInfo();
        store.updateBasket(updatedBasketItems, coupons);
        checkIfPurchaseIsValid(updatedBasketItems);
        updateBasketByCartItemInfoList(updatedBasketItems);
    }

    private void updateBasketByCartItemInfoList(List<CartItemInfo> updatedBasketItems) {
        for(CartItemInfo info : updatedBasketItems){
            items.get(info.getItemID()).info = info;
        }
    }

    private void checkIfPurchaseIsValid(List<CartItemInfo> updatedBasketItems) throws Exception {
        if(!store.checkIfPurchaseIsValid(updatedBasketItems)){
            throw new Exception("ERROR: Basket::checkIfPurchaseIsValid: the purchase is not valid!");
        }
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
            info = new CartItemInfo(item.getItemID(), quantity, item.getPrice(), _item.getCategory(), _item.getItemName(), _item.getWeight());
        }
    }

}


