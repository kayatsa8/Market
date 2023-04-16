package Acceptance;

import org.junit.Test;

public class System_InitiationTests extends ProjectTest{



    /**
     * System Initiation #1
     */
    @Test
    public void systemInit_Valid(){
        //setUpAllMarket(); //this should be the test??
    }

    @Test
    public void systemInit_NoConnectionToPaymentService(){
        //System shouldn't start
    }

    @Test
    public void systemInit_NoConnectionToSupplyService(){
        //System shouldn't start
    }

}
