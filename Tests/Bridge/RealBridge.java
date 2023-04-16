package Bridge;


import BusinessLayer.stores.Category;
import Objects.*;
import ServiceLayer.Objects.*;

import java.util.HashMap;
import java.util.List;

public class RealBridge implements Bridge{

    //public Facade facade;

    public RealBridge(){
        //facade = new Facade;
    }


    @Override
    public int registerUser(String userName, String password) {
        //facade.registerUser(userName, password);
        return 1;
    }


    @Override
    public boolean loginUser(int id, String password) {
        //facade.loginUser(id, password);
        return false;
    }

    @Override
    public void exitSystem(int id) {
        //facade.exitSystem(id);
    }

    public void loadSystem(){
        //facade.loadSystem();

    }

    @Override
    public StoreService getStoreInfo(int storeId) {
        //StoreService result = facade.getStoreInfo(storeId);
        //return result;
        return null;
    }

    @Override
    public List<CatalogItemService> searchItems(String itemName, List<String> filters) {
        //List<CatalogItemService> result = this.facade.searchItems(itemName, filters);
        //return result;
        return null;
    }


    @Override
    public boolean addItemToBasket(int userId, int storeId, int itemId, int amount) {
        //return facade.addItemToBasket(userId, storeId, itemId, amount);
        return true;
    }

    @Override
    public CartService getCart(int userId) {
        //return facade.showCart(userId);
        return null;
    }

    @Override
    public boolean buyCart(int userId, String paymentDetails) {
        //return facade.buyCart(userId, paymentDetails);
        return true;
    }

    @Override
    public int addCatalogItem(int storeId, String itemName, int price, Category category) {
        //return facade.addCatalogItem(storeId, itemName, price, category);
        return -1;
    }

    @Override
    public void addItemAmount(int storeId, int itemId, int amount) {
        //this.facade.addItemAmount(storeId, itemId, amount);
    }

    @Override
    public boolean removeItemFromStore(int storeId, int itemId) {
        //return this.facade.removeItemFromStore(storeId, itemId);
        return false;
    }

    @Override
    public boolean changeItemName(int storeId, int itemId, String newName) {
        //return this.facade.updateItemName(storeId, itemId, newName);
        return false;
    }

    @Override
    public List<UserStaffInfoService> showStaffInfo(int storeId, int userId) {
        //return this.facade.showStaffInfo(storeId, userId);
        return null;
    }

    @Override
    public HashMap<Integer, List<ReceiptService>> getSellingHistoryOfStoreForManager(int storeId, int userId) {
        //return this.facade.getSellingHistory(storeId, userId);
        return null;
    }

    @Override
    public StoreService getStoreInfoAsStoreManager(int storeId, int userId) {
        //return this.facade.getStoreInfoAsStoreManager(storeId, userId);
        return null;
    }

    @Override
    public boolean logOut(int userId) {
        //return this.facade.logOut(userId);
        return true;
    }

    @Override
    public int createStore(int userId) {
        //return this.facade.createStore(userId);
        return -1;
    }


    @Override
    public boolean closeStore(int userId, int storeId) {
        //return this.facade.closeStore(userId, storeId);
        return true;
    }

    @Override
    public boolean defineStoreManager(int storeId, int storeOwner, int newStoreManager){
        //return this.facade.defineStoreManager(storeId, storeOwner, newStoreManager);
        return true;
    }

    @Override
    public boolean removeStoreManager(int storeId, int storeOwnerId, int removeUserId) {
        //return this.facade.removeStoreManager(storeId, storeOwnerId, removeUserId);
        return false;
    }

    @Override
    public boolean defineStoreOwner(int storeId, int ownerId, int newCoOwnerId) {
        //return this.facade.defineStoreOwner(storeId, ownerId, newCoOwnerId);
        return false;
    }

    @Override
    public boolean removeStoreOwner(int storeId, int storeOwnerId, int newStoreOwnerId) {
        //return this.facade.removeStoreOwner(storeId, storeOwnerId, newStoreOwnerId);
        return false;
    }

    @Override
    public boolean payCart(int userId, String paymentDetails, String paymentService) {
        //return this.facade.payCart(userId, paymentDetails, paymentService);
        return false;
    }

    @Override
    public boolean askForSupply(int userId, List<CatalogItemService> items, String supplyService) {
        //return this.facade.askForSupply(userId, items, supplyService);
        return false;
    }

    @Override
    public boolean closeStorePermanently(int storeManagerId, int storeId) {
        //return this.facade.closeStorePermanently(storeManagerId, storeId);
        return false;
    }

    @Override
    public boolean checkIfStoreOwner(int userId, int storeId) {
        //return this.facade.checkIfStoreOwner(userId, storeId);
        return false;
    }

    @Override
    public boolean checkIfStoreManager(int userId, int storeId) {
        //return this.facade.checkIfStoreManager(userId, storeId);
        return false;
    }

    @Override
    public boolean removeRegisterdUser(int systemManagerId, int userToRemoveId) {
        //return this.facade.removeRegisterdUser(systemManagerId, userToRemoveId);
        return false;
    }

    @Override
    public HashMap<Integer, String> getComplaints(int managerId) {
        //return this.facade.getComplaints(managerId);
        return null;
    }

    @Override
    public boolean answerComplaint(int userId, HashMap<Integer, String> complaintsAnswers) {
        //return this.facade.answerComplaint(userId, complaintsAnswers);
        return false;
    }

    @Override
    public void postComplaint(int userId, String msg) {
        //this.facade.sendComplaint(userId, msg);
    }

    @Override
    public boolean sendMsg(int senderId, int receiverId, String msg) {
        //return this.facade.sendMsg(senderId, receiverId, msg);
        return false;
    }

    @Override
    public HashMap<Integer, List<String>> getMsgs(int userId) {
        //return this.facade.getMsgs(userId);
        return null;
    }

    @Override
    public HashMap<Integer, List<ReceiptService>> getSellingHistoryOfUserForManager(int managerId, int userId) {
        //return this.facade.getSellingHistoryOfUserForManager(managerId, userId);
        return null;
    }

    @Override
    public HashMap<Integer, String> getUsersTraffic(int managerId) {
        //return this.facade.getUsersTraffic(managerId);
        return null;
    }

    @Override
    public HashMap<Integer, Integer> getPurchaseTraffic(int managerId) {
        //return this.facade.getPurchaseTraffic(managerId);
        return null;
    }

    @Override
    public int getNumberOfRegistrationForToady(int managerId) {
        //return this.facade.getNumberOfRegistrationForToady(managerId);
        return 0;
    }

    @Override
    public boolean reopenStore(int userId, int storeId) {
        //return this.facade.reopenStore(userId, storeId);
        return false;
    }

    @Override
    public List<String> getNotifications(int userId) {
        //return this.facade.getNotifications(userId)
        return null;
    }

    @Override
    public boolean makeAComplaint(int userId, String complaint) {
        //return this.facade.makeAComplaint(userId, complaint)
        return false;
    }

    @Override
    public boolean rankAStore(int userId, int storeId, int rank) {
        //return this.facade.rankAStore(userId, storeId, rank);
        return false;
    }

    @Override
    public double getStoreRank(int userId, int storeId) {
        //return this.facade.getStoreRank(userId, storeId);
        return 0;
    }

    @Override
    public double getItemRank(int userId, int storeId, int itemId) {
        //return this.facade.getItemRank(userId, storeId, itemId);
        return 0;
    }

    @Override
    public boolean rankAnItemInStore(int userId, int storeId, int itemId, int rank) {
        //return this.facade.rankAnItemInStore(userId, storeId, itemId, rank);
        return false;
    }

    @Override
    public HashMap<Integer, List<ReceiptService>> getPersonalHistory(int userId) {
        //return this.facade.getPersonalHistory(userId);
        return null;
    }

    @Override
    public List<String> getPersonalInformation(int userId) {
        //return this.facade.getPersonalInformation(userId);
        return null;
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        //return this.facade.changePassword(userId, oldPassword, newPassword);
        return false;
    }

    @Override
    public List<String> getRequestsOfStore(int ownerManagerId, int storeId) {
        //return this.facade.getRequestsOfStore(ownerManagerId, storeId);
        return null;
    }

    @Override
    public boolean checkIfVisitor(int userId) {
        //return this.facade.checkIfVisitor(userId);
        return false;
    }

    @Override
    public boolean checkIfLoggedIn(int userId) {
        //return this.facade.checkIfLoggedIn(userId);
        return false;
    }


}
