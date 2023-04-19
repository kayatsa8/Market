package UnitTests;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.Users.UserFacade;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
public class TestUser {
    static String goodPass1="ab123456";
    static String badPass1="123";
    static String userName1="avi1";
    static String userName2="avi2";
    static String userName3="avi3";
    static UserFacade userFacade;
    static int id1;

    @BeforeClass
    public static void setUp() throws Exception {
        userFacade=new UserFacade();
        id1=userFacade.registerUser(userName1,goodPass1);
    }

    @Test
    public void registerShouldPass(){
        try {
            userFacade.registerUser(userName2,goodPass1);//assume pass
            assertTrue("Registration succeeded",true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void registerShouldFailExisted(){
        try {
            int id=userFacade.registerUser(userName1,goodPass1);//assume pass
//            NotificationHub.getInstance().removeFromService(id);
            userFacade.registerUser(userName1,goodPass1);
            fail("should not pass this test : userName existed");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }
    @Test
    public void registerShouldFailPassword(){
        try {
            userFacade.registerUser(userName3,badPass1);
            fail("should not pass this test : password incorrect");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }
    @Test
    public void registerShouldFailPassNull(){
        try {
            userFacade.registerUser(userName3,null);
            fail("should not pass this test : password is Null");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }
    @Test
    public void registerShouldFailUserNull(){
        try {
            userFacade.registerUser(null,goodPass1);
            fail("should not pass this test : user name is Null");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }
    @Test
    public void logInShouldPass(){
        try {
            userFacade.logIn(userName1,goodPass1);
            assertTrue("logIn succeeded",true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void logInShouldFailNotRegistered(){
        try {
            userFacade.logIn(userName3,badPass1);
            fail("should not pass this test : Not Registered");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }
    @Test
    public void logInShouldFailWrongPass(){
        try {
            userFacade.logIn(userName1,badPass1);
            fail("should not pass this test : password incorrect");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }

    @Test
    public void logInShouldFailPassNull(){
        try {
            userFacade.logIn(userName1,null);
            fail("should not pass this test : password is Null");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }

    @Test
    public void logInShouldFailUserNull(){
        try {
            userFacade.logIn(null,badPass1);
            fail("should not pass this test : userName is Null");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);
        }
    }






}

