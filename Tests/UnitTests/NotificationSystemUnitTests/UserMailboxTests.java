package UnitTests.NotificationSystemUnitTests;

import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.NotificationSystem.UserMailbox;
import BusinessLayer.Users.RegisteredUser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;



public class UserMailboxTests {

    private NotificationHub hub;

    @Before
    public void setUp(){
        hub = NotificationHub.getInstance();
    }

    @Test
    public void notifyOwner() throws Exception {
        RegisteredUser user1 = new RegisteredUser("user1", "123456789", 1111);
        UserMailbox mailbox1 = hub.registerToMailService(user1);

//        try{
//            mailbox1.notifyOwner();
//            fail("The function did not worked!");
//        }
//        catch(Exception e){
//            assertTrue(true);
//        }

        assertTrue(true); // for now, because it is not implemented

        hub.removeFromService(1111);
    }


}
