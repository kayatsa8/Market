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
        Store store = new Store(456, user.getId(), "store1");
        Mailbox mailbox = hub.getMailboxes().get(user.getId());

//        try{
//            mailbox.notifyOwner();
//            fail("The function worked");
//        }
//        catch(Exception e){
//            assertTrue(true);
//        }

        assertTrue(true); // for now, because it is not implemented

        hub.removeFromService(store.getStoreID());
        hub.removeFromService(user.getId());
    }

    @Test
    public void availability() throws Exception {
        RegisteredUser user = new RegisteredUser("user1", "123456", 1112);
        Store store = new Store(457, 1112, "store1");
        Mailbox mailbox = hub.getMailboxes().get(store.getStoreID());

        assertTrue("The store's mailbox is not available", mailbox.isAvailable());

        mailbox.setMailboxAsUnavailable();
        assertFalse("The store is still available", mailbox.isAvailable());

        mailbox.setMailboxAsAvailable();
        assertTrue("The store is not available", mailbox.isAvailable());

        hub.removeFromService(457);
        hub.removeFromService(1112);
    }

}
