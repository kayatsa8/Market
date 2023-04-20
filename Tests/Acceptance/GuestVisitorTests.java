package Acceptance;

import ServiceLayer.Objects.CartService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

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
    @Ignore
    @Test
    public void loadSystemValid(){
        //this.loadSystem();
        assertTrue(false);
    }


    @Ignore
    @Test
    public void loadSystemDBConnectionLost(){
        assertTrue(false);
    }


    /**
     * Exit System #8
     */

    @Test
    public void exitSystemGuest_Valid(){
        this.exitSystem(user1GuestId);
        CartService cart = this.getCart(user1GuestId);
        assertNull(cart);
        assertTrue(false);  //getCart not implemented yet
    }

    @Test
    @Ignore
    public void exitSystemDBConnectionLost(){
        assertTrue(false);
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
        int id = this.registerUser("User2", "User2!");
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
        boolean loggedIn = this.loginUser("User3", "User3!");
        assertTrue(loggedIn);

        //boolean check = checkIfLoggedIn(user3NotLoggedInId);
        //assertTrue(check);  /** Check Here*/
    }

    @Test
    public void loginUserWrongPassword(){
        boolean loggedIn = this.loginUser("User3", "Y!");
        assertFalse(loggedIn);
    }

    @Test
    public void loginUserNotRegistered(){
        boolean loggedIn = this.loginUser("User1", "Yona123!");
        assertFalse(loggedIn);

        loggedIn = this.loginUser("User0", "Yona123!");
        assertFalse(loggedIn);
    }




}
