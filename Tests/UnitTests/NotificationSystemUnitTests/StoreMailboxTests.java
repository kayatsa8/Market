package UnitTests.NotificationSystemUnitTests;

import BusinessLayer.NotificationSystem.Mailbox;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.NotificationSystem.StoreMailbox;
import BusinessLayer.Stores.Store;
import BusinessLayer.Users.RegisteredUser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;



public class StoreMailboxTests {
    private NotificationHub hub;

    @Before
    public void setUp(){
        hub = NotificationHub.getInstance();
    }

    @Test
    public void notifyOwner() throws Exception {
        RegisteredUser user = new RegisteredUser("user1", "123456", 1111);
        Store store = new Store(456, 1111, "store1");
        Mailbox temp = hub.registerToMailService(user);
        Mailbox mailbox = hub.registerToMailService(store);

        try{
            mailbox.notifyOwner();
            fail("The function worked");
        }
        catch(Exception e){
            assertTrue(true);
        }

        hub.removeFromService(456);
        hub.removeFromService(1111);
    }

    @Test
    public void availability() throws Exception {
        RegisteredUser user = new RegisteredUser("user1", "123456", 1111);
        Store store = new Store(456, 1111, "store1");
        Mailbox temp = hub.registerToMailService(user);
        StoreMailbox mailbox = hub.registerToMailService(store);

        assertTrue("The store's mailbox is not available", mailbox.isAvailable());

        mailbox.setMailboxAsUnavailable();
        assertFalse("The store is still available", mailbox.isAvailable());

        mailbox.setMailboxAsAvailable();
        assertTrue("The store is not available", mailbox.isAvailable());

        hub.removeFromService(456);
        hub.removeFromService(1111);
    }

}
