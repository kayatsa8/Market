package Bridge;

import BusinessLayer.Stores.Category;
import ServiceLayer.Objects.*;

import java.util.HashMap;
import java.util.List;

public class ProxyBridge implements Bridge{

    private Bridge real = null;

    public void setRealBridge(Bridge implementation) {
        if (real == null)
            real = implementation;
    }


    @Override
    public int registerUser(String userName, String password) {
        if(real != null){
            return real.registerUser(userName, password);
        }
        return 1;
    }

    @Override
    public boolean loginUser(int id, String password) {
        if(real != null){
            return real.loginUser(id, password);
        }
        return true;
    }

    @Override
    public void exitSystem(int id) {
        if(real != null){
            real.exitSystem(id);
        }
    }

    @Override
    public void loadSystem(){
        if(real != null){
            real.loadSystem();
        }
    }


    @Override
    public StoreService getStoreInfo(int storeId) {
        if(real != null){
            return real.getStoreInfo(storeId);
        }
        return null;
    }


    @Override
    public List<CatalogItemService> searchItems(String itemName, List<String> filters) {
        if(real != null){
            return real.searchItems(itemName, filters);
        }
        return null;
    }


    @Override
    public boolean addItemToBasket(int userId, int storeId, int itemId, int amount) {
        if(real != null){
            return real.addItemToBasket(userId, storeId, itemId, amount);
        }
        return true;
    }

    @Override
    public CartService getCart(int userId) {
        if(real != null){
            return real.getCart(userId);
        }
        return null;
    }

    @Override
    public boolean buyCart(int userId, String paymentDetails) {
        if(real != null){
            return real.buyCart(userId, paymentDetails);
        }
        return true;
    }

    @Override
    public int addCatalogItem(int storeId, String itemName, int price, Category category) {
        if(real != null){
            return real.addCatalogItem(storeId, itemName, price, category);
        }
        return -1;
    }

    @Override
    public void addItemAmount(int storeId, int itemId, int amount) {
        if(real != null){
            real.addItemAmount(storeId, itemId, amount);
        }
    }

    @Override
    public boolean removeItemFromStore(int storeId, int itemId) {
        if(real != null){
            return real.removeItemFromStore(storeId, itemId);
        }
        return false;
    }

    @Override
    public boolean changeItemName(int storeId, int itemId, String newName) {
        if(real != null){
            return real.changeItemName(storeId, itemId, newName);
        }
        return false;
    }

    @Override
    public List<UserStaffInfoService> showStaffInfo(int storeId, int userId) {
        if(real != null){
            return real.showStaffInfo(storeId, userId);
        }
        return null;
    }

    @Override
    public HashMap<Integer, List<ReceiptService>> getSellingHistoryOfStoreForManager(int storeId, int userId) {
        if(real != null){
            return real.getSellingHistoryOfStoreForManager(storeId, userId);
        }
        return null;
    }

    @Override
    public StoreService getStoreInfoAsStoreManager(int storeId, int userId) {
        if(real != null){
            return real.getStoreInfoAsStoreManager(storeId, userId);
        }
        return null;
    }

    @Override
    public boolean logOut(int userId) {
        if(real != null){
            return real.logOut(userId);
        }
        return true;
    }

    @Override
    public int createStore(int userId) {
        if(real != null){
            return real.createStore(userId);
        }
        return -1;
    }


    @Override
    public boolean closeStore(int userId, int storeId) {
        if(real != null){
            return real.closeStore(userId, storeId);
        }
        return true;
    }

    @Override
    public boolean defineStoreManager(int storeId, int storeOwner, int newStoreManager){
        if(real != null){
            return real.defineStoreManager(storeId, storeOwner, newStoreManager);
        }
        return true;
    }

    @Override
    public boolean removeStoreManager(int storeId, int storeOwnerId, int removeUserId) {
        if(real != null){
            return real.removeStoreManager(storeId, storeOwnerId, removeUserId);
        }
        return true;
    }

    @Override
    public boolean defineStoreOwner(int storeId, int ownerId, int newCoOwnerId) {
        if(real != null){
            return real.defineStoreOwner(storeId, ownerId, newCoOwnerId);
        }
        return false;
    }

    @Override
    public boolean removeStoreOwner(int storeId, int storeOwnerId, int newStoreOwnerId) {
        if(real != null){
            return real.removeStoreOwner(storeId, storeOwnerId, newStoreOwnerId);
        }
        return false;
    }

    @Override
    public boolean payCart(int userId, String paymentDetails, String paymentService) {
        if(real != null){
            return real.payCart(userId, paymentDetails, paymentService);
        }
        return false;
    }

    @Override
    public boolean askForSupply(int userId, List<CatalogItemService> items, String supplyService) {
        if(real != null){
            return real.askForSupply(userId, items, supplyService);
        }
        return true;
    }

    @Override
    public boolean closeStorePermanently(int storeManagerId, int storeId) {
        if(real != null){
            return real.closeStorePermanently(storeManagerId, storeId);
        }
        return false;
    }

    @Override
    public boolean checkIfStoreOwner(int userId, int storeId) {
        if(real != null){
            return real.checkIfStoreOwner(userId, storeId);
        }
        return false;
    }

    @Override
    public boolean checkIfStoreManager(int userId, int storeId) {
        if(real != null){
            return real.checkIfStoreManager(userId, storeId);
        }
        return false;
    }

    @Override
    public boolean removeRegisterdUser(int systemManagerId, int userToRemoveId) {
        if(real != null){
            return real.removeRegisterdUser(systemManagerId, userToRemoveId);
        }
        return false;
    }

    @Override
    public HashMap<Integer, String> getComplaints(int managerId) {
        if(real != null){
            return real.getComplaints(managerId);
        }
        return null;
    }

    @Override
    public boolean answerComplaint(int userId, HashMap<Integer, String> complaintsAnswers) {
        if(real != null){
            return real.answerComplaint(userId, complaintsAnswers);
        }
        return false;
    }

    @Override
    public void postComplaint(int userId, String msg) {
        if(real != null){
            real.postComplaint(userId, msg);
        }
    }

    @Override
    public boolean sendMsg(int senderId, int receiverId, String msg) {
        if(real != null){
            return real.sendMsg(senderId, receiverId, msg);
        }
        return false;
    }

    @Override
    public HashMap<Integer, List<String>> getMsgs(int userId) {
        if(real != null){
            return real.getMsgs(userId);
        }
        return null;
    }

    @Override
    public HashMap<Integer, List<ReceiptService>> getSellingHistoryOfUserForManager(int managerId, int userId) {
        if(real != null){
            return real.getSellingHistoryOfUserForManager(managerId, userId);
        }
        return null;
    }

    @Override
    public HashMap<Integer, String> getUsersTraffic(int managerId) {
        if(real != null){
            return real.getUsersTraffic(managerId);
        }
        return null;
    }

    @Override
    public HashMap<Integer, Integer> getPurchaseTraffic(int managerId) {
        if(real != null){
            return real.getPurchaseTraffic(managerId);
        }
        return null;
    }

    @Override
    public int getNumberOfRegistrationForToady(int managerId) {
        if(real != null){
            return real.getNumberOfRegistrationForToady(managerId);
        }
        return 0;
    }

    @Override
    public boolean reopenStore(int userId, int storeId) {
        if(real != null){
            return real.reopenStore(userId, storeId);
        }
        return false;
    }

    @Override
    public List<String> getNotifications(int userId) {
        if(real != null){
            return real.getNotifications(userId);
        }
        return null;
    }

    @Override
    public boolean makeAComplaint(int userId, String complaint) {
        if(real != null){
            return real.makeAComplaint(userId, complaint);
        }
        return false;
    }

    @Override
    public boolean rankAStore(int userId, int storeId, int rank) {
        if(real != null){
            return real.rankAStore(userId, storeId, rank);
        }
        return false;
    }

    @Override
    public double getStoreRank(int userId, int storeId) {
        if(real != null){
            return real.getStoreRank(userId, storeId);
        }
        return 0;
    }

    @Override
    public double getItemRank(int userId, int storeId, int itemId) {
        if(real != null){
            return real.getItemRank(userId, storeId, itemId);
        }
        return 0;
    }

    @Override
    public boolean rankAnItemInStore(int userId, int storeId, int itemId, int rank) {
        if(real != null){
            return real.rankAnItemInStore(userId, storeId, itemId, rank);
        }
        return false;
    }

    @Override
    public HashMap<Integer, List<ReceiptService>> getPersonalHistory(int userId) {
        if(real != null){
            return real.getPersonalHistory(userId);
        }
        return null;
    }

    @Override
    public List<String> getPersonalInformation(int userId) {
        if(real != null){
            return real.getPersonalInformation(userId);
        }
        return null;
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        if(real != null){
            return real.changePassword(userId, oldPassword, newPassword);
        }
        return false;
    }

    @Override
    public List<String> getRequestsOfStore(int ownerManagerId, int storeId) {
        if(real != null){
            return real.getRequestsOfStore(ownerManagerId, storeId);
        }
        return null;
    }

    @Override
    public boolean checkIfVisitor(int userId) {
        if(real != null){
            return real.checkIfVisitor(userId);
        }
        return false;
    }

    @Override
    public boolean checkIfLoggedIn(int userId) {
        if(real != null){
            return real.checkIfLoggedIn(userId);
        }
        return false;
    }

}
