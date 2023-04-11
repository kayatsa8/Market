package Tests.Acceptance;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.notification.RunListener;
import static org.junit.Assert.*;

public class RegisteredUserTests extends ProjectTest{


    @Override
    public void setUp() {
        super.setUp();
        setUpUser3();
        setUpUser4();
    }


    @After
    public void tearDown() {
    }


    /**
     * Logout #16
     */
    @Test
    public void logout_Valid(){
        boolean loggedOut = this.logOut(user4LoggedInId);
        assertTrue(loggedOut);
    }

    @Test
    public void logOut_UserNotLoggedIn(){
        boolean loggedOut = this.logOut(user3NotLoggedInId);
        assertFalse(loggedOut);
    }

    /**
     * Open Store #17
     */
    @Test
    public void openStore_Valid(){
        int storeId = this.createStore(user2LoggedInId);
        assertTrue(storeId > 0);
    }

    @Test
    public void createStore_MissingInformation(){
        //Missing info should be here
        int storeId = this.createStore(user2LoggedInId);
        assertTrue(storeId > 0);
    }

    @Test
    public void createStore_NotLoggedIn(){
        int storeId = this.createStore(user3NotLoggedInId);
        assertFalse(storeId > 0);
    }



}
