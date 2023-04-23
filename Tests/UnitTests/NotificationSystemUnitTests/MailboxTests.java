package UnitTests.NotificationSystemUnitTests;

import BusinessLayer.NotificationSystem.Mailbox;
import BusinessLayer.NotificationSystem.Message;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.Users.RegisteredUser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 *  The following tests only the functionality of Mailbox,
 *  yet because Mailbox is an abstract class, UserMailbox
 *  will be used as the implementation of Mailbox
 */
public class MailboxTests {

    private NotificationHub hub;

    @Before
    public void setUp(){
        hub = NotificationHub.getInstance();
    }

    @Test
    public void send_receiveMessage() throws Exception {
        RegisteredUser user1 = new RegisteredUser("user1", "123456789", 6111);
        RegisteredUser user2 = new RegisteredUser("user2", "123456789", 6222);
        RegisteredUser user3 = new RegisteredUser("user3", "123456789", 6333);

        Mailbox mailbox1 = user1.getMailbox();
        Mailbox mailbox2 = user2.getMailbox();
        Mailbox mailbox3 = user3.getMailbox();

        Message message1 = new Message(user1.getId(), user2.getId(), "title1", "message1");

        mailbox1.sendMessage(message1.getReceiverID(), message1.getTitle(), message1.getContent());

        // Good case
        assertTrue("The message was not added the sent list!", mailbox1.watchSentMessages().contains(message1));
        assertTrue("The message was not added the not-read list!", mailbox2.watchNotReadMessages().contains(message1));
        // Bad case
        assertFalse("The message was found in different mailbox!", mailbox3.watchNotReadMessages().contains(message1));

        hub.removeFromService(user1.getId());
        hub.removeFromService(user2.getId());
        hub.removeFromService(user3.getId());

    }

    @Test
    public void markMessageAsRead() throws Exception {
        RegisteredUser user1 = new RegisteredUser("user1", "123456789", 7111);
        RegisteredUser user2 = new RegisteredUser("user2", "123456789", 7222);

        Mailbox mailbox1 = user1.getMailbox();
        Mailbox mailbox2 = user2.getMailbox();

        Message message1 = new Message(user1.getId(), user2.getId(), "title1", "message1");

        mailbox1.sendMessage(message1.getReceiverID(), message1.getTitle(), message1.getContent());

        mailbox2.markMessageAsRead(message1);

        assertTrue("The message was not passed to read list", mailbox2.watchReadMessages().contains(message1));
        assertFalse("The message was not removed from the not-read list", mailbox2.watchNotReadMessages().contains(message1));

        hub.removeFromService(user1.getId());
        hub.removeFromService(user2.getId());

    }

    @Test
    public void markMessageAsNotRead() throws Exception {
        RegisteredUser user1 = new RegisteredUser("user1", "123456789", 8111);
        RegisteredUser user2 = new RegisteredUser("user2", "123456789", 8222);

        Mailbox mailbox1 = user1.getMailbox();
        Mailbox mailbox2 = user2.getMailbox();

        Message message1 = new Message(user1.getId(), user2.getId(), "title1", "message1");

        mailbox1.sendMessage(message1.getReceiverID(), message1.getTitle(), message1.getContent());

        mailbox2.markMessageAsRead(message1);

        mailbox2.markMessageAsNotRead(message1);

        assertTrue("The message is not in not-read lis", mailbox2.watchNotReadMessages().contains(message1));
        assertFalse("The message was not removed from read list", mailbox2.watchReadMessages().contains(message1));

        hub.removeFromService(user1.getId());
        hub.removeFromService(user2.getId());
    }





}
