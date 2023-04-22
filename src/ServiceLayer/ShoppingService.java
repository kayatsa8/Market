package ServiceLayer;

import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.NotificationSystem.Message;
import BusinessLayer.Receipts.Receipt.Receipt;
import Globals.FilterValue;
import Globals.SearchBy;
import Globals.SearchFilter;

import BusinessLayer.Log;
import BusinessLayer.Market;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.CatalogItem;

import ServiceLayer.Objects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ShoppingService {

    private static final Logger log = Log.log;
    private final Market market;

    public ShoppingService() {
        market = Market.getInstance();
    }

    public Result<List<CatalogItemService>> getCatalog() {
        Map<CatalogItem, Boolean> catalog = market.getCatalog();
        List<CatalogItemService> catalogService = new ArrayList<>();
        for (Map.Entry<CatalogItem, Boolean> entry : catalog.entrySet()) {
            catalogService.add(new CatalogItemService(entry.getKey(), entry.getValue()));
        }
        return new Result<>(false, catalogService);
    }

    public Result<List<CatalogItemService>> searchCatalog(String keywords, SearchBy searchBy, Map<SearchFilter, FilterValue> filters) {
        try {
            Map<CatalogItem, Boolean> catalog = market.searchCatalog(keywords, searchBy, filters);
            List<CatalogItemService> catalogService = new ArrayList<>();
            for (Map.Entry<CatalogItem, Boolean> entry : catalog.entrySet()) {
                catalogService.add(new CatalogItemService(entry.getKey(), entry.getValue()));
            }
            return new Result<>(false, catalogService);
        }
        catch (Exception e) {
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<CartService> getCart(int userID) {
        try {
            Cart cart = market.getCart(userID);
            return new Result<>(false, new CartService(cart));
        }
        catch (Exception e) {
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<CartService> addItemToCart(int userID, int storeID, int itemID, int quantity) throws Exception {
        try {
            Cart cart = market.addItemToCart(userID, storeID, itemID, quantity);
            return new Result<>(false, new CartService(cart));
        }
        catch (Exception e) {
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<CartService> removeItemFromCart(int userID, int storeID, int itemID) throws Exception {
        try {
            Cart cart = market.removeItemFromCart(userID, storeID, itemID);
            return new Result<>(false, new CartService(cart));
        }
        catch (Exception e) {
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<CartService> changeItemQuantityInCart(int userID, int storeID, int itemID, int quantity) throws Exception {
        try {
            Cart cart = market.changeItemQuantityInCart(userID, storeID, itemID, quantity);
            return new Result<>(false, new CartService(cart));
        }
        catch (Exception e) {
            return new Result<>(true, e.getMessage());
        }
    }

    /**
     * this method is used to show the costumer all the stores he added,
     * he can choose one of them and see what is inside with getItemsInBasket
     *
     * @return List<String> @TODO maybe should be of some kind of object?
     */
    public Result<List<String>> getStoresOfBaskets(int userID) {
        try {
            return new Result<>(false, market.getStoresOfBaskets(userID));
        }
        catch (Exception e) {
            return new Result<>(true, e.getMessage());
        }
    }

    public HashMap<CatalogItem, CartItemInfo> getItemsInBasket(int userID, String storeName) throws Exception {
        return market.getItemsInBasket(userID, storeName);
    }

    public Result buyCart(int userID, String address) throws Exception {
        try {
            market.buyCart(userID, address);
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("Cart could not be purchased");
            return new Result<>(true, e.getMessage());
        }
    }

    /**
     * empties the cart
     */
    public Result emptyCart(int userID) {
        try {
            market.emptyCart(userID);
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("Cart could not be emptied");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<StoreService> getStoreInfo(int storeID) {
        try {
            Store store = market.getStoreInfo(storeID);
            StoreService storeService = new StoreService(store);
            log.info("Store information received successfully");
            return new Result<>(false, storeService);
        } catch (Exception e) {
            log.info("Store information not received");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<StoreService> getStoreInfoAsStoreManager(int storeID, int userID) {
        try {
            Store store = market.getStoreInfo(storeID);
            StoreService storeService = new StoreService(store);
            log.info("Store information received successfully as store manager");
            return new Result<>(false, storeService);
        } catch (Exception e) {
            log.info("Store information not received");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<CatalogItemService> addItemToStore(int storeID, String itemName, double itemPrice, String itemCategory)
    {
        try {
            CatalogItem catalogItem = market.addItemToStore(storeID, itemName, itemPrice, itemCategory);
            CatalogItemService catalogItemService = new CatalogItemService(catalogItem, false);
            log.info("Added new item to store");
            return new Result<>(false, catalogItemService);
        } catch (Exception e) {
            log.info("Failed to add new item to store");
            return new Result<>(true, e.getMessage());
        }
    }


    public void addItemAmount(int storeId, int itemId, int amount){
        market.addItemAmount(storeId, itemId, amount);
    }


    public Result<String> removeItemFromStore(int storeID, int itemID)
    {
        try {
            market.removeItemFromStore(storeID, itemID);
            log.info("Removed item from store");
            return new Result<>(false, "Item removed");
        } catch (Exception e) {
            log.info("Failed to remove item from store");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<String> updateItemName(int storeID, int itemID, String newName)
    {
        try {
            String oldItemName = market.updateItemName(storeID, itemID, newName);
            log.info("Changed item name from " + oldItemName + " to " + newName);
            return new Result<>(false, "Changed item name from " + oldItemName + " to " + newName);
        } catch (Exception e) {
            log.info("Failed to change item name");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> checkIfStoreOwner(int userID, int storeID)
    {
        try {
            Boolean isStoreOwner = market.checkIfStoreOwner(userID, storeID);
            log.info("Checked if user " + userID + " is store owner at store " + storeID);
            return new Result<>(false, isStoreOwner);
        } catch (Exception e) {
            log.info("Failed to check if user " + userID + " is store owner at store " + storeID);
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Boolean> checkIfStoreManager(int userID, int storeID)
    {
        try {
            Boolean isStoreManager = market.checkIfStoreManager(userID, storeID);
            log.info("Checked if user " + userID + " is store manager at store " + storeID);
            return new Result<>(false, isStoreManager);
        } catch (Exception e) {
            log.info("Failed to check if user " + userID + " is store manager at store " + storeID);
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> reopenStore(int userID, int storeID)
    {
        try {
            Boolean reopenStore = market.reopenStore(userID, storeID);
            log.info("Open store");
            return new Result<>(false, reopenStore);
        } catch (Exception e) {
            log.info("Failed to reopen store " + storeID);
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> closeStore(int userID, int storeID)
    {
        try {
            Boolean closeStore = market.closeStore(userID, storeID);
            log.info("Close store");
            return new Result<>(false, closeStore);
        } catch (Exception e) {
            log.info("Failed to close store " + storeID);
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> closeStorePermanently(int userID, int storeID)
    {
        try {
            Boolean closeStorePermanently = market.closeStorePermanently(userID, storeID);
            log.info("Close store permanently");
            return new Result<>(false, closeStorePermanently);
        } catch (Exception e) {
            log.info("Failed to close store " + storeID + " permanently");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Integer> createStore(int userID, String storeName)
    {
        try {
            int storeID = market.addStore(userID, storeName);
            log.info("Create new store");
            return new Result<>(false, storeID);
        } catch (Exception e) {
            log.info("Failed to create new store");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> sendMessage(int storeID, int receiverID, String title, String content){
        boolean response = market.sendMessage(storeID, receiverID, title, content);

        if(response){
            return new Result<Boolean>(true, "Message was sent");
        }
        else{
            return new Result<Boolean>(false, "Message was not sent");
        }

    }

    public Result<Boolean> markMessageAsRead(int storeID, MessageService messageService){
        try{
            market.markMessageAsRead(storeID, new Message(messageService));
            return new Result<Boolean>(true, "Success");
        }
        catch(Exception e){
            return new Result<Boolean>(false, "Failure");
        }
    }

    public Result<Boolean> markMessageAsNotRead(int storeID, MessageService messageService){
        try{
            market.markMessageAsNotRead(storeID, new Message(messageService));
            return new Result<Boolean>(true, "Success");
        }
        catch(Exception e){
            return new Result<Boolean>(false, "Failure");
        }
    }

    public Result<List<MessageService>> watchNotReadMessages(int storeID){
        List<Message> messages = market.watchNotReadMessages(storeID);

        if(messages == null){
            return new Result<>(true, "Error");
        }
        else{
            return new Result<>(false, messageListToMessageServiceList(messages));
        }
    }

    private List<MessageService> messageListToMessageServiceList(List<Message> messages){
        List<MessageService> toReturn = new ArrayList<>();

        for(Message message : messages){
            toReturn.add(new MessageService(message));
        }

        return toReturn;
    }

    public Result<List<MessageService>> watchReadMessages(int storeID){
        List<Message> messages = market.watchReadMessages(storeID);

        if(messages == null){
            return new Result<List<MessageService>>(false, "Error");
        }
        else{
            return new Result<List<MessageService>>(true, messageListToMessageServiceList(messages));
        }
    }

    public Result<List<MessageService>> watchSentMessages(int storeID){
        List<Message> messages = market.watchSentMessages(storeID);

        if(messages == null){
            return new Result<List<MessageService>>(false, "Error");
        }
        else{
            return new Result<List<MessageService>>(true, messageListToMessageServiceList(messages));
        }
    }

    public Result<Boolean> setMailboxAsUnavailable(int storeID){
        boolean answer = market.setMailboxAsUnavailable(storeID);

        if(answer){
            return new Result<Boolean>(true, "Success");
        }
        else{
            return new Result<Boolean>(false, "Failure");
        }
    }

    public Result<Boolean> setMailboxAsAvailable(int storeID){
        boolean answer = market.setMailboxAsAvailable(storeID);

        if(answer){
            return new Result<Boolean>(true, "Success");
        }
        else{
            return new Result<Boolean>(false, "Failure");
        }
    }

    public Result<List<ReceiptService>> getSellingHistoryOfStoreForManager(int storeId, int userId) {

        try {
            List<Receipt> result = market.getSellingHistoryOfStoreForManager(storeId, userId);
            return new Result<>(false, receiptsToReceiptsService(result));
        } catch (Exception e) {
            return new Result<>(true, null);
        }

    }

    private List<ReceiptService> receiptsToReceiptsService(List<Receipt> receipts) {
        List<ReceiptService> result = new ArrayList<>();
        for(Receipt receipt: receipts){
            result.add(new ReceiptService(receipt));
        }
        return result;
    }
}