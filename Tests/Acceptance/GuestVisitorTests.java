package Acceptance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GuestVisitorTests extends ProjectTest{



    @Before
    public void setUp() {
        super.setUp();
        setUpUser2();
        setUpUser3();
    }

    @After
    public void tearDown() {
        //delete stores and delete users from DB
        //delete info from registerUserValid()
    }


    /**
     * Load System #7
     */
    @Test
    public void loadSystemValid(){
        this.loadSystem();
        //TODO Yona
        //what to check here?
    }

    @Test
    public void loadSystemDBConnectionLost(){
        //TODO Yona
        //what to check here?
    }


    /**
     * Exit System #8
     */
    @Test
    public void exitSystemGuestValid(){
        //in the setup the guest has carts and items
        this.exitSystem(user1GuestId);
        //TODO Yona
        //assertTrue(cart of guest is null);
    }

    @Test
    public void exitSystemRegisteredUserValid(){
        //in the setup the registered user has carts and items
        this.exitSystem(user2LoggedInId);
        //TODO Yona
        //assertTrue(cart of user1 is null)
    }

    @Test
    public void exitSystemDBConnectionLost(){

    }


    /**
     * Register User #9
     * same as #24
     */
    @Test
    public void registerUserValid(){
        int id = this.registerUser("unusedUserName", "unusedPassword!23");
        assertTrue(id > 0);

    }

    @Test
    public void registerUsedUser(){
        int id = registerUser("User2", "User2!");
        assertTrue(id < 0);
    }

    @Test
    public void registerFaultyUser(){
        int id1 = this.registerUser("aa", "!");
        assertTrue(id1 < 0);

        int id2 = this.registerUser("aaaaa", "Y");
        assertTrue(id2 < 0);

        int id3 = this.registerUser("aaaaa", "a");
        assertTrue(id3 < 0);

    }



    /**
     * Login User #10
     */
    @Test
    public void loginUserValid(){
        boolean loggedIn = this.loginUser(user3NotLoggedInId, "User3!");
        assertTrue(loggedIn);
    }

    @Test
    public void loginUserWrongPassword(){
        boolean loggedIn = this.loginUser(user3NotLoggedInId, "Y!");
        assertFalse(loggedIn);
    }

    @Test
    public void loginUserNotRegistered(){
        boolean loggedIn = this.loginUser(user1GuestId, "Yona123!");
        assertFalse(loggedIn);

        loggedIn = this.loginUser(userNotExistId, "Yona123!");
        assertFalse(loggedIn);
    }




}
