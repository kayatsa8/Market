package Bridge;


import Objects.*;

import java.util.ArrayList;
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
    public String getStoreInfo(int storeId) {
        //String result = facade.getStoreInfo(storeId);
        //return result;
        return "Temp";
    }

    @Override
    public List<TestItemInfo> searchItems(String itemName, List<String> filters) {
        //List<TestItemInfo> result = acade.searchItems(itemName, filters);
        //return result;
        return new ArrayList<TestItemInfo>();
    }


    @Override
    public boolean addItemToBasket(int userId, int storeId, int itemId, int amount) {
        //return facade.addItemToBasket(userId, storeId, itemId, amount);
        return true;
    }

    @Override
    public TestCartInfo showCart(int userId) {
        //return facade.showCart(userId);
        return null;
    }

    @Override
    public boolean buyCart(int userId, String paymentDetails) {
        //return facade.buyCart(userId, paymentDetails);
        return true;
    }

    @Override
    public int addItemToStore(int storeId, String itemName, int price) {
        //return facade.addItemToStore(storeId, itemName, price);
        return -1;
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
    public List<TestStaffInfo> showStaffInfo(int storeId, int userId) {
        //return this.facade.showStaffInfo(storeId, userId);
        return null;
    }

    @Override
    public HashMap<Integer, List<TestReceipt>> getSellingHistoryOfStoreForManager(int storeId, int userId) {
        //return this.facade.getSellingHistory(storeId, userId);
        return null;
    }

    @Override
    public TestStoreInfo getStoreInfoAsStoreManager(int storeId, int userId) {
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
    public boolean askForSupply(int userId, List<TestItemInfo> items, String supplyService) {
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
    public HashMap<Integer, List<TestReceipt>> getSellingHistoryOfUserForManager(int managerId, int userId) {
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


}
