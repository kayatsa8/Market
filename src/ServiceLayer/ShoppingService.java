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
<<<<<<< HEAD
getSellingHistory               int storeId, int userId

askForSupply                    int userId, List<TestItemInfo> items, String supplyService

rankAStore                      int userId, int storeId, int rank
getStoreRank                    int userId, int storeId
rankAnItemInStore               int userId, int storeId, int itemId, int rank
getItemRank                     int userId, int storeId, int itemId
=======
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
>>>>>>> 9b37986 (resolving rebase conflicts)
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
}