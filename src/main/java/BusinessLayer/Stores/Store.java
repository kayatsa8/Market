package BusinessLayer.Stores;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Log;
import BusinessLayer.NotificationSystem.Chat;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.NotificationSystem.StoreMailbox;
import BusinessLayer.Receipts.ReceiptHandler;
import BusinessLayer.StorePermissions.StoreEmployees;
import BusinessLayer.StorePermissions.StoreManager;
import BusinessLayer.StorePermissions.StoreOwner;
import BusinessLayer.Stores.Policies.Conditions.LogicalCompositions.*;
import BusinessLayer.Stores.Policies.Conditions.LogicalCompositions.Rules.*;
import BusinessLayer.Stores.Policies.Conditions.NumericCompositions.*;
import BusinessLayer.Stores.Policies.Discounts.Discount;
import BusinessLayer.Stores.Policies.Discounts.DiscountScopes.CategoryDiscount;
import BusinessLayer.Stores.Policies.Discounts.DiscountScopes.DiscountScope;
import BusinessLayer.Stores.Policies.Discounts.DiscountScopes.ItemsDiscount;
import BusinessLayer.Stores.Policies.Discounts.DiscountScopes.StoreDiscount;
import BusinessLayer.Stores.Policies.Discounts.DiscountsTypes.Conditional;
import BusinessLayer.Stores.Policies.Discounts.DiscountsTypes.Hidden;
import BusinessLayer.Stores.Policies.Discounts.DiscountsTypes.Visible;
import BusinessLayer.Stores.Policies.PurchasePolicies.PurchasePolicy;
import Globals.FilterValue;
import Globals.SearchBy;
import Globals.SearchFilter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static BusinessLayer.StorePermissions.StoreActionPermissions.BID_MANAGEMENT;
import static BusinessLayer.Stores.StoreStatus.*;

public class Store {
    private static final Logger log = Log.log;
    private final int founderID;
    private String storeName;
    private final int storeID;
    private int bidsIDs;
    private int lotteriesIDs;
    private int auctionsIDs;
    private int discountsIDs;
    private int purchasePoliciesIDs;
    private StoreMailbox storeMailBox;
    private StoreStatus storeStatus;
    private Map<Integer, Discount> discounts;
    private Map<Integer, PurchasePolicy> purchasePolicies;
    private Map<Integer, CatalogItem> items;
    private Map<Integer, Integer> itemsAmounts;
    private Map<Integer, Integer> savedItemsAmounts;
    private Map<Integer, Bid> bids;
    private Map<Integer, Auction> auctions;
    private Map<Integer, Lottery> lotteries;
    private ReceiptHandler receiptHandler;
    private List<StoreOwner> storeOwners;
    private List<StoreManager> storeManagers;

    public Store(int storeID, int founderID, String name) {
        this.storeID = storeID;
        this.storeName = name;
        this.discounts = new HashMap<>();
        this.purchasePolicies = new HashMap<>();
        this.itemsAmounts = new HashMap<>();
        this.items = new HashMap<>();
        this.savedItemsAmounts = new HashMap<>();
        this.auctions = new HashMap<>();
        this.lotteries = new HashMap<>();
        this.bids = new HashMap<>();
        this.receiptHandler = new ReceiptHandler();
        this.bidsIDs = 0;
        this.lotteriesIDs = 0;
        this.auctionsIDs = 0;
        this.discountsIDs = 0;
        this.purchasePoliciesIDs = 0;
        this.storeStatus = OPEN;
        this.storeManagers = new ArrayList<>();
        this.founderID = founderID;
        this.storeOwners = new ArrayList<>();
        try {
            this.storeMailBox = NotificationHub.getInstance().registerToMailService(this);
        } catch (Exception ignored) {
        }
        log.info("Store " + storeID + " created with name: " + storeName);
    }

    public List<StoreOwner> getStoreOwners() {
        return storeOwners;
    }

    public List<StoreManager> getStoreManagers() {
        return storeManagers;
    }

    public CatalogItem getItem(int itemID) {
        return items.get(itemID);
    }

    public int getItemAmount(int itemID) {
        return itemsAmounts.get(itemID);
    }

    public int getStoreID() {
        return storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Discount getDiscount(int discountID) {
        return discounts.get(discountID);
    }
    public PurchasePolicy getPurchasePolicy(int purchasePolicyID)
    {
        return purchasePolicies.get(purchasePolicyID);
    }

    public ReceiptHandler getReceiptHandler() {
        return receiptHandler;
    }

    public int getFounderID() {
        return founderID;
    }

    public Map<CatalogItem, Boolean> getCatalog() {
        for (Map.Entry<Integer, CatalogItem> item : items.entrySet())
        {
            updateItemDiscounts(item.getKey());
            updateItemPurchasePolicies(item.getKey());
        }
        Map<CatalogItem, Boolean> res = new HashMap<>();
        CatalogItem valueFromA;
        boolean valueFromB;
        for (Map.Entry<Integer, CatalogItem> entry : items.entrySet()) {
            valueFromA = entry.getValue();
            valueFromB = itemsAmounts.get(entry.getKey()) > 0;

            // Put the value from map A as the key and the value from map B as the value in map res
            res.put(valueFromA, valueFromB);
        }
        return res;
    }

    public Map<CatalogItem, Boolean> getCatalog(String keywords, SearchBy searchBy, Map<SearchFilter, FilterValue> filters) throws Exception {
        for (Map.Entry<Integer, CatalogItem> item : items.entrySet())
        {
            updateItemDiscounts(item.getKey());
            updateItemPurchasePolicies(item.getKey());
        }
        Map<CatalogItem, Boolean> res = new HashMap<>();
        CatalogItem valueFromA;
        boolean valueFromB;
        boolean filterResult;
        for (Map.Entry<Integer, CatalogItem> entry : items.entrySet()) {
            valueFromA = entry.getValue();
            valueFromB = itemsAmounts.get(entry.getKey()) > 0;
            if (belongsToSearch(valueFromA, keywords, searchBy)) {
                filterResult = true;
                for (FilterValue filterValue : filters.values()) {
                    filterResult = filterResult && filterValue.filter();
                }
                if (filterResult) {
                    res.put(valueFromA, valueFromB);
                }
            }
        }
        return res;
    }

    private boolean sameCategory(CatalogItem item, String keywords) {
        return (item.getCategory().toLowerCase()).equals(keywords.toLowerCase());
    }

    private boolean sameName(CatalogItem item, String keywords) {
        return (item.getItemName().toLowerCase()).equals(keywords.toLowerCase());
    }

    public boolean belongsToSearch(CatalogItem item, String keywords, SearchBy searchBy) throws Exception {
        switch (searchBy) {
            case CATEGORY -> {
                return sameCategory(item, keywords);
            }
            case ITEM_NAME -> {
                return sameName(item, keywords);
            }
            case KEY_WORD -> {
                {
                    String[] keys = keywords.split(",");
                    for (String key : keys) {
                        key = key.strip();
                        if (sameCategory(item, key) || sameName(item, key)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        throw new Exception("Search by " + searchBy + "is invalid");
    }

    public int addVisibleItemsDiscount(List<Integer> itemsIDs, double percent, Calendar endOfSale) {
        DiscountScope discountScope = new ItemsDiscount(itemsIDs);
        Discount discount = new Visible(discountsIDs, percent, endOfSale, discountScope);
        discounts.put(discountsIDs, discount);
        log.info("Added new visible discount at store " + storeID);
        return discountsIDs++;
    }
    public int addVisibleCategoryDiscount(String category, double percent, Calendar endOfSale) {
        DiscountScope discountScope = new CategoryDiscount(category);
        Discount discount = new Visible(discountsIDs, percent, endOfSale, discountScope);
        discounts.put(discountsIDs, discount);
        log.info("Added new visible discount at store " + storeID);
        return discountsIDs++;
    }
    public int addVisibleStoreDiscount(double percent, Calendar endOfSale) {
        DiscountScope discountScope = new StoreDiscount();
        Discount discount = new Visible(discountsIDs, percent, endOfSale, discountScope);
        discounts.put(discountsIDs, discount);
        log.info("Added new visible discount at store " + storeID);
        return discountsIDs++;
    }
    public int addConditionalItemsDiscount(double percent, Calendar endOfSale, List<Integer> itemsIDs) {
        DiscountScope discountScope = new ItemsDiscount(itemsIDs);
        Discount discount = new Conditional(discountsIDs, percent, endOfSale, discountScope);
        discounts.put(discountsIDs, discount);
        log.info("Added new conditional discount at store " + storeID);
        return discountsIDs++;
    }
    public int addConditionalCategoryDiscount(double percent, Calendar endOfSale, String category) {
        DiscountScope discountScope = new CategoryDiscount(category);
        Discount discount = new Conditional(discountsIDs, percent, endOfSale, discountScope);
        discounts.put(discountsIDs, discount);
        log.info("Added new conditional discount at store " + storeID);
        return discountsIDs++;
    }
    public int addConditionalStoreDiscount(double percent, Calendar endOfSale) {
        DiscountScope discountScope = new StoreDiscount();
        Discount discount = new Conditional(discountsIDs, percent, endOfSale, discountScope);
        discounts.put(discountsIDs, discount);
        log.info("Added new conditional discount at store " + storeID);
        return discountsIDs++;
    }

    public int addHiddenItemsDiscount(List<Integer> itemsIDs, double percent, String coupon, Calendar endOfSale) {
        DiscountScope discountScope = new ItemsDiscount(itemsIDs);
        Discount discount = new Hidden(discountsIDs, percent, endOfSale, coupon, discountScope);
        discounts.put(discountsIDs, discount);
        log.info("Added new hidden discount at store " + storeID);
        return discountsIDs++;
    }
    public int addHiddenCategoryDiscount(String category, double percent, String coupon, Calendar endOfSale) {
        DiscountScope discountScope = new CategoryDiscount(category);
        Discount discount = new Hidden(discountsIDs, percent, endOfSale, coupon, discountScope);
        discounts.put(discountsIDs, discount);
        log.info("Added new hidden discount at store " + storeID);
        return discountsIDs++;
    }
    public int addHiddenStoreDiscount(double percent, String coupon, Calendar endOfSale) {
        DiscountScope discountScope = new StoreDiscount();
        Discount discount = new Hidden(discountsIDs, percent, endOfSale, coupon, discountScope);
        discounts.put(discountsIDs, discount);
        log.info("Added new hidden discount at store " + storeID);
        return discountsIDs++;
    }


    public String addDiscountBasketTotalPriceRule(int discountID, double minimumPrice)
    {
        Conditional discount = (Conditional) getDiscount(discountID);
        return discount.addBasketTotalPriceRule(minimumPrice);
    }
    public String addDiscountQuantityRule(int discountID, Map<Integer, Integer> itemsAmounts)
    {
        Conditional discount = (Conditional) getDiscount(discountID);
        return discount.addQuantityRule(itemsAmounts);
    }
    public String addDiscountComposite(int discountID, LogicalComposites logicalComposite, List<Integer> logicalComponentsIDs) throws Exception
    {
        Conditional discount = (Conditional) getDiscount(discountID);
        return discount.addComposite(logicalComposite, logicalComponentsIDs);
    }
    public String finishConditionalDiscountBuilding(int discountID) throws Exception
    {
        Conditional discount = (Conditional) getDiscount(discountID);
        return discount.finish();
    }
    public int wrapDiscounts(List<Integer> discountsIDsToWrap, NumericComposites numericCompositeEnum) throws Exception
    {
        List<Discount> discountsToWrap = new ArrayList<>();
        for (Integer discountID : discountsIDsToWrap)
        {
            discountsToWrap.add(getDiscount(discountID));
        }
        NumericComposite myNumericComposite = null;
        switch (numericCompositeEnum)
        {
            case ADD:
            {
                myNumericComposite = new Add(discountsIDs, discountsToWrap);
                break;
            }
            case MAX:
            {
                myNumericComposite = new Max(discountsIDs, discountsToWrap);
                break;
            }
            case MIN:
            {
                myNumericComposite = new Min(discountsIDs, discountsToWrap);
                break;
            }
        }
        if (myNumericComposite == null)
            throw new Exception("The numeric composite is unrecognized");
        for (Integer discountID: discountsIDsToWrap)
        {
            discounts.remove(discountID);
        }
        discounts.put(discountsIDs, myNumericComposite);
        return discountsIDs++;
    }


    public String addPurchasePolicyBasketWeightLimitRule(double basketWeightLimit)
    {
        BasketWeightLimitRule basketWeightLimitRule = new BasketWeightLimitRule(basketWeightLimit, purchasePoliciesIDs);
        PurchasePolicy purchasePolicy = new PurchasePolicy(basketWeightLimitRule);
        purchasePolicies.put(purchasePoliciesIDs++, purchasePolicy);
        return (purchasePoliciesIDs-1) + ": " + purchasePolicy.toString();
    }
    public String addPurchasePolicyBuyerAgeRule(int minimumAge)
    {
        BuyerAgeRule buyerAgeRule = new BuyerAgeRule(minimumAge, purchasePoliciesIDs);
        PurchasePolicy purchasePolicy = new PurchasePolicy(buyerAgeRule);
        purchasePolicies.put(purchasePoliciesIDs++, purchasePolicy);
        return (purchasePoliciesIDs-1) + ": " + purchasePolicy.toString();
    }
    public String addPurchasePolicyForbiddenCategoryRule(String forbiddenCategory)
    {
        ForbiddenCategoryRule forbiddenCategoryRule = new ForbiddenCategoryRule(forbiddenCategory, purchasePoliciesIDs);
        PurchasePolicy purchasePolicy = new PurchasePolicy(forbiddenCategoryRule);
        purchasePolicies.put(purchasePoliciesIDs++, purchasePolicy);
        return (purchasePoliciesIDs-1) + ": " + purchasePolicy.toString();
    }
    public String addPurchasePolicyForbiddenDatesRule(List<Calendar> forbiddenDates)
    {
        ForbiddenDatesRule forbiddenDatesRule = new ForbiddenDatesRule(forbiddenDates, purchasePoliciesIDs);
        PurchasePolicy purchasePolicy = new PurchasePolicy(forbiddenDatesRule);
        purchasePolicies.put(purchasePoliciesIDs++, purchasePolicy);
        return (purchasePoliciesIDs-1) + ": " + purchasePolicy.toString();
    }
    public String addPurchasePolicyForbiddenHoursRule(int startHour, int endHour)
    {
        ForbiddenHoursRule forbiddenHoursRule = new ForbiddenHoursRule(startHour, endHour, purchasePoliciesIDs);
        PurchasePolicy purchasePolicy = new PurchasePolicy(forbiddenHoursRule);
        purchasePolicies.put(purchasePoliciesIDs++, purchasePolicy);
        return (purchasePoliciesIDs-1) + ": " + purchasePolicy.toString();
    }
    public String addPurchasePolicyMustDatesRule(List<Calendar> mustDates)
    {
        MustDatesRule mustDatesRule = new MustDatesRule(mustDates, purchasePoliciesIDs);
        PurchasePolicy purchasePolicy = new PurchasePolicy(mustDatesRule);
        purchasePolicies.put(purchasePoliciesIDs++, purchasePolicy);
        return (purchasePoliciesIDs-1) + ": " + purchasePolicy.toString();
    }
    public String addPurchasePolicyItemsWeightLimitRule(Map<Integer, Double> weightsLimits)
    {
        ItemsWeightLimitRule itemsWeightLimitRule = new ItemsWeightLimitRule(weightsLimits, purchasePoliciesIDs);
        PurchasePolicy purchasePolicy = new PurchasePolicy(itemsWeightLimitRule);
        purchasePolicies.put(purchasePoliciesIDs++, purchasePolicy);
        return (purchasePoliciesIDs-1) + ": " + purchasePolicy.toString();
    }
    public String addPurchasePolicyBasketTotalPriceRule(double minimumPrice)
    {
        BasketTotalPriceRule basketTotalPriceRule = new BasketTotalPriceRule(minimumPrice, purchasePoliciesIDs);
        PurchasePolicy purchasePolicy = new PurchasePolicy(basketTotalPriceRule);
        purchasePolicies.put(purchasePoliciesIDs++, purchasePolicy);
        return (purchasePoliciesIDs-1) + ": " + purchasePolicy.toString();
    }
    public String addPurchasePolicyMustItemsAmountsRule(Map<Integer, Integer> itemsAmounts)
    {
        MustItemsAmountsRule mustItemsAmountsRule = new MustItemsAmountsRule(itemsAmounts, purchasePoliciesIDs);
        PurchasePolicy purchasePolicy = new PurchasePolicy(mustItemsAmountsRule);
        purchasePolicies.put(purchasePoliciesIDs++, purchasePolicy);
        return (purchasePoliciesIDs-1) + ": " + purchasePolicy.toString();
    }
    public int wrapPurchasePolicies(List<Integer> purchasePoliciesIDsToWrap, LogicalComposites logicalCompositeEnum) throws Exception
    {
        List<LogicalComponent> policiesRootsToWrap = new ArrayList<>();
        for (Integer policyID : purchasePoliciesIDsToWrap)
        {
            policiesRootsToWrap.add(getPurchasePolicy(policyID).getRoot());
        }
        LogicalComponent myLogicalComponent = null;
        switch (logicalCompositeEnum)
        {
            case AND:
            {
                myLogicalComponent = new And(policiesRootsToWrap, purchasePoliciesIDs);
                break;
            }
            case OR:
            {
                myLogicalComponent = new Or(policiesRootsToWrap, purchasePoliciesIDs);
                break;
            }
            case CONDITIONING:
            {
                if (policiesRootsToWrap.size() != 2)
                    throw new Exception("Conditioning logical component for purchase policy expect 2 purchase policies to wrap, but got " + policiesRootsToWrap.size());
                myLogicalComponent = new Conditioning(policiesRootsToWrap.get(0), policiesRootsToWrap.get(1), purchasePoliciesIDs);
                break;
            }
        }
        if (myLogicalComponent == null)
            throw new Exception("The logical component is unrecognized");
        for (Integer purchasePolicyID: purchasePoliciesIDsToWrap)
        {
            purchasePolicies.remove(purchasePolicyID);
        }
        purchasePolicies.put(purchasePoliciesIDs, new PurchasePolicy(myLogicalComponent));
        return purchasePoliciesIDs++;
    }


    public StoreStatus getStoreStatus() {
        return storeStatus;
    }

    public CatalogItem addCatalogItem(int itemID, String itemName, double itemPrice, String itemCategory, double weight) {
        CatalogItem newItem = new CatalogItem(itemID, itemName, itemPrice, itemCategory, this.storeName, this.storeID, weight);
        itemsAmounts.put(itemID, 0);
        items.put(itemID, newItem);
        savedItemsAmounts.put(itemID, 0);
        log.info("Added new item: " + itemName + ", at store " + storeID);
        return newItem;
    }

    public void buyBasket(List<CartItemInfo> basketItems, int userID) {
        Map<CatalogItem, CartItemInfo> receiptItems = new HashMap<>();
        for (CartItemInfo cartItemInfo : basketItems) {
            int itemID = cartItemInfo.getItemID();
            receiptItems.put(getItem(itemID), cartItemInfo);
            savedItemsAmounts.put(itemID, savedItemsAmounts.get(itemID) - cartItemInfo.getAmount());
        }
        Map<Integer, Map<CatalogItem, CartItemInfo>> receiptInfo = new HashMap<>();
        receiptInfo.put(userID, receiptItems);
        receiptHandler.addReceipt(storeID, receiptInfo);
        List<Integer> sendToList = storeOwners.stream().map(StoreEmployees::getUserID).collect(Collectors.toList());
        storeMailBox.sendMessageToList(sendToList, "User " + userID + " made a purchase in store " + storeName + " where you are one of the owners");
        log.info("A basket was bought at store " + storeID);
    }
    public synchronized boolean saveItemsForUpcomingPurchase(List<CartItemInfo> basketItems, List<String> coupons) throws Exception
    {
        if (checkIfItemsInStock(basketItems))
        {
            if (checkIfBasketPriceChanged(basketItems, coupons))
            {
                updateBasket(basketItems, coupons);
                log.warning("Trying to buy a basket in store: " + storeName + ", but item price or discount changed/removed/added");
                throw new Exception("One or more of the items or discounts in store : " + storeName + " that affect the basket have been changed");
            }
            if (!checkIfPurchaseIsValid(basketItems))
            {
                log.warning("Trying to buy a basket in store: " + storeName + ", but you don't comply with the purchase policies");
                throw new Exception("You don't comply with the purchase policies");
            }
            int itemID;
            int itemAmountToSave;
            for (CartItemInfo cartItemInfo : basketItems)
            {
                itemID = cartItemInfo.getItemID();
                itemAmountToSave = cartItemInfo.getAmount();
                saveItemAmount(itemID, itemAmountToSave);
            }
            log.info("Items was saved for upcoming purchase at store " + storeID);
            return true;
        }
        else
        {
            log.warning("Items wasn't saved for upcoming purchase at store " + storeID + " due to lack of items");
            throw new Exception("Not enough items in stock");
        }
    }

    private boolean checkIfPurchaseIsValid(List<CartItemInfo> basketItems) throws Exception
    {
        for (Map.Entry<Integer, PurchasePolicy> purchasePolicy : purchasePolicies.entrySet())
        {
            if (!purchasePolicy.getValue().isValidForPurchase(basketItems))
            {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfBasketPriceChanged(List<CartItemInfo> basketItems, List<String> coupons)
    {
        List<CartItemInfo> copyBasketItems = new ArrayList<>();
        for (CartItemInfo item : basketItems)
        {
            copyBasketItems.add(new CartItemInfo(item));
        }
        updateBasket(copyBasketItems, coupons);
        for (int i = 0; i<basketItems.size(); i++)
        {
            CartItemInfo item = basketItems.get(i);
            CartItemInfo copyItem = copyBasketItems.get(i);
            if ((item.getOriginalPrice() != copyItem.getOriginalPrice()) || (item.getPercent() != copyItem.getPercent()))
            {
                return false;
            }
        }
        return true;
    }

    public boolean checkIfItemsInStock(List<CartItemInfo> basketItems)
    {
        int itemID;
        int itemAmountToSave;
        int itemCurrentAmount;
        for (CartItemInfo cartItemInfo : basketItems) {
            itemID = cartItemInfo.getItemID();
            itemAmountToSave = cartItemInfo.getAmount();
            itemCurrentAmount = itemsAmounts.get(itemID);
            if (itemCurrentAmount < itemAmountToSave) {
                return false;
            }
        }
        return true;
    }
    
    public void updateBasket(List<CartItemInfo> basketItems, List<String> coupons) //update the items of the basket after any change of the basket
    {
        updateBasketPrices(basketItems);
        if (discounts.size() == 0 || basketItems.size() == 0)
        {
            for (CartItemInfo item : basketItems) {
                item.setPercent(0);
            }
            return;
        }
        List<List<CartItemInfo>> tempBaskets = new ArrayList<>();
        for (Discount discount : discounts.values()) //get separate basket for each discount
        {
            tempBaskets.add(discount.updateBasket(basketItems, coupons));
        }
        for(int i = 0; i<basketItems.size(); i++) //set the original basket to the first temp basket
        {
            basketItems.get(i).setPercent(tempBaskets.get(0).get(i).getPercent());
        }
        if (tempBaskets.size()>1)
        {
            for (int i = 1; i < tempBaskets.size(); i++) //skipping the first temp basket and apply all discount together in the original basket
            {
                List<CartItemInfo> tempBasket = tempBaskets.get(i);
                for (int j = 0; j < basketItems.size(); j++) {
                    double originalItemPercent = basketItems.get(j).getPercent();
                    double tempItemPercent = tempBasket.get(j).getPercent();
                    basketItems.get(j).setPercent(tempItemPercent * (1 - originalItemPercent) + originalItemPercent);
                    /// 40% discount + 30% discount = 58% discount (30% from (100-40=60) is 18, plus 40% = 58%)
                    ///(because 0.3*(1-0.4)+0.4 = 0.58 => 58%)
                }
            }
        }
    }

    private void updateBasketPrices(List<CartItemInfo> basketItems)
    {
        for (CartItemInfo item : basketItems)
        {
            item.setOriginalPrice(getItem(item.getItemID()).getPrice());
        }
    }

    public void saveItemAmount(int itemID, int amountToSave)
    {
        int itemAmountToSave = amountToSave;
        int itemCurrentAmount = itemsAmounts.get(itemID);
        int itemCurrentSavedAmount = savedItemsAmounts.get(itemID);
        if ((itemCurrentAmount - amountToSave >= 0) && (itemCurrentSavedAmount + amountToSave >= 0)) {
            itemsAmounts.put(itemID, itemCurrentAmount - amountToSave);
            savedItemsAmounts.put(itemID, itemCurrentSavedAmount + amountToSave);
        }
    }

    public void reverseSavedItems(List<CartItemInfo> basketItems) throws Exception {
        boolean success = checkIfItemsSaved(basketItems);
        if (success) {
            for (CartItemInfo cartItemInfo : basketItems) {
                saveItemAmount(cartItemInfo.getItemID(), -cartItemInfo.getAmount());
            }
        } else {
            throw new Exception("Somehow the amounts of items to unsave exceed the amounts saved before");
        }
    }

    public boolean checkIfItemsSaved(List<CartItemInfo> basketItems)
    {
        int itemID;
        int itemAmountToRemoveFromSaved;
        int itemCurrentSavedAmount;
        for (CartItemInfo cartItemInfo : basketItems) {
            itemID = cartItemInfo.getItemID();
            itemAmountToRemoveFromSaved = cartItemInfo.getAmount();
            itemCurrentSavedAmount = savedItemsAmounts.get(itemID);
            if (itemCurrentSavedAmount < itemAmountToRemoveFromSaved) {
                return false;
            }
        }
        return true;
    }

    public void addBid(int itemID, int userID, double offeredPrice) {
        saveItemAmount(itemID, 1);
        Bid newBid = new Bid(itemID, userID, offeredPrice);
        List<StoreEmployees> storeOwnersAndManagers = new ArrayList<>();
        storeOwnersAndManagers.addAll(storeOwners);
        storeOwnersAndManagers.addAll(storeManagers.stream().filter(manager -> manager.hasPermission(BID_MANAGEMENT)).toList());
        List<Integer> sendToList = storeOwnersAndManagers.stream().map(StoreEmployees::getUserID).collect(Collectors.toList());
        newBid.setRepliers(sendToList);
        bids.put(bidsIDs++, newBid);
        storeMailBox.sendMessageToList(sendToList, "User " + userID + " offered new bid for item " + items.get(itemID).getItemName() + " at store " + storeName + " with price of " + offeredPrice + " while the original price is " + items.get(itemID).getPrice());
        log.info("Added new bid for item " + itemID + " at store " + storeID);
    }

    public void addLottery(int itemID, double price, int lotteryPeriodInDays) {
        saveItemAmount(itemID, 1);
        lotteries.put(lotteriesIDs, new Lottery(this, lotteriesIDs++, itemID, price, lotteryPeriodInDays));
        log.info("Added new lottery for item " + itemID + " at store " + storeID);
    }

    public void addAuction(int itemID, double initialPrice, int auctionPeriodInDays) {
        saveItemAmount(itemID, 1);
        auctions.put(auctionsIDs, new Auction(this, auctionsIDs++, itemID, initialPrice, auctionPeriodInDays));
        log.info("Added new auction for item " + itemID + " at store " + storeID);
    }

    public void addItemAmount(int itemID, int amountToAdd) {
        int currentAmount = getItemAmount(itemID);
        itemsAmounts.put(itemID, currentAmount + amountToAdd);
        log.info("Added amount by " + amountToAdd + " for item " + itemID + " at store " + storeID);
    }

    public void addSavedItemAmount(int itemID, int amountToRemove)
    {
        int currentAmountSaved = savedItemsAmounts.get(itemID);
        savedItemsAmounts.put(itemID, currentAmountSaved + amountToRemove);
    }

    private void removeBid(int bidID) {
        bids.remove(bidID);
    }

    private void removeAuction(int auctionID) {
        auctions.remove(auctionID);
    }

    private void removeLottery(int lotteryID) {
        lotteries.remove(lotteryID);
    }

    public void finishBidSuccessfully(int bidID) {
        Bid bid = bids.get(bidID);
        int itemID = bid.getItemID();
        int userID = bid.getUserID();
        addSavedItemAmount(itemID, -1);
        removeBid(bidID);
        if (bid.getHighestCounterOffer() == -1) {
            storeMailBox.sendMessage(userID, "Hi, your bid for the item: " + items.get(itemID).getItemName() + ", was approved by the store, and the item will be sent to you soon");
            log.info("Bid " + bidID + " was fully approved");
        } else {
            storeMailBox.sendMessage(userID, "Hi, your bid for the item: " + items.get(itemID).getItemName() + ", was countered by the store with counter-offer of: " + bid.getHighestCounterOffer() + " while the original price is: " + items.get(itemID).getPrice());
            log.info("Bid " + bidID + " was counter-offered with price of " + bid.getHighestCounterOffer());
        }
    }

    public void finishBidUnsuccessfully(int bidID) {
        Bid bid = bids.get(bidID);
        int itemID = bid.getItemID();
        int userID = bid.getUserID();
        addItemAmount(itemID, 1);
        addSavedItemAmount(itemID, -1);
        removeBid(bidID);
        storeMailBox.sendMessage(userID, "Hi, we apologize for the inconvenience, but your bid for the item: " + items.get(itemID).getItemName() + ", was rejected by the store");
        log.info("Bid " + bidID + " was rejected");
    }

    public void finishAuctionSuccessfully(int auctionID) {
        System.out.println("The item is sold to user");
        Auction myAuction = auctions.get(auctionID);
        int winnerID = myAuction.getCurrentWinningUserID();
        int itemID = myAuction.getItemID();
        addSavedItemAmount(myAuction.getItemID(), -1);
        myAuction.getAuctionTimer().cancel();
        myAuction.getAuctionTimer().purge();
        removeAuction(auctionID);
        storeMailBox.sendMessage(winnerID, "Congratulations, you are the winner in our auction in store " + storeName + " of item " + items.get(itemID).getItemName() + " with an offer of " + myAuction.getCurrentPrice() + " while the original price is " + items.get(itemID).getPrice());
        log.info("Auction " + auctionID + " finished successfully and item was sold");
    }

    public void finishAuctionUnsuccessfully(int auctionID) {
        Auction myAuction = auctions.get(auctionID);
        int itemID = myAuction.getItemID();
        addItemAmount(itemID, 1);
        addSavedItemAmount(itemID, -1);
        myAuction.getAuctionTimer().cancel();
        myAuction.getAuctionTimer().purge();
        removeAuction(auctionID);
        log.info("Auction " + auctionID + " finished unsuccessfully and item was not sold");
    }

    public void finishLotterySuccessfully(int lotteryID) {
        Lottery myLottery = lotteries.get(lotteryID);
        int winnerID = myLottery.getWinnerID();
        int itemID = myLottery.getItemID();
        addSavedItemAmount(itemID, -1);
        myLottery.getLotteryTimer().cancel();
        myLottery.getLotteryTimer().purge();
        removeLottery(lotteryID);
        storeMailBox.sendMessage(winnerID, "Congratulations, you are the winner in our lottery in store " + storeName + " of item " + items.get(itemID).getItemName());
        List<Integer> losers = myLottery.getParticipants();
        losers.remove(winnerID);
        storeMailBox.sendMessageToList(losers, "We are sorry, but you lost the lottery in store " + storeName + " of item " + items.get(itemID).getItemName());
        log.info("Lottery " + lotteryID + " finished successfully and item was sold to user " + winnerID);
    }

    public void finishLotteryUnsuccessfully(int lotteryID) {
        Lottery myLottery = lotteries.get(lotteryID);
        int itemID = myLottery.getItemID();
        addItemAmount(itemID, 1);
        addSavedItemAmount(itemID, -1);
        myLottery.getLotteryTimer().cancel();
        myLottery.getLotteryTimer().purge();
        removeLottery(lotteryID);
        List<Integer> participants = myLottery.getParticipants();
        if (participants.size() > 0)
            storeMailBox.sendMessageToList(participants, "We are sorry, but the lottery in store " + storeName + " of item " + items.get(itemID).getItemName() + " has canceled due to lack of demand. Your money will be returned.");
        log.info("Lottery " + lotteryID + " finished unsuccessfully and item was not sold");
    }

    public boolean participateInLottery(int lotteryID, int userID, double offerPrice) {
        Lottery myLottery = lotteries.get(lotteryID);
        boolean participateSuccessfully = myLottery.participateInLottery(userID, offerPrice);
        if (participateSuccessfully) {
            if (myLottery.isLotteryFinished()) {
                finishLotterySuccessfully(lotteryID);
            }
            log.info("User " + userID + " is participating in lottery " + lotteryID);
            return true;
        }
        log.warning("User " + userID + " failed to participate in lottery " + lotteryID);
        return false;
    }

    public boolean offerToAuction(int auctionID, int userID, double offerPrice) {
        Auction myAuction = auctions.get(auctionID);
        double bestOfferBefore = myAuction.getCurrentPrice();
        int winnerBefore = myAuction.getCurrentWinningUserID();
        String itemName = items.get(myAuction.getItemID()).getItemName();
        boolean result = myAuction.offerToAuction(userID, offerPrice);
        double bestOfferNow = myAuction.getCurrentPrice();
        if (result)
            storeMailBox.sendMessage(winnerBefore, "Hi, we want to inform you that other user passed your offer of " + bestOfferBefore + " with an offer of " + bestOfferNow + " at the auction of item " + itemName + " at store " + storeName);
        log.info("User " + userID + " offered to auction " + auctionID + " with price of " + offerPrice);
        return result;
    }

    public boolean approve(int bidID, int replierUserID) throws Exception {
        boolean finishedBid = bids.get(bidID).approve(replierUserID);
        log.info("User " + replierUserID + " approved bid " + bidID);
        if (finishedBid) {
            finishBidSuccessfully(bidID);
            return true;
        }
        return false;
    }

    public boolean reject(int bidID, int replierUserID) throws Exception {
        boolean finishedBid = bids.get(bidID).reject(replierUserID);
        log.info("User " + replierUserID + " rejected bid " + bidID);
        if (finishedBid) {
            finishBidUnsuccessfully(bidID);
            return true;
        }
        return false;
    }

    public boolean counterOffer(int bidID, int replierUserID, double counterOffer) throws Exception {
        boolean finishedBid = bids.get(bidID).counterOffer(replierUserID, counterOffer);
        log.info("User " + replierUserID + " counter-offered bid " + bidID);
        if (finishedBid) {
            finishBidSuccessfully(bidID);
            return true;
        }
        return false;
    }
    
    public boolean reopenStore(int userID) throws Exception
    {
        if (userID != founderID)
            throw new Exception("Only the founder of the store can open it");
        if (storeStatus == OPEN) {
            return false;
        } else if (storeStatus == PERMANENTLY_CLOSE) {
            throw new Exception("Store is permanently close and cannot change its status to open");
        } else {
            storeStatus = OPEN;
            List<StoreEmployees> storeOwnersAndManagers = new ArrayList<>();
            storeOwnersAndManagers.addAll(storeOwners);
            storeOwnersAndManagers.addAll(storeManagers);
            List<Integer> sendToList = storeOwnersAndManagers.stream().map(StoreEmployees::getUserID).collect(Collectors.toList());
            storeMailBox.sendMessageToList(sendToList, "Store " + storeName + " has opened");
            storeMailBox.setMailboxAsAvailable();
            log.info("Store " + storeID + " opened");
            return true;
        }
    }

    private List<Integer> getOwnerIDs() {
        return storeOwners.stream().map(StoreEmployees::getUserID).toList();
    }

    private List<Integer> getManagerIDs() {
        return storeManagers.stream().map(StoreEmployees::getUserID).toList();
    }
    
    public boolean closeStore(int userID) throws Exception
    {
        if (userID != founderID)
            throw new Exception("Only the founder of the store can close it");
        if (storeStatus == CLOSE) {
            return false;
        } else if (storeStatus == PERMANENTLY_CLOSE) {
            throw new Exception("Store is permanently close and cannot change its status to close");
        } else {
            storeStatus = CLOSE;
            List<StoreEmployees> storeOwnersAndManagers = new ArrayList<>();
            storeOwnersAndManagers.addAll(storeOwners);
            storeOwnersAndManagers.addAll(storeManagers);
            List<Integer> sendToList = storeOwnersAndManagers.stream().map(StoreEmployees::getUserID).collect(Collectors.toList());
            storeMailBox.sendMessageToList(sendToList, "Store " + storeName + " has closed");
            storeMailBox.setMailboxAsUnavailable();
            log.info("Store " + storeID + " closed");
            return true;
        }
    }

    public boolean closeStorePermanently() throws Exception {
        if (storeStatus == PERMANENTLY_CLOSE) {
            return false;
        } else {
            storeStatus = PERMANENTLY_CLOSE;
            List<StoreEmployees> storeOwnersAndManagers = new ArrayList<>();
            storeOwnersAndManagers.addAll(storeOwners);
            storeOwnersAndManagers.addAll(storeManagers);
            List<Integer> sendToList = storeOwnersAndManagers.stream().map(StoreEmployees::getUserID).collect(Collectors.toList());
            storeMailBox.sendMessageToList(sendToList, "Store " + storeName + " has closed permanently");
            storeMailBox.setMailboxAsUnavailable();
            storeOwners = new ArrayList<>();
            storeManagers = new ArrayList<>();
            log.info("Store " + storeID + " is permanently closed");
            return true;
        }
    }

    public void addManager(StoreManager manager) {
        this.storeManagers.add(manager);
    }

    public void addOwner(StoreOwner user) {
        this.storeOwners.add(user);
    }

    //Integer instead of int so that it removes by object not index
    public void removeManager(StoreManager manager) {
        this.storeManagers.remove(manager);
    }

    //Integer instead of int so that it removes by object not index
    public void removeOwner(StoreOwner owner) {
        this.storeOwners.remove(owner);
    }

    public CatalogItem removeItemFromStore(int itemID) throws Exception
    {
        if (getItem(itemID) == null)
            return null;
        if (savedItemsAmounts.get(itemID) > 0)
            throw new Exception("Someone is in the middle of a purchase with this item, please try again in a few seconds");
        else
            savedItemsAmounts.remove(itemID);
        if (haveActiveNonEmptyLottery(itemID))
            throw new Exception("Someone participates in a lottery for this item, please try again when the lottery ends");
        else
            removeItemLotteries(itemID);
        if (haveActiveNonEmptyAuction(itemID))
            throw new Exception("Someone participates in an auction for this item, please try again when the auction ends");
        else
            removeItemAuctions(itemID);
        return removeItem(itemID);
    }

    private void removeItemAuctions(int itemID) {
        List<Integer> auctionsIDsToRemove = new ArrayList<>();
        for (Auction auction : auctions.values()) {
            if (auction.getItemID() == itemID)
                auctionsIDsToRemove.add(auction.getAuctionID());
        }
        for (Integer auctionID : auctionsIDsToRemove) {
            auctions.remove(auctionID);
        }
    }

    private void removeItemLotteries(int itemID) {
        List<Integer> lotteriesIDsToRemove = new ArrayList<>();
        for (Lottery lottery : lotteries.values()) {
            if (lottery.getItemID() == itemID)
                lotteriesIDsToRemove.add(lottery.getLotteryID());
        }
        for (Integer lotteryID : lotteriesIDsToRemove) {
            lotteries.remove(lotteryID);
        }
    }

    private boolean haveActiveNonEmptyAuction(int itemID) {
        for (Auction auction : auctions.values()) {
            if (auction.getItemID() == itemID && auction.getCurrentWinningUserID() != -1)
                return true;
        }
        return false;
    }

    private boolean haveActiveNonEmptyLottery(int itemID) {
        for (Lottery lottery : lotteries.values()) {
            if (lottery.getItemID() == itemID && !lottery.getParticipants().isEmpty())
                return true;
        }
        return false;
    }

    private CatalogItem removeItem(int itemID)
    {
        itemsAmounts.remove(itemID);
        return items.remove(itemID);
    }

    public String updateItemName(int itemID, String newName) throws Exception {
        if (items.containsKey(itemID)) {
            return getItem(itemID).setName(newName);
        }
        throw new Exception("Item with ID " + itemID + " is not exist in store " + storeName);
    }

    public Boolean checkIfStoreOwner(int userID) {
        return getOwnerIDs().contains(userID);
    }

    public Boolean checkIfStoreManager(int userID) {
        return getManagerIDs().contains(userID);
    }

    public StoreMailbox getMailBox(){
        return storeMailBox;
    }

    public void sendMessage(int receiverID, String title, String content){
        storeMailBox.sendMessage(receiverID, content);
    }

//    public void markMessageAsRead(Message message) throws Exception {
//        storeMailBox.markMessageAsRead(message);
//    }

//    public void markMessageAsNotRead(Message message) throws Exception {
//        storeMailBox.markMessageAsNotRead(message);
//    }

//    public List<Message> watchNotReadMessages(){
//        return storeMailBox.watchNotReadMessages();
//    }

//    public List<Message> watchReadMessages(){
//        return storeMailBox.watchReadMessages();
//    }

//    public List<Message> watchSentMessages(){
//        return storeMailBox.watchSentMessages();
//    }

    public ConcurrentHashMap<Integer, Chat> getChats(){
        return storeMailBox.getChats();
    }

    public void setMailboxAsUnavailable(){
        storeMailBox.setMailboxAsUnavailable();
    }

    public void setMailboxAsAvailable(){
        storeMailBox.setMailboxAsAvailable();
    }

    public Map<Integer, Discount> getStoreDiscounts()
    {
        return discounts;
    }

    public Map<Integer, Visible> getStoreVisibleDiscounts()
    {
        Map<Integer, Visible> visibleDiscounts = new HashMap<>();
        for (Map.Entry<Integer, Discount> discount : discounts.entrySet())
        {
            if (discount.getValue() instanceof Visible)
            {
                visibleDiscounts.put(discount.getKey(), (Visible) discount.getValue());
            }
        }
        return visibleDiscounts;
    }

    public Map<Integer, PurchasePolicy> getStorePurchasePolicies()
    {
        return purchasePolicies;
    }

    private void updateItemDiscounts(int itemID)
    {
        CatalogItem item = getItem(itemID);
        String category = item.getCategory();
        List<Discount> result = new ArrayList<>();
        for (Discount discount : discounts.values())
        {
            if (discount.isDiscountApplyForItem(itemID, category))
            {
                result.add(discount);
            }
        }
        item.setDiscounts(result);
    }

    private void updateItemPurchasePolicies(int itemID)
    {
        CatalogItem item = getItem(itemID);
        String category = item.getCategory();
        List<PurchasePolicy> result = new ArrayList<>();
        for (PurchasePolicy purchasePolicy : purchasePolicies.values())
        {
            if (purchasePolicy.isPurchasePolicyApplyForItem(itemID, category))
            {
                result.add(purchasePolicy);
            }
        }
        item.setPurchasePolicies(result);
    }
}
