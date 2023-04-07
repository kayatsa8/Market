package Tests.Acceptance;

import Tests.Bridge.Bridge;
import Tests.Bridge.Driver;

public abstract class ProjectTest {

    private Bridge bridge;

    public void setUp() {
        this.bridge = Driver.getBridge();
        //setUpCities();
        //setUpHalls();
        //setUpUsers();
    }

    public int registerUser(String userName, String password) {
        return this.bridge.registerUser(userName, password);
    }



}
