package ServiceLayer;

import Globals.FilterValue;
import Globals.SearchBy;
import Globals.SearchFilter;

import BusinessLayer.Log;
import BusinessLayer.Cart;
import BusinessLayer.Market;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.CatalogItem;

import ServiceLayer.Objects.CartService;
import ServiceLayer.Objects.CatalogItemService;
import ServiceLayer.Objects.StoreService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/*
getStoreInfo(storeId)                                       int storeId
getStoreInfoAsStoreManager(storeId, userId)                 int storeId, int userId
searchItems(itemName, filters)                              String itemName, List<String> filters

addItemToBasket(userId, storeId, itemId, amount)            int userId, int storeId, int itemId, int amount
showCart(userId)                                            int userId
buyCart(userId, paymentDetails)                             int userId, String paymentDetails
payCart(userId, paymentDetails, paymentService)             int userId, String paymentDetails, String paymentService

addItemToStore(storeId, itemName, price)                    int storeId, String itemName, int price
removeItemFromStore(storeId, itemId)                        int storeId, int itemId
updateItemName(storeId, itemId, newName)                    int storeId, int itemId, String newName

getSellingHistory(storeId, userId)                          int storeId, int userId

createStore(userId)                                         int userId
reopenStore(userId, storeId)                                int userId, int storeId
closeStore(userId, storeId)                                 int userId, int storeId
closeStorePermanently(storeManagerId, storeId)              int storeManagerId, int storeId

askForSupply(userId, items, supplyService)                  int userId, List<TestItemInfo> items, String supplyService

checkIfStoreOwner(userId, storeId)                          int userId, int storeId
checkIfStoreManager(userId, storeId)                        int userId, int storeId

rankAStore(userId, storeId, rank)                           int userId, int storeId, int rank
getStoreRank(userId, storeId)                               int userId, int storeId
rankAnItemInStore(userId, storeId, itemId, rank)            int userId, int storeId, int itemId, int rank
getItemRank(userId, storeId, itemId)                        int userId, int storeId, int itemId

getRequestsOfStore(ownerManagerId, storeId)                 int ownerManagerId, int storeId
 */

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

    public HashMap<CatalogItem, Integer> getItemsInBasket(int userID, String storeName) throws Exception {
        return market.getItemsInBasket(userID, storeName);
    }

    public void buyCart(int userID) throws Exception {
        market.buyCart(userID);
    }

    /**
     * empties the cart
     */
    public void emptyCart(int userID) {
        market.emptyCart(userID);
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
}