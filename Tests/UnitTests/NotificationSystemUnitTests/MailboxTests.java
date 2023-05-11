package UnitTests.NotificationSystemUnitTests;

import BusinessLayer.Market;
import BusinessLayer.NotificationSystem.Mailbox;
import BusinessLayer.NotificationSystem.Message;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.StoreFacade;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 *  The following tests only the functionality of Mailbox,
 *  yet because Mailbox is an abstract class, UserMailbox
 *  will be used as the implementation of Mailbox
 */
public class MailboxTests {

    static NotificationHub hub;
    static Market market;
    static UserFacade userFacade;
    static StoreFacade storeFacade;
    static RegisteredUser user1;
    static RegisteredUser user2;
    static RegisteredUser user3;
    static RegisteredUser user4;
    static RegisteredUser user5;
    static RegisteredUser user6;
    static RegisteredUser user7;

    @BeforeClass
    public static void setUp() throws Exception {
        hub = NotificationHub.getInstance();
        market = Market.getInstance();
        userFacade = market.getUserFacade();
        storeFacade = market.getStoreFacade();
        int userID1 = market.register("user1B", "123456789");
        int userID2 = market.register("user2B", "123456789");
        int userID3 = market.register("user3B", "123456789");
        int userID4 = market.register("user4B", "123456789");
        int userID5 = market.register("user5B", "123456789");
        int userID6 = market.register("user6B", "123456789");
        int userID7 = market.register("user7B", "123456789");
        user1 = userFacade.getRegisteredUser(userID1);
        user2 = userFacade.getRegisteredUser(userID2);
        user3 = userFacade.getRegisteredUser(userID3);
        user4 = userFacade.getRegisteredUser(userID4);
        user5 = userFacade.getRegisteredUser(userID5);
        user6 = userFacade.getRegisteredUser(userID6);
        user7 = userFacade.getRegisteredUser(userID7);
    }

    @Test
    public void send_receiveMessage() throws Exception {
        Mailbox mailbox1 = user1.getMailbox();
        Mailbox mailbox2 = user2.getMailbox();
        Mailbox mailbox3 = user3.getMailbox();
        Message message1 = new Message(user1.getId(), user2.getId(), "title1", "message1");
        mailbox1.sendMessage(message1.getReceiverID(), message1.getTitle(), message1.getContent());
        assertTrue("The message was not added the sent list!", mailbox1.watchSentMessages().contains(message1));
        assertTrue("The message was not added the not-read list!", mailbox2.watchNotReadMessages().contains(message1));
        assertFalse("The message was found in different mailbox!", mailbox3.watchNotReadMessages().contains(message1));
        hub.removeFromService(user1.getId());
        hub.removeFromService(user2.getId());
        hub.removeFromService(user3.getId());
    }

    @Test
    public void markMessageAsRead() throws Exception {
        Mailbox mailbox1 = user4.getMailbox();
        Mailbox mailbox2 = user5.getMailbox();
        Message message1 = new Message(user4.getId(), user5.getId(), "title1", "message1");
        mailbox1.sendMessage(message1.getReceiverID(), message1.getTitle(), message1.getContent());
        mailbox2.markMessageAsRead(message1);
        assertTrue("The message was not passed to read list", mailbox2.watchReadMessages().contains(message1));
        assertFalse("The message was not removed from the not-read list", mailbox2.watchNotReadMessages().contains(message1));
        hub.removeFromService(user4.getId());
        hub.removeFromService(user5.getId());
    }

    @Test
    public void markMessageAsNotRead() throws Exception {
        Mailbox mailbox1 = user6.getMailbox();
        Mailbox mailbox2 = user7.getMailbox();
        Message message1 = new Message(user6.getId(), user7.getId(), "title1", "message1");
        mailbox1.sendMessage(message1.getReceiverID(), message1.getTitle(), message1.getContent());
        mailbox2.markMessageAsRead(message1);
        mailbox2.markMessageAsNotRead(message1);
        assertTrue("The message is not in not-read lis", mailbox2.watchNotReadMessages().contains(message1));
        assertFalse("The message was not removed from read list", mailbox2.watchReadMessages().contains(message1));
        hub.removeFromService(user6.getId());
        hub.removeFromService(user7.getId());
    }
}
