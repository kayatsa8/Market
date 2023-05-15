package ServiceLayer.Objects;

import BusinessLayer.Users.RegisteredUser;

import java.util.ArrayList;
import java.util.Set;

public class UserInfoService {

    private String username;
    private int id;
    private ArrayList<Integer> storesIOwn;
    private ArrayList<Integer> storesIManage;

    public UserInfoService(RegisteredUser user) {
        this.username = user.getUsername();
        this.id = user.getId();
        this.storesIOwn = new ArrayList<>(user.getStoresIOwn().keySet());
        this.storesIManage = new ArrayList<>(user.getStoresIManage().keySet());
    }


    //Sets the Store for Owner Screen. Only the Stores he appointed me are here
    //if action is owner than Only storeI Own will be defined.
    public UserInfoService(RegisteredUser user, Set<Integer> value, String action) {
        this.username = user.getUsername();
        this.id = user.getId();
        if(action.equals("owner")){
            this.storesIOwn = new ArrayList<>(value);
            this.storesIManage = new ArrayList<>();
        }
        else{
            this.storesIManage = new ArrayList<>(value);
            this.storesIOwn = new ArrayList<>();
        }

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Integer> getStoresIOwn() {
        return storesIOwn;
    }

    public ArrayList<Integer> getStoresIManage() {
        return storesIManage;
    }

    public void addStoresIOwn(int storeId) {
        this.storesIOwn.add(storeId);
    }

    public void addStoresIManage(int storeId) {
        this.storesIManage.add(storeId);
    }

    public void removeStoresIOwn(Integer storeId) {
        this.storesIOwn.remove(storeId);
    }

    public void removeStoresIManage(Integer storeId) {
        this.storesIManage.remove(storeId);
    }

    public boolean ownStore(int storeId) {
        return this.storesIOwn.contains(storeId);
    }

    public boolean manageStore(int storeId) {
        return this.storesIManage.contains(storeId);
    }

    public String getStoreIOwnString(){
        StringBuilder res = new StringBuilder();
        for(Integer id : storesIOwn){
            res.append(id).append(" ,");
        }
        if(res.length() > 0){
            res.deleteCharAt(res.length() - 1);   //deletes ,
        }
        return res.toString();
    }

    public String getStoreIManageString(){
        StringBuilder res = new StringBuilder();
        for(Integer id : storesIManage){
            res.append(id).append(" ,");
        }
        if(res.length() > 0){
            res.deleteCharAt(res.length() - 1);   //deletes ,
        }
        return res.toString();
    }
}