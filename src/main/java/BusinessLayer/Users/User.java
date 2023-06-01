package BusinessLayer.Users;

import BusinessLayer.CartAndBasket.Basket;
import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.ExternalSystems.Purchase.PurchaseClient;
import BusinessLayer.ExternalSystems.PurchaseInfo;
import BusinessLayer.ExternalSystems.Supply.SupplyClient;
import BusinessLayer.Market;
import BusinessLayer.NotificationSystem.Chat;
import BusinessLayer.NotificationSystem.Observer.NotificationObserver;
import BusinessLayer.NotificationSystem.UserMailbox;
import BusinessLayer.ExternalSystems.SupplyInfo;
import BusinessLayer.Receipts.ReceiptHandler;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class User {
    protected Cart cart;
    protected LocalDate bDay=null;
    protected String address=null;
    protected ReceiptHandler receiptHandler;
    protected int id;
    protected UserMailbox mailbox;

    public User(int id) throws Exception {
        this.id = id;
        this.cart = new Cart(id);
        this.receiptHandler = new ReceiptHandler();
    }

    public Cart getCart() {
        return cart;
    }

    public int getId() {
        return id;
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

    public Cart buyCart(PurchaseInfo purchaseInfo, SupplyInfo supplyInfo) throws Exception {
        receiptHandler.addReceipt(id,cart.buyCart(new PurchaseClient(), new SupplyClient(), purchaseInfo, supplyInfo));
        return cart;
    }

    /**
     * empties the cart
     */
    public Cart emptyCart() {
        cart.empty();
        return cart;
    }

    public void addCouponToCart(String coupon) throws Exception {
        cart.addCoupon(coupon);
    }

    public void removeCouponFromCart(String coupon) throws Exception {
        cart.removeCoupon(coupon);
    }

    public Basket removeBasketFromCart(int storeID) throws Exception {
        return cart.removeBasket(storeID);
    }

    public UserMailbox getMailbox(){
        return mailbox;
    }

    public void sendMessage(int receiverID, String content){
        mailbox.sendMessage(receiverID, content);
    }

    public ConcurrentHashMap<Integer, Chat> getChats(){
        return mailbox.getChats();
    }

    public void listenToNotifications(NotificationObserver listener){
        mailbox.listen(listener);
    }

    public LocalDate getbDay() {
        return bDay;
    }

    public void setbDay(LocalDate bDay) {
        this.bDay = bDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
