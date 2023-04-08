package Tests.Bridge;


import Tests.Objects.*;

import java.util.ArrayList;
import java.util.List;

public class RealBridge implements Bridge{

    //public Facade facade;

    public RealBridge(){
        //facade = new Facade;
    }


    @Override
    public int registerUser(String userName, String password) {
        //TODO real
        //facade.registerUser(userName, password);
        return 1;
    }


    @Override
    public boolean loginUser(int id, String password) {
        //TODO real
        //facade.loginUser(id, password);
        return false;
    }

    @Override
    public void exitSystem(int id) {
        //TODO real
        //facade.exitSystem(id);
    }

    public void loadSystem(){
        //TODO real
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
    public Boolean addItemToBasket(int userId, int storeId, int itemId, int amount) {
        //return facade.addItemToBasket(userId, storeId, itemId, amount);
        return true;
    }

    @Override
    public TestCartInfo showCart(int userId) {
        //return facade.showCart(userId);
        return null;
    }

    @Override
    public Boolean buyCart(int userId, String paymentDetails) {
        //return facade.buyCart(userId, paymentDetails);
        return true;
    }

    @Override
    public int addItemToStore(int storeId, String itemName, int price) {
        //return facade.addItemToStore(storeId, itemName, price);
        return -1;
    }

    @Override
    public Boolean removeItemFromStore(int storeId, int itemId) {
        //return this.facade.removeItemFromStore(storeId, itemId);
        return false;
    }

    @Override
    public Boolean changeItemName(int storeId, int itemId, String newName) {
        //return this.facade.updateItemName(storeId, itemId, newName);
        return false;
    }

    @Override
    public List<TestStaffInfo> showStaffInfo(int storeId, int userId) {
        //return this.facade.showStaffInfo(storeId, userId);
        return null;
    }

    @Override
    public List<TestReceipt> getSellingHistory(int storeId, int userId) {
        //return this.facade.getSellingHistory(storeId, userId);
        return null;
    }

    @Override
    public TestStoreInfo getStoreInfoAsStoreManager(int storeId, int userId) {
        //return this.facade.getStoreInfoAsStoreManager(storeId, userId);
        return null;
    }


}
