package Tests.Acceptance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GuestVisitorTests extends ProjectTest{



    //Don't sure I need this for this class
    /*
    @Before
    public void setUp() {
        super.setUp();
        //this.goodShow0 = createShow0();
        //this.goodShow1 = createShow1();
    }
     */

    @After
    public void tearDown() {
        //this.goodShow0 = null;
        //this.goodShow1 = null;
    }


    /**
     * Register User #9
     */
    @Test
    public void registerUser(){
        int id = this.registerUser("unusedUserName", "unusedPassword!23");
        assertTrue(id > 0);
    }

    @Test
    public void registerUsedUser(){
        this.registerUser("used", "123U!");

        int id = this.registerUser("used", "12345U!");
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





}
