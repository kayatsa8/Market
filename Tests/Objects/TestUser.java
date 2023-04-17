package Objects;
import BusinessLayer.Users.UserFacade;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;
public class TestUser {
    String goodPass1="ab123456";
    String badPass1="123";
    String userName1="avi1";
    String userName2="avi2";
    UserFacade userFacade=new UserFacade();

    public void setUp() {

//        RegisteredUser  registeredUser=new RegisteredUser(userName1,goodPass1);

    }


    @After
    public void tearDown() {
    }

    @Test
    public void registerShouldPass(){
        try {
            userFacade.registerUser(userName1,goodPass1);
            assertTrue("Registration succeeded",true);
        } catch (Exception e) {
            fail(e.getMessage());
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
            userFacade.registerUser(null,badPass1);
            fail("should not pass this test : user name is Null");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }
    @Test
    public void logInShouldPass(){
        try {
            userFacade.registerUser(userName1,goodPass1);//assume pass
            userFacade.logIn(userName1,goodPass1);
            assertTrue("logIn succeeded",true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void logInShouldFailNotRegistered(){
        try {
            userFacade.registerUser(userName1,goodPass1);//assume pass
            userFacade.logIn(userName1,badPass1);
            fail("should not pass this test : Not Registered");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }
    @Test
    public void logInShouldFailWrongPass(){
        try {
            userFacade.registerUser(userName1,goodPass1);//assume pass
            userFacade.registerUser(userName1,badPass1);
            fail("should not pass this test : password incorrect");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }

    @Test
    public void logInShouldFailPassNull(){
        try {
            userFacade.registerUser(userName1,goodPass1);//assume pass
            userFacade.registerUser(userName1,null);
            fail("should not pass this test : password is Null");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }

    @Test
    public void logInShouldFailUserNull(){
        try {
            userFacade.registerUser(userName1,goodPass1);//assume pass
            userFacade.registerUser(null,badPass1);
            fail("should not pass this test : userName is Null");
        } catch (Exception e) {
            assertTrue("Test passed\nException thrown:"+e.getMessage(),true);

        }
    }






}

