package Tests.Bridge;

import Tests.Objects.*;

import java.util.ArrayList;
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
    public String getStoreInfo(int storeId) {
        if(real != null){
            return real.getStoreInfo(storeId);
        }
        return "Temp";
    }


    @Override
    public List<TestItemInfo> searchItems(String itemName, List<String> filters) {
        if(real != null){
            return real.searchItems(itemName, filters);
        }
        return new ArrayList<TestItemInfo>();
    }


    @Override
    public Boolean addItemToBasket(int userId, int storeId, int itemId, int amount) {
        if(real != null){
            return real.addItemToBasket(userId, storeId, itemId, amount);
        }
        return true;
    }

    @Override
    public TestCartInfo showCart(int userId) {
        if(real != null){
            return real.showCart(userId);
        }
        return new TestCartInfo();
    }

    @Override
    public Boolean buyCart(int userId, String paymentDetails) {
        if(real != null){
            return real.buyCart(userId, paymentDetails);
        }
        return true;
    }

    @Override
    public int addItemToStore(int storeId, String itemName, int price) {
        if(real != null){
            return real.addItemToStore(storeId, itemName, price);
        }
        return -1;
    }

    @Override
    public Boolean removeItemFromStore(int storeId, int itemId) {
        if(real != null){
            return real.removeItemFromStore(storeId, itemId);
        }
        return false;
    }

    @Override
    public Boolean changeItemName(int storeId, int itemId, String newName) {
        if(real != null){
            return real.changeItemName(storeId, itemId, newName);
        }
        return false;
    }

    @Override
    public List<TestStaffInfo> showStaffInfo(int storeId, int userId) {
        if(real != null){
            return real.showStaffInfo(storeId, userId);
        }
        return null;
    }

    @Override
    public List<TestReceipt> getSellingHistory(int storeId, int userId) {
        if(real != null){
            return real.getSellingHistory(storeId, userId);
        }
        return null;
    }

    @Override
    public TestStoreInfo getStoreInfoAsStoreManager(int storeId, int userId) {
        if(real != null){
            return real.getStoreInfoAsStoreManager(storeId, userId);
        }
        return null;
    }


}
