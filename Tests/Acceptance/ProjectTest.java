package Tests.Acceptance;

import Tests.Bridge.Bridge;
import Tests.Bridge.Driver;

public abstract class ProjectTest {

    private Bridge bridge;
    protected int user1Id;
    protected int user2Id;
    protected int user3Id;

    public void setUp() {
        this.bridge = Driver.getBridge();
        setUpUsers();
        setUpStores();
        setUpCarts();
    }

    private void setUpCarts() {
        //add carts to users and add items to carts
    }


    private void setUpStores() {
        //add stores to the system, at least 2
    }


    private void setUpUsers() {
        user1Id = registerUser("YonatanUser123", "YonatanPass123!");
        loginUser(user1Id, "YonatanPass123!");

        user2Id = registerUser("YonatanUser12345", "YonatanPass123!");
        loginUser(user2Id, "YonatanPass123!");

        //add user guest
        //user3Id = loginAsGuest()
    }


    /**
     * userName : YonatanUser
     * password : YonatanPass123
     * @return
     */
    protected int registerYonatan() {
        return registerUser("YonatanUser", "YonatanPass123!");
    }


    public int registerUser(String userName, String password) {
        return this.bridge.registerUser(userName, password);
    }


    protected boolean loginUser(int id, String password) {
        return this.bridge.loginUser(id, password);
    }

    protected void exitSystem(int id) {
        this.bridge.exitSystem(id);
    }

    protected void loadSystem() {
        this.bridge.loadSystem();
    }
}
