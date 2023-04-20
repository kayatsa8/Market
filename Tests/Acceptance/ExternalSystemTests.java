package Acceptance;

import ServiceLayer.Objects.CatalogItemService;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ExternalSystemTests extends ProjectTest{

    @Override
    public void setUp() {
        super.setUp();
        //setUpAllMarket();
    }


    @After
    public void tearDown() {
        //delete stores and delete users from DB
    }


    /**
     * Change connection with external system #2
     */
    @Test @Ignore
    public void changeConnectionToExternalSystem_Valid(){

    }

    @Test @Ignore
    public void changeConnectionToExternalSystem_WrongStoreInfo(){

    }

    @Test @Ignore
    public void changeConnectionToExternalSystem_WrongSystemInformation(){

    }

}
