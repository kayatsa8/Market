package PresentationLayer.initialize;

import ServiceLayer.Objects.CatalogItemService;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;
import ServiceLayer.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Loader {
    private List<RegisteredUser>  userList;
    private ShoppingService shoppingService;
    private UserService userService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public Loader() {
        try {
            shoppingService = new ShoppingService();
            userService = new UserService();
        }catch (Exception e) {
            System.out.println("Problem initiating Market"+e.getMessage());
        }
    }
    public void load(String path) {
        try {
            // Read the JSON file as a String
            String jsonString = readFileAsString(path);

            // Create a Gson object
            Gson gson = new Gson();
            TypeToken<List<RegisteredUser>> myDataTypeToken = new TypeToken<List<RegisteredUser>> () {};

            // Parse the JSON string to MyData object
            userList = gson.fromJson(jsonString, myDataTypeToken.getType());
            //TODO build APP using API
            build(userList);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void build(List<RegisteredUser> userList){
        //users
        for (RegisteredUser user: userList) {
            Result<Integer> registerResult= userService.register(user.username,user.password, user.getAddress(),LocalDate.parse(user.getbDay(), formatter) );
            Result<Integer> loginResult= userService.login(user.username,user.password);
            if (!registerResult.isError()&!loginResult.isError()){
                int founderId=registerResult.getValue();
                setAdmin(founderId);
                buildStores(user.getStores(),founderId);

                //logout
                Result<Boolean> logoutResult= userService.logout(loginResult.getValue());
                if (logoutResult.isError()) System.out.println("Problem logout"+logoutResult.getMessage());
            }
            else System.out.println("Problem register or login"+registerResult.getMessage()+"\n"+loginResult.getMessage());
        }
    }

    private void setAdmin(int userId) {
        //TODO add admin in Load
    }

    private void buildStores(List<Store> stores, int founderId) {
        //stores
        for (Store store: stores) {
            Result<Integer> idResult =userService.getUserIdByName(store.founderName);
            if (!idResult.isError()){
                int id= idResult.getValue();
                //crate store
                Result<Integer> storeIdResult = shoppingService.createStore(id,store.getStoreName());//storeID
                if (!storeIdResult.isError()){
                    int storeId=storeIdResult.getValue();
                    //Load items & amount
                    loadItems(store, storeId);
                    loadOwners(founderId,store.ownersList,storeId);
                    loadManagers(founderId,store.managersList,storeId);

                }else System.out.println("Fail to create store"+storeIdResult.getMessage());
            }else System.out.println("Fail to getUserIdByName"+idResult.getMessage());

        }

    }

    private void loadManagers(int founderId, List<String> managersList, int storeId) {
        for (String managerName:managersList) {
            Result<Integer> newManagerIdResult=userService.getUserIdByName(managerName);
            if (!newManagerIdResult.isError()) {
                int newManagerId=newManagerIdResult.getValue();
                Result<Boolean> addManagerResult = userService.addManager(founderId,newManagerId,storeId);
                if (!addManagerResult.isError()) System.out.println("Fail to addOwner"+addManagerResult.getMessage());
            }else System.out.println("Fail to getUserIdByName"+newManagerIdResult.getMessage());
        }
    }

    private void loadOwners(int founderId, List<String> ownersList, int storeID) {
        for (String ownerName:ownersList) {
            Result<Integer> newOwnerIdResult=userService.getUserIdByName(ownerName);
            if (!newOwnerIdResult.isError()) {
                int newOwnerId=newOwnerIdResult.getValue();
                Result<Boolean> addOwnerResult = userService.addOwner(founderId,newOwnerId,storeID);
                if (!addOwnerResult.isError()) System.out.println("Fail to addOwner"+addOwnerResult.getMessage());
            }else System.out.println("Fail to getUserIdByName"+newOwnerIdResult.getMessage());
        }
    }

    private void loadItems(Store store, int storeID) {
        for (Item item: store.getItemList()) {
            Result<CatalogItemService> itemId = shoppingService.addItemToStore(storeID,item.getItemName(),
                    item.getItemPrice(), item.getItemCategory(), item.getWeight());
            if(!itemId.isError()) {
                Result<Boolean> addItemAmountResult=shoppingService.addItemAmount(storeID, itemId.getValue().getItemID(), item.getAmount());
                if (addItemAmountResult.isError()) System.out.println("Fail to addItemAmountResult"+addItemAmountResult.getMessage());
            }
            else System.out.println("Fail to addItemToStore"+itemId.getMessage());
        }
    }

    public void crateJson(){

    }
    public List<Item> initializeItemList(){
        HashMap<String, List<String>> doubleBraceMap  = new HashMap<String, List<String>>() {{
            put("Dairy", Arrays.asList("Milk","Butter"));
            put("Wheat", Arrays.asList("Bread","Baguette"));
        }};
        return  Arrays.asList(
                new Item("Milk",5.0,"Dairy",1.0,3),
                new Item("Butter",7.0,"Dairy",1.0,3)
        );
    }

    private static String readFileAsString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    private class MyData{
        private List<Store> storeList;
        private List<RegisteredUser> registeredUserList;

        public List<Store> getStoreList() {
            return storeList;
        }

        public void setStoreList(List<Store> storeList) {
            this.storeList = storeList;
        }

        public List<RegisteredUser> getRegisteredUserList() {
            return registeredUserList;
        }

        public void setRegisteredUserList(List<RegisteredUser> registeredUserList) {
            this.registeredUserList = registeredUserList;
        }
    }
    private class RegisteredUser {
        private String username;
        private String password;
        private String address;
        private String bDay;
        private boolean isAdmin;
        private List<Store> stores;
        // Getter and setter methods
        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean admin) {
            isAdmin = admin;
        }

        public List<Store> getStores() {
            return stores;
        }

        public void setStores(List<Store> stores) {
            this.stores = stores;
        }
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getbDay() {
            return bDay;
        }

        public void setbDay(String bDay) {
            this.bDay = bDay;
        }
    }

    private class Store {
        private String founderName;
        private String storeName;
        private List<String> ownersList;//names of users
        private List<String> managersList;//names of users
        private List<Item> itemList;
        // Getter and setter methods
        public List<String> getOwnersList() {
            return ownersList;
        }

        public void setOwnersList(List<String> ownersList) {
            this.ownersList = ownersList;
        }

        public List<String> getManagersList() {
            return managersList;
        }

        public void setManagersList(List<String> managersList) {
            this.managersList = managersList;
        }

        public List<Item> getItemList() {
            return itemList;
        }

        public void setItemList(List<Item> itemList) {
            this.itemList = itemList;
        }
        public String getFounderName() {
            return founderName;
        }

        public void setFounderName(String founderName) {
            this.founderName = founderName;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }
    }

    private class Item {
        private String itemName;
        private double itemPrice;
        private String itemCategory;
        private double weight;
        private int amount;

        public Item(String itemName, double itemPrice, String itemCategory, double weight, int amount) {
            this.itemName = itemName;
            this.itemPrice = itemPrice;
            this.itemCategory = itemCategory;
            this.weight = weight;
            this.amount = amount;
        }

        // Getter and setter methods
        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public double getItemPrice() {
            return itemPrice;
        }

        public void setItemPrice(double itemPrice) {
            this.itemPrice = itemPrice;
        }

        public String getItemCategory() {
            return itemCategory;
        }

        public void setItemCategory(String itemCategory) {
            this.itemCategory = itemCategory;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

    }
}
