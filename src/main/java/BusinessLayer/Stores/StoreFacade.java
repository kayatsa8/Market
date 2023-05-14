package BusinessLayer.Stores;

import BusinessLayer.NotificationSystem.Chat;
import BusinessLayer.Stores.Conditions.LogicalCompositions.LogicalComposites;
import BusinessLayer.Stores.Conditions.NumericCompositions.NumericComposites;
import BusinessLayer.Stores.Policies.DiscountPolicy;
import BusinessLayer.Stores.Discounts.Discount;
import BusinessLayer.Stores.Discounts.DiscountsTypes.Visible;
import BusinessLayer.Stores.Policies.PurchasePolicy;
import Globals.FilterValue;
import Globals.SearchBy;
import Globals.SearchFilter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static Globals.SearchFilter.STORE_RATING;

public class StoreFacade {
    private Map<Integer, Store> stores;
    private Set<String> categoryPool;
    private int storesIDs;
    private int itemsIDs;

    public StoreFacade() {
        this.stores = new HashMap<>();
        this.categoryPool = new HashSet<>();
        this.storesIDs = 0;
        this.itemsIDs = 0;
    }

    public Store addStore(int founderID, String name) {
        Store newStore = new Store(storesIDs, founderID, name);
        stores.put(storesIDs++, newStore);
        return newStore;
    }

    public void setStoreName(int storeID, String storeName) throws Exception
    {
        Store store = getStore(storeID);
        store.setStoreName(storeName);
    }


    public Store getStore(int storeID) throws Exception
    {
        Store store = stores.get(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store;
    }

    public CatalogItem getItem(int storeID, int itemID) throws Exception
    {
        Store store = getStore(storeID);
        return store.getItem(itemID);
    }
    public CatalogItem addCatalogItem(int storeID, String itemName, double itemPrice, String itemCategory, double weight) throws Exception
    {
        Store store = getStore(storeID);
        if (itemPrice <= 0)
            throw new Exception("Item price has to be positive but is " + itemPrice);
        categoryPool.add(itemCategory);
        return store.addCatalogItem(itemsIDs++, itemName, itemPrice, itemCategory, weight);
    }
    public int getItemAmount(int storeID, int itemID) throws Exception
    {
        Store store = getStore(storeID);
        return store.getItemAmount(itemID);
    }
    public boolean addItemAmount(int storeID, int itemID, int amountToAdd) throws Exception
    {
        Store store = getStore(storeID);
        store.addItemAmount(itemID, amountToAdd);

        //Yonatan added it, don't delete
        return true;
    }
    public void addBid(int storeID, int itemID, int userID, double offeredPrice) throws Exception
    {
        Store store = getStore(storeID);
        store.addBid(itemID, userID, offeredPrice);
    }
    public void addLottery(int storeID, int itemID, double price, int lotteryPeriodInDays) throws Exception
    {
        Store store = getStore(storeID);
        store.addLottery(itemID, price, lotteryPeriodInDays);
    }
    public void addAuction(int storeID, int itemID, double initialPrice, int auctionPeriodInDays) throws Exception
    {
        Store store = getStore(storeID);
        store.addAuction(itemID, initialPrice, auctionPeriodInDays);
    }
    public boolean participateInLottery(int storeID, int lotteryID, int userID, double offerPrice) throws Exception
    {
        Store store = getStore(storeID);
        return store.participateInLottery(lotteryID, userID, offerPrice);
    }
    public boolean offerToAuction(int storeID, int auctionID, int userID, double offerPrice) throws Exception
    {
        Store store = getStore(storeID);
        return store.offerToAuction(auctionID, userID, offerPrice);
    }
    public boolean approve(int storeID, int bidID, int replierUserID) throws Exception
    {
        Store store = getStore(storeID);
        return store.approve(bidID, replierUserID);
    }
    public boolean reject(int storeID, int bidID, int replierUserID) throws Exception
    {
        Store store = getStore(storeID);
        return store.reject(bidID, replierUserID);
    }
    public boolean counterOffer(int storeID, int bidID, int replierUserID, double counterOffer) throws Exception
    {
        Store store = getStore(storeID);
        return store.counterOffer(bidID, replierUserID, counterOffer);
    }

    public boolean reopenStore(int userID, int storeID) throws Exception {
        return getStore(storeID).reopenStore(userID);
    }

    public boolean closeStore(int userID, int storeID) throws Exception {
        return getStore(storeID).closeStore(userID);
    }

    public boolean closeStorePermanently(int storeID) throws Exception {
        return getStore(storeID).closeStorePermanently();
    }

    public int addVisibleItemsDiscount(int storeID, List<Integer> itemsIDs, double percent, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        return store.addVisibleItemsDiscount(itemsIDs, percent, endOfSale);
    }
    public int addVisibleCategoryDiscount(int storeID, String category, double percent, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        return store.addVisibleCategoryDiscount(category, percent, endOfSale);
    }
    public int addVisibleStoreDiscount(int storeID, double percent, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        return store.addVisibleStoreDiscount(percent, endOfSale);
    }
    public int addConditionalItemsDiscount(int storeID, double percent, Calendar endOfSale, List<Integer> itemsIDs) throws Exception
    {
        Store store = getStore(storeID);
        return store.addConditionalItemsDiscount(percent, endOfSale, itemsIDs);
    }
    public int addConditionalCategoryDiscount(int storeID, double percent, Calendar endOfSale, String category) throws Exception
    {
        Store store = getStore(storeID);
        return store.addConditionalCategoryDiscount(percent, endOfSale, category);
    }
    public int addConditionalStoreDiscount(int storeID, double percent, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        return store.addConditionalStoreDiscount(percent, endOfSale);
    }
    public int addHiddenItemsDiscount(int storeID, List<Integer> itemsIDs, double percent, String coupon, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        return store.addHiddenItemsDiscount(itemsIDs, percent, coupon, endOfSale);
    }
    public int addHiddenCategoryDiscount(int storeID, String category, double percent, String coupon, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        return store.addHiddenCategoryDiscount(category, percent, coupon, endOfSale);
    }
    public int addHiddenStoreDiscount(int storeID, double percent, String coupon, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        return store.addHiddenStoreDiscount(percent, coupon, endOfSale);
    }


    public String addDiscountBasketTotalPriceRule(int storeID, int discountID, double minimumPrice) throws Exception
    {
        Store store = getStore(storeID);
        return store.addDiscountBasketTotalPriceRule(discountID, minimumPrice);
    }
    public String addDiscountQuantityRule(int storeID, int discountID, Map<Integer, Integer> itemsAmounts) throws Exception
    {
        Store store = getStore(storeID);
        return store.addDiscountQuantityRule(discountID, itemsAmounts);
    }
    public String addDiscountComposite(int storeID, int discountID, LogicalComposites logicalComposite, List<Integer> logicalComponentsIDs) throws Exception
    {
        Store store = getStore(storeID);
        return store.addDiscountComposite(discountID, logicalComposite, logicalComponentsIDs);
    }
    public String finishConditionalDiscountBuilding(int storeID, int discountID) throws Exception
    {
        Store store = getStore(storeID);
        return store.finishConditionalDiscountBuilding(discountID);
    }
    public int wrapDiscounts(int storeID, List<Integer> discountsIDsToWrap, NumericComposites numericCompositeEnum) throws Exception
    {
        Store store = getStore(storeID);
        return store.wrapDiscounts(discountsIDsToWrap, numericCompositeEnum);
    }

    public String addPurchasePolicyBasketWeightLimitRule(int storeID, double basketWeightLimit) throws Exception
    {
        Store store = getStore(storeID);
        return store.addPurchasePolicyBasketWeightLimitRule(basketWeightLimit);
    }
    public String addPurchasePolicyBuyerAgeRule(int storeID, int minimumAge) throws Exception
    {
        Store store = getStore(storeID);
        return store.addPurchasePolicyBuyerAgeRule(minimumAge);
    }
    public String addPurchasePolicyForbiddenCategoryRule(int storeID, String forbiddenCategory) throws Exception
    {
        Store store = getStore(storeID);
        return store.addPurchasePolicyForbiddenCategoryRule(forbiddenCategory);
    }
    public String addPurchasePolicyForbiddenDatesRule(int storeID, List<Calendar> forbiddenDates) throws Exception
    {
        Store store = getStore(storeID);
        return store.addPurchasePolicyForbiddenDatesRule(forbiddenDates);
    }
    public String addPurchasePolicyForbiddenHoursRule(int storeID, int startHour, int endHour) throws Exception
    {
        Store store = getStore(storeID);
        return store.addPurchasePolicyForbiddenHoursRule(startHour, endHour);
    }
    public String addPurchasePolicyMustDatesRule(int storeID, List<Calendar> mustDates) throws Exception
    {
        Store store = getStore(storeID);
        return store.addPurchasePolicyMustDatesRule(mustDates);
    }
    public String addPurchasePolicyItemsWeightLimitRule(int storeID, Map<Integer, Double> weightsLimits) throws Exception
    {
        Store store = getStore(storeID);
        return store.addPurchasePolicyItemsWeightLimitRule(weightsLimits);
    }
    public String addPurchasePolicyBasketTotalPriceRule(int storeID, double minimumPrice) throws Exception
    {
        Store store = getStore(storeID);
        return store.addPurchasePolicyBasketTotalPriceRule(minimumPrice);
    }
    public String addPurchasePolicyMustItemsAmountsRule(int storeID, Map<Integer, Integer> itemsAmounts) throws Exception
    {
        Store store = getStore(storeID);
        return store.addPurchasePolicyMustItemsAmountsRule(itemsAmounts);
    }
    public int wrapPurchasePolicies(int storeID, List<Integer> purchasePoliciesIDsToWrap, LogicalComposites logicalCompositeEnum) throws Exception
    {
        Store store = getStore(storeID);
        return store.wrapPurchasePolicies(purchasePoliciesIDsToWrap, logicalCompositeEnum);
    }

    public String addDiscountPolicyBasketWeightLimitRule(int storeID, double basketWeightLimit) throws Exception
    {
        Store store = getStore(storeID);
        return store.addDiscountPolicyBasketWeightLimitRule(basketWeightLimit);
    }
    public String addDiscountPolicyBuyerAgeRule(int storeID, int minimumAge) throws Exception
    {
        Store store = getStore(storeID);
        return store.addDiscountPolicyBuyerAgeRule(minimumAge);
    }
    public String addDiscountPolicyForbiddenCategoryRule(int storeID, String forbiddenCategory) throws Exception
    {
        Store store = getStore(storeID);
        return store.addDiscountPolicyForbiddenCategoryRule(forbiddenCategory);
    }
    public String addDiscountPolicyForbiddenDatesRule(int storeID, List<Calendar> forbiddenDates) throws Exception
    {
        Store store = getStore(storeID);
        return store.addDiscountPolicyForbiddenDatesRule(forbiddenDates);
    }
    public String addDiscountPolicyForbiddenHoursRule(int storeID, int startHour, int endHour) throws Exception
    {
        Store store = getStore(storeID);
        return store.addDiscountPolicyForbiddenHoursRule(startHour, endHour);
    }
    public String addDiscountPolicyMustDatesRule(int storeID, List<Calendar> mustDates) throws Exception
    {
        Store store = getStore(storeID);
        return store.addDiscountPolicyMustDatesRule(mustDates);
    }
    public String addDiscountPolicyItemsWeightLimitRule(int storeID, Map<Integer, Double> weightsLimits) throws Exception
    {
        Store store = getStore(storeID);
        return store.addDiscountPolicyItemsWeightLimitRule(weightsLimits);
    }
    public String addDiscountPolicyBasketTotalPriceRule(int storeID, double minimumPrice) throws Exception
    {
        Store store = getStore(storeID);
        return store.addDiscountPolicyBasketTotalPriceRule(minimumPrice);
    }
    public String addDiscountPolicyMustItemsAmountsRule(int storeID, Map<Integer, Integer> itemsAmounts) throws Exception
    {
        Store store = getStore(storeID);
        return store.addDiscountPolicyMustItemsAmountsRule(itemsAmounts);
    }
    public int wrapDiscountPolicies(int storeID, List<Integer> discountPoliciesIDsToWrap, LogicalComposites logicalCompositeEnum) throws Exception
    {
        Store store = getStore(storeID);
        return store.wrapDiscountPolicies(discountPoliciesIDsToWrap, logicalCompositeEnum);
    }


    public Map<CatalogItem, Boolean> getCatalog() {
        Map<CatalogItem, Boolean> res = new HashMap<>();
        for (Store store : stores.values()) {
            res.putAll(store.getCatalog());
        }
        return res;
    }

    public Map<CatalogItem, Boolean> getCatalog(String keywords, SearchBy searchBy, Map<SearchFilter, FilterValue> filters) throws Exception {
        Map<CatalogItem, Boolean> res = new HashMap<>();
        Collection<Store> storesToSearch = stores.values();
        if (filters.containsKey(STORE_RATING)) {
            storesToSearch.removeIf(store -> filters.get(STORE_RATING).filter());
            filters.remove(STORE_RATING);
        }
        for (Store store : storesToSearch) {
            res.putAll(store.getCatalog(keywords, searchBy, filters));
        }
        return res;
    }

    public CatalogItem removeItemFromStore(int storeID, int itemID) throws Exception
    {
        Store store = getStore(storeID);
        return store.removeItemFromStore(itemID);
    }

    public String updateItemName(int storeID, int itemID, String newName) throws Exception
    {
        Store store = getStore(storeID);
        return store.updateItemName(itemID, newName);
    }

    public Boolean checkIfStoreOwner(int userID, int storeID) throws Exception {
        return getStore(storeID).checkIfStoreOwner(userID);
    }

    public Boolean checkIfStoreManager(int userID, int storeID) throws Exception {
        return getStore(storeID).checkIfStoreManager(userID);
    }

    public void sendMessage(int storeID, int receiverID, String content) throws Exception
    {
        Store store = getStore(storeID);
        store.sendMessage(receiverID, content);
    }

    public ConcurrentHashMap<Integer, Chat> getChats(int storeID) throws Exception {
        if(!isStoreExists(storeID)){
            throw new Exception("The store does not exist!");
        }
        return stores.get(storeID).getChats();
    }

    public void setMailboxAsUnavailable(int storeID) throws Exception
    {
        Store store = getStore(storeID);
        store.setMailboxAsUnavailable();
    }

    public void setMailboxAsAvailable(int storeID) throws Exception
    {
        Store store = getStore(storeID);
        store.setMailboxAsAvailable();
    }

    public Map<Integer, Store> getAllStores() {
        return stores;
    }

    public Map<Integer, Discount> getStoreDiscounts(int storeID) throws Exception
    {
        Store store = getStore(storeID);
        return store.getStoreDiscounts();
    }

    public Map<Integer, Visible> getStoreVisibleDiscounts(int storeID) throws Exception
    {
        Store store = getStore(storeID);
        return store.getStoreVisibleDiscounts();
    }
    public Map<Integer, PurchasePolicy> getStorePurchasePolicies(int storeID) throws Exception
    {
        Store store = getStore(storeID);
        return store.getStorePurchasePolicies();
    }
    public Map<Integer, DiscountPolicy> getStoreDiscountPolicies(int storeID) throws Exception
    {
        Store store = getStore(storeID);
        return store.getStoreDiscountPolicies();
    }

    public boolean isStoreExists(int storeID) {
        return stores.containsKey(storeID);
    }
}