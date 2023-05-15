package UnitTests.NotificationSystemUnitTests;

import BusinessLayer.Market;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.NotificationSystem.Observer.NotificationObserver;
import BusinessLayer.NotificationSystem.Observer.SimpleObserver;
import BusinessLayer.NotificationSystem.UserMailbox;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;



public class UserMailboxTests {

    static NotificationHub hub;
    static UserFacade userFacade;
    static RegisteredUser user1;
    static RegisteredUser user2;
    static Market market;
    @BeforeClass
    public static void setUp() throws Exception {
        market = Market.getInstance();
        hub = market.getNotificationHub();
        userFacade = market.getUserFacade();
        int user1ID = market.register("user1", "123456789");
        user1 = userFacade.getRegisteredUser(user1ID);
        int user2Id = market.register("user2", "awd1523");
        user2 = userFacade.getRegisteredUser(user2Id);

        userFacade.logIn("user1", "123456789");
        userFacade.logIn("user2", "awd1523");
    }

    @Test
    public void notifyOwner() throws Exception {
        UserMailbox mailbox1 = user1.getMailbox();

        NotificationObserver observer = new SimpleObserver();
        observer.listenToNotifications(user1.getId());

        user2.sendMessage(user1.getId(), "Hello There!");

        String givenNotification = ((SimpleObserver)observer).getGivenNotification();

        assertNotNull(givenNotification);
        assertEquals("A new message is wait for you!", givenNotification);

        hub.removeFromService(user1.getId());
    }


}
