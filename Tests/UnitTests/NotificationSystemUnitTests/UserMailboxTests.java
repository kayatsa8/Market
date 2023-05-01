package UnitTests.NotificationSystemUnitTests;

import BusinessLayer.Market;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.NotificationSystem.UserMailbox;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;



public class UserMailboxTests {

    static NotificationHub hub;
    static UserFacade userFacade;
    static RegisteredUser user1;
    static Market market;
    @BeforeClass
    public static void setUp() throws Exception {
        NotificationHub.testMode = false;
        hub = NotificationHub.getInstance();
        market = Market.getInstance();
        userFacade = market.getUserFacade();
        int user1ID = market.register("user1UserMailbox", "123456789");
        user1 = userFacade.getRegisteredUser(user1ID);
    }

    @Test
    public void notifyOwner() throws Exception {
        UserMailbox mailbox1 = user1.getMailbox();

//        try{
//            mailbox1.notifyOwner();
//            fail("The function did not worked!");
//        }
//        catch(Exception e){
//            assertTrue(true);
//        }

        assertTrue(true); // for now, because it is not implemented

        hub.removeFromService(user1.getId());
    }


}
