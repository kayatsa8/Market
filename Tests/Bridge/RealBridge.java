package Bridge;


import Globals.FilterValue;
import Globals.SearchBy;
import Globals.SearchFilter;
import ServiceLayer.Objects.*;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;
import ServiceLayer.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealBridge implements Bridge{

    public UserService userService;
    public ShoppingService shoppingService;

    public RealBridge(){
        try {
            userService = new UserService();
            shoppingService = new ShoppingService();
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }


    @Override
    public int registerUser(String userName, String password) {
        Result<Integer> result = userService.register(userName, password);
        return handleIntResult(result);
    }


    @Override
    public boolean loginUser(String name, String password) {
        Result<Integer> result = userService.login(name, password);
        int res = handleIntResult(result);
        /** */
        return res != -1;
    }


    @Override
    public StoreService getStoreInfo(int storeId) {
        Result<StoreService> result = shoppingService.getStoreInfo(storeId);
        if(result.isError()){
            System.out.println(result.getMessage());
            return null;
        }
        return result.getValue();
    }

    @Override
    public List<CatalogItemService> searchItems(String keywords, SearchBy searchBy, Map<SearchFilter, FilterValue> filters) {
        Result<List<CatalogItemService>> result =  shoppingService.searchCatalog(keywords, searchBy, filters);
        if(result.isError()){
            System.out.println(result.getMessage());
            return null;
        }
        return result.getValue();
    }


    @Override
    public CartService addItemToBasket(int userId, int storeId, int itemId, int amount) {
        /** can remove try catch when shoppingService will dump theirs*/

        try {
            Result<CartService> result = shoppingService.addItemToCart(userId, storeId, itemId, amount);
            if(result.isError()){
                System.out.println(result.getMessage());
                return null;
            }
            return result.getValue();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CartService getCart(int userId) {
        Result<CartService> result = shoppingService.getCart(userId);
        if(result.isError()){
            System.out.println(result.getMessage());
            return null;
        }
        return result.getValue();

    }

    @Override
    public boolean buyCart(int userId, String deliveryAddress) {
        try {
            shoppingService.buyCart(userId, deliveryAddress);

            //temp fix, I need it to return boolean
            return shoppingService.getCart(userId).getValue().isEmpty();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public CatalogItemService addCatalogItem(int storeId, String itemName, int price, String category) {
        Result<CatalogItemService> result = shoppingService.addItemToStore(storeId, itemName, price, category);
        if(result.isError()){
            System.out.println(result.getMessage());
            return null;
        }
        return result.getValue();
    }



    @Override
    public boolean removeItemFromStore(int storeId, int itemId) {
        Result<String> result =  shoppingService.removeItemFromStore(storeId, itemId);
        if(result.isError()){
            System.out.println(result.getMessage());
            return false;
        }
        //item removed
        return true;
    }

    @Override
    public String changeItemName(int storeId, int itemId, String newName) {
        Result<String> result = shoppingService.updateItemName(storeId, itemId, newName);
        if(result.isError()){
            System.out.println(result.getMessage());
        }
        return result.getMessage();
    }

    @Override
    public List<UserStaffInfoService> showStaffInfo(int storeId, int userId) {
        //return this.facade.showStaffInfo(storeId, userId);
        /** */
        return null;
    }

    @Override
    public List<ReceiptService> getSellingHistoryOfStoreForManager(int storeId, int userId) {
        Result<List<ReceiptService>> result = shoppingService.getSellingHistoryOfStoreForManager(storeId, userId);
        if(result.isError()){
            System.out.println(result.getMessage());
            return null;
        }
        return result.getValue();
    }

    @Override
    public StoreService getStoreInfoAsStoreManager(int storeId, int userId) {
        Result<StoreService> result = shoppingService.getStoreInfoAsStoreManager(storeId, userId);
        if(result.isError()){
            System.out.println(result.getMessage());
            return null;
        }
        return result.getValue();
    }

    @Override
    public boolean logOut(int userID) {
        Result<Boolean> result = this.userService.logout(userID);
        return handleBoolResult(result);
    }


    @Override
    public int createStore(int userId, String storeName) {
        Result<Integer>  result = shoppingService.createStore(userId, storeName);
        return handleIntResult(result);
    }

    @Override
    public boolean closeStore(int userId, int storeId) {
        Result<Boolean> result = shoppingService.closeStore(userId, storeId);
        return handleBoolResult(result);
    }

    @Override
    public boolean defineStoreManager(int storeId, int storeOwner, int newStoreManager){
        Result<Boolean> result = userService.addManager(storeOwner, newStoreManager, storeId);
        return handleBoolResult(result);
    }

    @Override
    public boolean defineStoreOwner(int storeId, int ownerId, int newCoOwnerId) {
        Result<Boolean> result = userService.addOwner(ownerId, newCoOwnerId, storeId);
        return handleBoolResult(result);
    }

    @Override
    public void addItemAmount(int storeId, int itemId, int amount) {
        shoppingService.addItemAmount(storeId, itemId, amount);
    }

    @Override
    public void exitSystem(int id) {
        //facade.exitSystem(id);
        /** */
    }


    @Override
    public HashMap<Integer, List<ReceiptService>> getSellingHistoryOfUserForManager(int managerId, int userId) {
        //return this.facade.getSellingHistoryOfUserForManager(managerId, userId);
        /** */
        return null;
    }


    @Override
    public List<ReceiptService> getPersonalHistory(int userId) {
        //return this.facade.getPersonalHistory(userId);
        /** */
        return null;
    }




    @Override
    public boolean checkIfStoreOwner(int userId, int storeId) {
        Result<Boolean> result = shoppingService.checkIfStoreOwner(userId, storeId);
        return handleBoolResult(result);
    }

    @Override
    public boolean checkIfStoreManager(int userId, int storeId) {
        Result<Boolean> result = shoppingService.checkIfStoreManager(userId, storeId);
        return handleBoolResult(result);
    }

    @Override
    public List<MessageService> watchNotReadMessages(int id) {
        Result<List<MessageService>> result = shoppingService.watchNotReadMessages(id);
        if(result.isError()){
            System.out.println(result.getMessage());
            return null;
        }
        return result.getValue();
    }



    private boolean handleBoolResult(Result<Boolean> result) {
        if(result.isError()){
            System.out.println(result.getMessage());
            return false;
        }
        return result.getValue();
    }

    private int handleIntResult(Result<Integer> result) {
        if(result.isError()){
            System.out.println(result.getMessage());
            return -1;
        }
        return result.getValue();
    }








    @Override
    public boolean sendMsg(int senderId, int receiverId, String msg) {
        /** NotForVersion1 */
        return false;
    }

    @Override
    public HashMap<Integer, String> getComplaints(int managerId) {
        //return this.facade.getComplaints(managerId);
        /** NotForVersion1 */
        return null;
    }


    @Override
    public List<String> getPersonalInformation(int userId) {
        //return this.facade.getPersonalInformation(userId);
        /** NotForVersion1 */
        return null;
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        //return this.facade.changePassword(userId, oldPassword, newPassword);
        /** NotForVersion1 */
        return false;
    }


    @Override
    public HashMap<Integer, List<String>> getMsgs(int userId) {
        //return this.facade.getMsgs(userId);
        /** NotForVersion1 */
        return null;
    }


    @Override
    public boolean removeStoreManager(int storeId, int storeOwnerId, int removeUserId) {
        /** NotForVersion1 */
        Result<Boolean> result = userService.removeManager(storeOwnerId, removeUserId, storeId);
        return handleBoolResult(result);
    }


    @Override
    public boolean removeStoreOwner(int storeId, int storeOwnerId, int newStoreOwnerId) {
        /** NotForVersion1 */
        Result<Boolean> result = userService.removeOwner(storeOwnerId, newStoreOwnerId, storeId);
        return handleBoolResult(result);
    }

    @Override
    public boolean closeStorePermanently(int storeManagerId, int storeId) {
        /** NotForVersion1 */
        Result<Boolean> result = shoppingService.closeStorePermanently(storeManagerId, storeId);
        return handleBoolResult(result);
    }

    @Override
    public boolean reopenStore(int userId, int storeId) {
        /** NotForVersion1 */
        Result<Boolean> result = shoppingService.reopenStore(userId, storeId);
        return handleBoolResult(result);
    }

    @Override
    public boolean removeRegisterdUser(int systemManagerId, int userToRemoveId) {
        /** NotForVersion1 */
        //return this.facade.removeRegisterdUser(systemManagerId, userToRemoveId);
        return false;
    }

    @Override
    public boolean answerComplaint(int userId, HashMap<Integer, String> complaintsAnswers) {
        /** NotForVersion1 */
        //return this.facade.answerComplaint(userId, complaintsAnswers);
        return false;
    }

    @Override
    public void postComplaint(int userId, String msg) {
        /** NotForVersion1 */
        //this.facade.sendComplaint(userId, msg);
    }

    @Override
    public HashMap<Integer, String> getUsersTraffic(int managerId) {
        /** NotForVersion1 */
        //return this.facade.getUsersTraffic(managerId);
        return null;
    }

    @Override
    public HashMap<Integer, Integer> getPurchaseTraffic(int managerId) {
        /** NotForVersion1 */
        //return this.facade.getPurchaseTraffic(managerId);
        return null;
    }

    @Override
    public int getNumberOfRegistrationForToady(int managerId) {
        /** NotForVersion1 */
        //return this.facade.getNumberOfRegistrationForToady(managerId);
        return 0;
    }


    @Override
    public List<String> getRequestsOfStore(int ownerManagerId, int storeId) {
        /** NotForVersion1 */
        //return this.facade.getRequestsOfStore(ownerManagerId, storeId);
        return null;
    }



    @Override
    public boolean checkIfLoggedIn(int userId) {
        //return this.facade.checkIfLoggedIn(userId);
        /** Don't implement*/
        return false;
    }



    @Override
    public boolean rankAStore(int userId, int storeId, int rank) {
        //return this.facade.rankAStore(userId, storeId, rank);
        /** NotForVersion1 */
        return false;
    }

    @Override
    public double getStoreRank(int userId, int storeId) {
        //return this.facade.getStoreRank(userId, storeId);
        /** NotForVersion1 */
        return 0;
    }

    @Override
    public double getItemRank(int userId, int storeId, int itemId) {
        //return this.facade.getItemRank(userId, storeId, itemId);
        /** NotForVersion1 */
        return 0;
    }

    @Override
    public boolean rankAnItemInStore(int userId, int storeId, int itemId, int rank) {
        //return this.facade.rankAnItemInStore(userId, storeId, itemId, rank);
        /** NotForVersion1 */
        return false;
    }


    public void loadSystem(){
        //facade.loadSystem();
        /** NotForVersion1 */

    }


    @Override
    public boolean payCart(int userId, String paymentDetails, String paymentService) {
        //return this.facade.payCart(userId, paymentDetails, paymentService);
        /** Not sure...*/
        return false;
    }

    @Override
    public boolean askForSupply(int userId, List<CatalogItemService> items, String supplyService) {
        //return this.facade.askForSupply(userId, items, supplyService);
        /** Not sure...*/
        return false;
    }




}
