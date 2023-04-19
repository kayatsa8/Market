package BusinessLayer.Users;

import BusinessLayer.Market;
import BusinessLayer.StorePermissions.StoreOwner;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.StoreFacade;

import java.util.Map;

public class SystemManager {
    private final UserFacade userFacade;
    private final StoreFacade storeFacade;
    private RegisteredUser myUser;
    private Market market;

    public SystemManager(RegisteredUser user) throws Exception {
        this.myUser = user;
        this.market = Market.getInstance();
        this.userFacade = market.getUserFacade();
        this.storeFacade = market.getStoreFacade();
        this.market.addAdmin(myUser.getUsername(), this);
    }

    public void closeStorePermanently(Store store) throws Exception
    {
        //remove all owners -> will automatically remove all managers
        int founderID = store.getFounderID();
        RegisteredUser founder = userFacade.getUserByID(founderID);
        myUser.closeStore(founder, store.getStoreID());
        storeFacade.closeStorePermanently(store.getStoreID());
    }

    public void removeUser(RegisteredUser userToRemove) throws Exception {
        removeStoreAssociations(userToRemove);
        userFacade.removeUser(userToRemove);
    }

    private void removeStoreAssociations(RegisteredUser userToRemove) throws Exception
    {
        int storeId;
        int founderId;
        int parentUserId;
        Store store;
        RegisteredUser founder;
        RegisteredUser parentUser;
        for (StoreOwner ownership : userToRemove.getStoresIOwn().values()) {
            //use store to find founder
            storeId = ownership.getStoreID();
            store = storeFacade.getStore(storeId);
            founderId = store.getFounderID();
            founder = userFacade.getUserByID(founderId);
            parentUserId = founder.getStoreIOwn(storeId).findChild(userToRemove);
            parentUser = userFacade.getUserByID(parentUserId);
            if (userToRemove.getId()==founderId) {
                parentUser.getStoreIOwn(storeId).closeStore();
                storeFacade.closeStorePermanently(storeId);
            }
            else {
                parentUser.removeOwner(userToRemove, storeId);
            }
        }
        for (Integer storeID : userToRemove.getStoresIManage().keySet()) {
            myUser.removeManager(userToRemove, storeID);
        }
        //TODO also remove from notification system
    }
}
