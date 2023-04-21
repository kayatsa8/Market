package Acceptance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.*;

public class SystemNotificationTests extends ProjectTest{


    @Before
    public void setUp() {
        super.setUp();
        setUpAllMarket();
    }


    @After
    public void tearDown() {
    }


    /**
     * Live Notification #5
     */
    @Test
    public void getLiveNotification(){
        closeStore(user2LoggedInId, store2Id);

        List<String> notifications = getNotifications(user2LoggedInId);
        assertTrue(notifications.contains("Closed Store"));

    }

    /**
     * delayed Notifications #6
     */
    @Test
    public void delayedNotifications_Valid(){
        logOut("User4", "User4!!");

        this.closeStore(user4LoggedInId, store4Id);
        loginUser("User4", "User4!");

        List<String> notifications = getNotifications(user3NotLoggedInId);
        assertEquals(notifications.get(0), "Closed store");
    }


}
