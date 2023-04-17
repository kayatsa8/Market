package Acceptance;

import org.junit.After;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.*;

public class SystemNotificationTests extends ProjectTest{


    @Override
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

        this.sendMsg(user2LoggedInId, user3NotLoggedInId, "Msg");
        loginUser("User3", "User3!");

        List<String> notifications = getNotifications(user3NotLoggedInId);
        assertEquals(notifications.get(0), "Msg");
    }


}
