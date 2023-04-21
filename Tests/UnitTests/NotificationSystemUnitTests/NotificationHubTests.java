package UnitTests.NotificationSystemUnitTests;

import BusinessLayer.NotificationSystem.*;
import BusinessLayer.Stores.Store;
import BusinessLayer.Users.RegisteredUser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class NotificationHubTests {
    private NotificationHub hub;

    @Before
    public void setUp(){
        hub = NotificationHub.getInstance();
    }

    @Test
    public void registerUserToService(){
        RegisteredUser user = new RegisteredUser("user1", "123456", 1111);

        try{
            assertNotNull(user.getMailbox());
            hub.removeFromService(1111);
            assertFalse("The user is still registered", hub.isRegistered(user.getId()));
        }
        catch(Exception e){
            fail(e.getMessage());
        }



    }

    @Test
    public void registerStoreToService(){
        try{
            RegisteredUser user = new RegisteredUser("user1", "123456", 1111);
            Store store = new Store(456, 1111, "store1");
            StoreMailbox mailbox = store.getMailBox();

            assertNotNull(mailbox);

            hub.removeFromService(456);
            hub.removeFromService(1111);

            assertFalse("The store is still registered", hub.isRegistered(456));

        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void passMessage() throws Exception {
        RegisteredUser user1 = new RegisteredUser("user1", "123456789", 1111);
        RegisteredUser user2 = new RegisteredUser("user2", "123456789", 2222);
        RegisteredUser user3 = new RegisteredUser("user3", "123456789", 3333);

        Mailbox mailbox1 = user1.getMailbox();
        Mailbox mailbox2 = user2.getMailbox();
        Mailbox mailbox3 = user3.getMailbox();

        Message message1 = new Message(user1.getId(), user2.getId(), "title1", "message1");

        mailbox1.sendMessage(message1.getReceiverID(), message1.getTitle(), message1.getContent());

        // Good case
        assertTrue("The message was not sent properly!", mailbox1.watchSentMessages().contains(message1));
        assertTrue("The message was not sent properly!", mailbox2.watchNotReadMessages().contains(message1));
        // Bad case
        assertFalse("The message was sent to different mailbox!", mailbox3.watchNotReadMessages().contains(message1));

        hub.removeFromService(user1.getId());
        hub.removeFromService(user2.getId());
        hub.removeFromService(user3.getId());
    }


}
