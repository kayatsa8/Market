package ServiceLayer;

import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.NotificationSystem.Chat;
import BusinessLayer.NotificationSystem.Message;
import BusinessLayer.Receipts.Receipt.Receipt;
import BusinessLayer.Stores.Policies.Conditions.LogicalCompositions.LogicalComposites;
import BusinessLayer.Stores.Policies.Conditions.NumericCompositions.NumericComposites;
import BusinessLayer.Stores.Policies.Discounts.Discount;
import BusinessLayer.Stores.Policies.Discounts.DiscountsTypes.Visible;
import BusinessLayer.Stores.Policies.PurchasePolicies.PurchasePolicy;
import Globals.FilterValue;
import Globals.SearchBy;
import Globals.SearchFilter;

import BusinessLayer.Log;
import BusinessLayer.Market;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.CatalogItem;

import ServiceLayer.Objects.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ShoppingService {

    private static final Logger log = Log.log;
    private final Market market;

    public ShoppingService() throws Exception {
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


    public Result<Map<Integer, StoreService>> getAllStoresInfo() {
        try {
            Map<Integer, Store> allStores = market.getAllStores();
            Map<Integer, StoreService> storeServices = new HashMap<>();
            for (Map.Entry<Integer, Store> entry: allStores.entrySet()) {
                storeServices.put(entry.getKey(), new StoreService(entry.getValue()));
            }
            log.info("Stores information received successfully");
            return new Result<>(false, storeServices);
        }
        catch (Exception e){
            log.info("Stores information not received");
            return new Result<>(true, e.getMessage());
        }

    }

    public Result<Map<Integer, StoreService>> getStoresIOwn(int ownerId) {
        try {
            Map<Integer, Store> allStores = market.getStoresIOwn(ownerId);
            Map<Integer, StoreService> storeServices = new HashMap<>();
            for (Map.Entry<Integer, Store> entry: allStores.entrySet()) {
                storeServices.put(entry.getKey(), new StoreService(entry.getValue()));
            }
            log.info("Stores information received successfully");
            return new Result<>(false, storeServices);
        }
        catch (Exception e){
            log.info("Stores information not received");
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
            CatalogItem catalogItem = market.addItemToStore(storeID, itemName, itemPrice, itemCategory, 0);
            CatalogItemService catalogItemService = new CatalogItemService(catalogItem, false);
            log.info("Added new item to store");
            return new Result<>(false, catalogItemService);
        } catch (Exception e) {
            log.info("Failed to add new item to store");
            return new Result<>(true, e.getMessage());
        }
    }


    public Result<Boolean> addItemAmount(int storeId, int itemId, int amount)
    {
        try {
            boolean res = market.addItemAmount(storeId, itemId, amount);
            //Yonatan added boolean, don't delete
            return new Result<>(false, res);
        } catch (Exception e) {
            log.info("Failed to add amount of item to store");
            return new Result<>(true, e.getMessage());
        }
    }


    public Result<CatalogItemService> removeItemFromStore(int storeID, int itemID)
    {
        try {
            CatalogItem item = market.removeItemFromStore(storeID, itemID);
            CatalogItemService catalogItemService = new CatalogItemService(item, false);
            log.info("Removed item from store");
            return new Result<>(false, catalogItemService);
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

    public Result<Boolean> sendMessage(int storeID, int receiverID, String title, String content)
    {
        try
        {
            boolean response = market.sendMessage(storeID, receiverID, title, content);
            log.info("Sent message successfully");
            return new Result<>(false, response);
        } catch (Exception e)
        {
            log.info("Failed to send message");
            return new Result<>(true, e.getMessage());
        }


    }

//    public Result<Boolean> markMessageAsRead(int storeID, MessageService messageService){
//        try{
//            market.markMessageAsRead(storeID, new Message(messageService));
//            return new Result<Boolean>(false, true);
//        }
//        catch(Exception e){
//            return new Result<Boolean>(true, e.getMessage());
//        }
//    }
//
//    public Result<Boolean> markMessageAsNotRead(int storeID, MessageService messageService){
//        try{
//            market.markMessageAsNotRead(storeID, new Message(messageService));
//            return new Result<Boolean>(false, true);
//        }
//        catch(Exception e){
//            return new Result<Boolean>(true, e.getMessage());
//        }
//    }

//    public Result<List<MessageService>> watchNotReadMessages(int storeID) throws Exception
//    {
//        List<Message> messages = market.watchNotReadMessages(storeID);
//
//        if(messages == null){
//            return new Result<>(true, "Error");
//        }
//        else{
//            return new Result<>(false, messageListToMessageServiceList(messages));
//        }
//    }
//
//    private List<MessageService> messageListToMessageServiceList(List<Message> messages){
//        List<MessageService> toReturn = new ArrayList<>();
//
//        for(Message message : messages){
//            toReturn.add(new MessageService(message));
//        }
//
//        return toReturn;
//    }
//
//    public Result<List<MessageService>> watchReadMessages(int storeID) throws Exception
//    {
//        List<Message> messages = market.watchReadMessages(storeID);
//
//        if(messages == null){
//            return new Result<List<MessageService>>(true, "Error");
//        }
//        else{
//            return new Result<List<MessageService>>(false, messageListToMessageServiceList(messages));
//        }
//    }
//
//    public Result<List<MessageService>> watchSentMessages(int storeID) throws Exception
//    {
//        List<Message> messages = market.watchSentMessages(storeID);
//
//        if(messages == null){
//            return new Result<List<MessageService>>(true, "Error");
//        }
//        else{
//            return new Result<List<MessageService>>(false, messageListToMessageServiceList(messages));
//        }
//    }

    public Result<Boolean> setMailboxAsUnavailable(int storeID) throws Exception
    {
        boolean answer = market.setMailboxAsUnavailable(storeID);

        if(answer){
            return new Result<Boolean>(true, "Success");
        }
        else{
            return new Result<Boolean>(false, "Failure");
        }
    }

    public Result<Boolean> setMailboxAsAvailable(int storeID) throws Exception
    {
        boolean answer = market.setMailboxAsAvailable(storeID);

        if(answer){
            return new Result<Boolean>(true, "Success");
        }
        else{
            return new Result<Boolean>(false, "Failure");
        }
    }

    public Result<List<ReceiptService>> getSellingHistoryOfStoreForManager(int storeId, int userId) {
//
//
//        //for testing
//        List<ReceiptService> receiptServices = new ArrayList<>();
//        Receipt receipt1 = new Receipt(10, 1, Calendar.getInstance());
//        Receipt receipt2 = new Receipt(11, 2, Calendar.getInstance());
//        List<ReceiptItem> items = new ArrayList<>();
//        items.add(new ReceiptItem(1, "name", 20, 12, 13));
//        items.add(new ReceiptItem(2, "name2", 30, 12, 13));
//        receipt1.addItems(1, items);
//        receipt1.addItems(100002, items);
//        receiptServices.add(new ReceiptService(receipt1));
//        receiptServices.add(new ReceiptService(receipt2));
//        return new Result<>(false, receiptServices);

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

    public Result<Integer> addVisibleItemsDiscount(int storeID, List<Integer> itemsIDs, double percent, Calendar endOfSale)
    {
        try {
            int id = market.addVisibleItemsDiscount(storeID, itemsIDs, percent, endOfSale);
            log.info("Added new visible discount");
            return new Result<>(false, id);
        } catch (Exception e) {
            log.info("Failed to add new visible discount");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Integer> addVisibleCategoryDiscount(int storeID, String category, double percent, Calendar endOfSale)
    {
        try {
            int id = market.addVisibleCategoryDiscount(storeID, category, percent, endOfSale);
            log.info("Added new visible discount");
            return new Result<>(false, id);
        } catch (Exception e) {
            log.info("Failed to add new visible discount");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Integer> addVisibleStoreDiscount(int storeID, double percent, Calendar endOfSale)
    {
        try {
            int id = market.addVisibleStoreDiscount(storeID, percent, endOfSale);
            log.info("Added new visible discount");
            return new Result<>(false, id);
        } catch (Exception e) {
            log.info("Failed to add new visible discount");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Integer> addConditionalItemsDiscount(int storeID, double percent, Calendar endOfSale, List<Integer> itemsIDs)
    {
        try {
            int id = market.addConditionalItemsDiscount(storeID, percent, endOfSale, itemsIDs);
            log.info("Added new conditional discount");
            return new Result<>(false, id);
        } catch (Exception e) {
            log.info("Failed to add new conditional discount");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Integer> addConditionalCategoryDiscount(int storeID, double percent, Calendar endOfSale, String category)
    {
        try {
            int id = market.addConditionalCategoryDiscount(storeID, percent, endOfSale, category);
            log.info("Added new conditional discount");
            return new Result<>(false, id);
        } catch (Exception e) {
            log.info("Failed to add new conditional discount");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Integer> addConditionalStoreDiscount(int storeID, double percent, Calendar endOfSale)
    {
        try {
            int id = market.addConditionalStoreDiscount(storeID, percent, endOfSale);
            log.info("Added new conditional discount");
            return new Result<>(false, id);
        } catch (Exception e) {
            log.info("Failed to add new conditional discount");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Integer> addHiddenItemsDiscount(int storeID, List<Integer> itemsIDs, double percent, String coupon, Calendar endOfSale)
    {
        try {
            int id = market.addHiddenItemsDiscount(storeID, itemsIDs, percent, coupon, endOfSale);
            log.info("Added new hidden discount");
            return new Result<>(false, id);
        } catch (Exception e) {
            log.info("Failed to add new hidden discount");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Integer> addHiddenCategoryDiscount(int storeID, String category, double percent, String coupon, Calendar endOfSale)
    {
        try {
            int id = market.addHiddenCategoryDiscount(storeID, category, percent, coupon, endOfSale);
            log.info("Added new hidden discount");
            return new Result<>(false, id);
        } catch (Exception e) {
            log.info("Failed to add new hidden discount");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Integer> addHiddenStoreDiscount(int storeID, double percent, String coupon, Calendar endOfSale)
    {
        try {
            int id = market.addHiddenStoreDiscount(storeID, percent, coupon, endOfSale);
            log.info("Added new hidden discount");
            return new Result<>(false, id);
        } catch (Exception e) {
            log.info("Failed to add new hidden discount");
            return new Result<>(true, e.getMessage());
        }
    }


    public Result<Boolean> addDiscountBasketTotalPriceRule(int storeID, int discountID, double minimumPrice)
    {
        try {
            String result = market.addDiscountBasketTotalPriceRule(storeID, discountID, minimumPrice);
            log.info("Added new hidden discount");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add new hidden discount");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Boolean> addDiscountQuantityRule(int storeID, int discountID, Map<Integer, Integer> itemsAmounts)
    {
        try {
            String result = market.addDiscountQuantityRule(storeID, discountID, itemsAmounts);
            log.info("Added new hidden discount");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add new hidden discount");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Boolean> addDiscountComposite(int storeID, int discountID, LogicalComposites logicalComposite, List<Integer> logicalComponentsIDs)
    {
        try {
            String result = market.addDiscountComposite(storeID, discountID, logicalComposite, logicalComponentsIDs);
            log.info("Added new hidden discount");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add new hidden discount");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Boolean> finishConditionalDiscountBuilding(int storeID, int discountID)
    {
        try {
            String result = market.finishConditionalDiscountBuilding(storeID, discountID);
            log.info("Added new hidden discount");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add new hidden discount");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Integer> wrapDiscounts(int storeID, List<Integer> discountsIDsToWrap, NumericComposites numericCompositeEnum)
    {
        try {
            int id = market.wrapDiscounts(storeID, discountsIDsToWrap, numericCompositeEnum);
            log.info("Wrapped discounts successfully");
            return new Result<>(false, id);
        } catch (Exception e) {
            log.info("Failed to wrap discounts");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<String> addPurchasePolicyBasketWeightLimitRule(int storeID, double basketWeightLimit)
    {
        try {
            String result = market.addPurchasePolicyBasketWeightLimitRule(storeID, basketWeightLimit);
            log.info("Succeeded to purchase policy of basket weight limit rule");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add purchase policy of basket weight limit rule");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<String> addPurchasePolicyBuyerAgeRule(int storeID, int minimumAge)
    {
        try {
            String result = market.addPurchasePolicyBuyerAgeRule(storeID, minimumAge);
            log.info("Succeeded to purchase policy of buyer age rule");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add purchase policy of buyer age rule");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<String> addPurchasePolicyForbiddenCategoryRule(int storeID, String forbiddenCategory)
    {
        try {
            String result = market.addPurchasePolicyForbiddenCategoryRule(storeID, forbiddenCategory);
            log.info("Succeeded to purchase policy of forbidden category rule");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add purchase policy of forbidden category rule");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<String> addPurchasePolicyForbiddenDatesRule(int storeID, List<Calendar> forbiddenDates)
    {
        try {
            String result = market.addPurchasePolicyForbiddenDatesRule(storeID, forbiddenDates);
            log.info("Succeeded to purchase policy of forbidden dates rule");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add purchase policy of forbidden dates rule");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<String> addPurchasePolicyForbiddenHoursRule(int storeID, int startHour, int endHour)
    {
        try {
            String result = market.addPurchasePolicyForbiddenHoursRule(storeID, startHour, endHour);
            log.info("Succeeded to purchase policy of forbidden hours rule");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add purchase policy of forbidden hours rule");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<String> addPurchasePolicyMustDatesRule(int storeID, List<Calendar> mustDates)
    {
        try {
            String result = market.addPurchasePolicyMustDatesRule(storeID, mustDates);
            log.info("Succeeded to purchase policy of must dates rule");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add purchase policy of must dates rule");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<String> addPurchasePolicyItemsWeightLimitRule(int storeID, Map<Integer, Double> weightsLimits)
    {
        try {
            String result = market.addPurchasePolicyItemsWeightLimitRule(storeID, weightsLimits);
            log.info("Succeeded to purchase policy of items weight limit rule");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add purchase policy of items weight limit rule");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<String> addPurchasePolicyBasketTotalPriceRule(int storeID, double minimumPrice)
    {
        try {
            String result = market.addPurchasePolicyBasketTotalPriceRule(storeID, minimumPrice);
            log.info("Succeeded to purchase policy of basket total price rule");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add purchase policy of basket total price rule");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<String> addPurchasePolicyMustItemsAmountsRule(int storeID, Map<Integer, Integer> itemsAmounts)
    {
        try {
            String result = market.addPurchasePolicyMustItemsAmountsRule(storeID, itemsAmounts);
            log.info("Succeeded to purchase policy of must items amounts rule");
            return new Result<>(false, result);
        } catch (Exception e) {
            log.info("Failed to add purchase policy of must items amounts rule");
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Integer> wrapPurchasePolicies(int storeID, List<Integer> purchasePoliciesIDsToWrap, LogicalComposites logicalCompositeEnum)
    {
        try {
            int id = market.wrapPurchasePolicies(storeID, purchasePoliciesIDsToWrap, logicalCompositeEnum);
            log.info("Succeeded to wrap purchase policies under logical composite of " + logicalCompositeEnum.name());
            return new Result<>(false, id);
        } catch (Exception e) {
            log.info("Failed to wrap purchase policies under logical composite of " + logicalCompositeEnum.name());
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> getStoreDiscounts(int storeID) //Change from boolean to result of service discounts
    {
        try {
            Map<Integer, Discount> discounts = market.getStoreDiscounts(storeID);
            log.info("Got all discounts of store " + storeID);
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("Failed to get discounts of store " + storeID);
            return new Result<>(true, e.getMessage());
        }
    }
    public Result<Boolean> getStoreVisibleDiscounts(int storeID) //Change from boolean to result of service discounts
    {
        try {
            Map<Integer, Visible> visibleDiscounts = market.getStoreVisibleDiscounts(storeID);
            log.info("Got all visible discounts of store " + storeID);
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("Failed to get visible discounts of store " + storeID);
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> getStorePurchasePolicies(int storeID) //Change from boolean to result of service purchase policies
    {
        try {
            Map<Integer, PurchasePolicy> purchasePolicies = market.getStorePurchasePolicies(storeID);
            log.info("Got all purchase policies of store " + storeID);
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("Failed to get purchase policies of store " + storeID);
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<HashMap<Integer, ChatService>> getChats(int storeId){
        try{
            ConcurrentHashMap<Integer, Chat> _chats = market.getChats(storeId);
            HashMap<Integer, ChatService> chats = new HashMap<>();

            for(Integer id : _chats.keySet()){
                chats.put(id, new ChatService(_chats.get(id)));
            }

            return new Result<>(false, chats);
        }
        catch(Exception e){
            return new Result<>(true, e.getMessage());
        }

    }
}