package BusinessLayer.CartAndBasket;

import BusinessLayer.CartAndBasket.Repositories.Carts.BasketsRepository;
import BusinessLayer.ExternalSystems.Purchase.PurchaseClient;
import BusinessLayer.ExternalSystems.PurchaseInfo;
import BusinessLayer.ExternalSystems.Supply.SupplyClient;
import BusinessLayer.ExternalSystems.SupplyInfo;
import BusinessLayer.Log;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {

    //fields
    private final int userID;
    private final BasketsRepository baskets;
    private List<String> coupons;


    //methods
    /**
     * a constructor for registered user
     * @param _userID the id of the user
     */
    public Cart(int _userID){
        userID = _userID;
        baskets = new BasketsRepository();
        coupons = new ArrayList<>();
        Log.log.info("A new cart was created for user " + userID);
    }

    public void addItem(Store store, CatalogItem item, int quantity) throws Exception {
        baskets.putIfAbsent(store.getStoreID(), new Basket(store));
        baskets.get(store.getStoreID()).addItem(item, quantity, coupons);

        Log.log.info("The item " + item.getItemID() + " of store " +
                store.getStoreID() + " was added (" + quantity + " unites)");
    }

    public void removeItem(int storeID, int itemID) throws Exception {
        if(!baskets.containsKey(storeID)){
            Log.log.warning("Cart::removeItemFromCart: the store " + storeID + " was not found!");
            throw new Exception("Cart::removeItemFromCart: the store " + storeID + " was not found!");
        }

        baskets.get(storeID).removeItem(itemID, coupons);
        Log.log.info("The item " + itemID + " of store " + storeID + " was removed from the cart");
    }

    public Basket removeBasket(int storeID) throws Exception {
        Basket basket=baskets.remove(storeID);
        if(basket==null){
            Log.log.warning("Cart::removeBasket: the store " + storeID + " was not found!");
            throw new Exception("Cart::removeBasket: the store " + storeID + " was not found!");
        }
        else
            Log.log.info("The Basket from user"+userID+" of store " + storeID + " was removed from the cart");
        return basket;

    }

    public void changeItemQuantity(int storeID, int itemID, int quantity) throws Exception {
        if(!baskets.containsKey(storeID)){
            Log.log.warning("Cart::changeItemQuantityInCart: the store " + storeID + " was not found!");
            throw new Exception("Cart::changeItemQuantityInCart: the store " + storeID + " was not found!");
        }

        baskets.get(storeID).changeItemQuantity(itemID, quantity, coupons);
        Log.log.info("The quantity of item " + itemID + "was changed");
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
            Log.log.warning("Cart::getItemsInBasket: the store " + storeName + " was not found!");
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
    public HashMap<Integer, HashMap<CatalogItem, CartItemInfo>> buyCart(PurchaseClient purchase, SupplyClient supply
            , PurchaseInfo purchaseInfo, SupplyInfo supplyInfo) throws Exception {
        HashMap<Integer, HashMap<CatalogItem, CartItemInfo>> receiptData = new HashMap<>();

        for(Basket basket : baskets.values()){
            basket.saveItems(coupons);
        }
        Log.log.info("Items of cart " + userID + " are saved");

        //TODO: should change in future versions
        double finalPrice = calculateTotalPrice();

        if(!purchase.handShake()){
            throw new Exception("Problem with connection to external System");
        }


        int purchaseTransId = purchase.pay(purchaseInfo.getCardNumber(), purchaseInfo.getMonth(), purchaseInfo.getYear(),
                purchaseInfo.getHolderName(), purchaseInfo.getCcv(), purchaseInfo.getBuyerId());

        //TODO: should change in future versions
        supply.chooseService();
        int supplyTransId = supply.supply(supplyInfo.getName(), supplyInfo.getAddress(), supplyInfo.getCity(), supplyInfo.getCountry(), supplyInfo.getZip());


        if( purchaseTransId == -1 || supplyTransId == -1 ){
            for(Basket basket : baskets.values()){
                basket.releaseItems();
            }
            supply.cancelSupply(supplyTransId);
            purchase.cancelPay(purchaseTransId);
            throw new Exception("Problem with Supply or Purchase");
        }
        else{
            Log.log.info("Cart " + userID + " payment completed");
            Log.log.info("Cart " + userID + " delivery is scheduled");
        }


        for(Basket basket : baskets.values()){
            receiptData.putIfAbsent(basket.getStore().getStoreID(), basket.buyBasket(userID));
        }

        Log.log.info("All items of " + userID + "are bought");

        empty();
        Log.log.info("Cart " + userID + " is empty");

        return receiptData;
    }

    /**
     * empties the cart
     */
    public void empty(){
        baskets.clear();
        coupons.clear();
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

    public ConcurrentHashMap<Integer, Basket> getBaskets() {
        return baskets.getBaskets();
    }

    public void addCoupon(String coupon) throws Exception {
        if(coupon.isBlank()){
            throw new Exception("Cart::addCoupon: given coupon is invalid!");
        }

        coupons.add(coupon);

        updateBasketsWithCoupons();
    }

    public void removeCoupon(String coupon) throws Exception {
        if(coupon.isBlank()){
            throw new Exception("Cart::removeCoupon: given coupon is invalid!");
        }

        if(!coupons.contains(coupon)){
            throw new Exception("Cart::removeCoupon: given coupon is not in cart!");
        }

        coupons.remove(coupon);

        updateBasketsWithCoupons();
    }

    private void updateBasketsWithCoupons() throws Exception {
        for(Basket basket : baskets.values()){
            basket.updateBasketWithCoupons(coupons);
        }
    }


}
