package UnitTests;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.Users.UserFacade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
public class TestUser {
    String goodPass1="ab123456";
    String badPass1="123";
    String userName1="avi1";
    String userName2="avi2";
    UserFacade userFacade;


    @Before
    public void setUp() {
        userFacade=new UserFacade();
    }

    @Test
    public void registerShouldPass(){
        try {
            int id=userFacade.registerUser(userName1,goodPass1);//assume pass
            NotificationHub.getInstance().removeFromService(id);
            assertTrue("Registration succeeded",true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void registerShouldFailExisted(){
        try {
            int id=userFacade.registerUser(userName1,goodPass1);//assume pass
            NotificationHub.getInstance().removeFromService(id);
            userFacade.registerUser(userName1,goodPass1);
            fail("should not pass this test : userName existed");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }
    @Test
    public void registerShouldFailPassword(){
        try {
            userFacade.registerUser(userName1,badPass1);
            fail("should not pass this test : password incorrect");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }
    @Test
    public void registerShouldFailPassNull(){
        try {
            userFacade.registerUser(userName1,null);
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
            int id=userFacade.registerUser(userName1,goodPass1);//assume pass
            NotificationHub.getInstance().removeFromService(id);
            userFacade.logIn(userName1,goodPass1);
            assertTrue("logIn succeeded",true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void logInShouldFailNotRegistered(){
        try {
            int id=userFacade.registerUser(userName1,goodPass1);//assume pass
            NotificationHub.getInstance().removeFromService(id);
            userFacade.logIn(userName1,badPass1);
            fail("should not pass this test : Not Registered");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }
    @Test
    public void logInShouldFailWrongPass(){
        try {
            int id=userFacade.registerUser(userName1,goodPass1);//assume pass
            NotificationHub.getInstance().removeFromService(id);
            userFacade.logIn(userName1,badPass1);
            fail("should not pass this test : password incorrect");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }

    @Test
    public void logInShouldFailPassNull(){
        try {
            int id=userFacade.registerUser(userName1,goodPass1);//assume pass
            NotificationHub.getInstance().removeFromService(id);
            userFacade.logIn(userName1,null);
            fail("should not pass this test : password is Null");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }

    @Test
    public void logInShouldFailUserNull(){
        try {
            int id=userFacade.registerUser(userName1,goodPass1);//assume pass
            NotificationHub.getInstance().removeFromService(id);
            userFacade.logIn(null,badPass1);
            fail("should not pass this test : userName is Null");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }






}

