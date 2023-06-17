package UnitTests.StoreTests;

import BusinessLayer.Market;
import BusinessLayer.StorePermissions.StoreEmployees;
import BusinessLayer.Stores.*;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BidsUnitTests {
    static StoreFacade storeFacade;
    static UserFacade userFacade;
    static RegisteredUser founder1;
    static RegisteredUser storeOwner1;
    static RegisteredUser storeOwner2;
    static RegisteredUser storeOwner3;
    static RegisteredUser storeOwner4;

    static RegisteredUser storeManager1;
    static RegisteredUser noRole;
    static CatalogItem item1;
    static Store store1;
    static Market market;
    static Bid bid1;

    @BeforeClass
    public static void setUp() throws Exception {
        System.setProperty("env", "test");
        market = Market.getInstance();
        storeFacade = market.getStoreFacade();
        userFacade = market.getUserFacade();
        String addressOk = "addressOk";
        LocalDate bDayOk = LocalDate.of(2022, 7, 11);
        int id1 = market.register("bidUserName1", "password1", addressOk, bDayOk);
        int id2 = market.register("bidUserName2", "password2", addressOk, bDayOk);
        int id3 = market.register("bidUserName3", "password3", addressOk, bDayOk);
        int id4 = market.register("bidUserName4", "password4", addressOk, bDayOk);
        int id5 = market.register("bidUserName5", "password5", addressOk, bDayOk);
        int id6 = market.register("bidUserName6", "password6", addressOk, bDayOk);
        int id7 = market.register("bidUserName7", "password7", addressOk, bDayOk);
        market.login("bidUserName1", "password1");
        market.login("bidUserName2", "password2");
        market.login("bidUserName3", "password3");
        market.login("bidUserName4", "password4");
        market.login("bidUserName5", "password5");
        market.login("bidUserName6", "password6");
        market.login("bidUserName7", "password7");
        founder1 = userFacade.getRegisteredUser(id1);
        storeOwner1 = userFacade.getRegisteredUser(id2);
        storeManager1 = userFacade.getRegisteredUser(id3);
        noRole = userFacade.getRegisteredUser(id4);
        storeOwner2 = userFacade.getRegisteredUser(id5);
        storeOwner3 = userFacade.getRegisteredUser(id6);
        storeOwner4 = userFacade.getRegisteredUser(id7);
        int storeID = market.addStore(founder1.getId(), "bidStoreName1");
        store1 = market.getStoreInfo(storeID);
        market.addOwner(founder1.getId(), id2, storeID);
        market.addOwner(founder1.getId(), id5, storeID);
        market.addOwner(founder1.getId(), id6, storeID);
        market.addOwner(founder1.getId(), id7, storeID);
        market.addManager(founder1.getId(), id3, storeID);
        item1 = market.addItemToStore(storeID, "item1", 10, "Books", 5);
    }

    @Test
    public void aAddBidNoStore() {
        try {
            market.addBid(999, item1.getItemID(), noRole.getId(), 5);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("No store with ID: 999", e.getMessage());
        }
    }

    @Test
    public void bAddBidNoItem() {
        try {
            market.addBid(store1.getStoreID(), 999, noRole.getId(), 5);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("Item ID: 999 does not exist", e.getMessage());
        }
    }

    @Test
    public void cAddBidSuccessfully() {
        try {
            int bidsBefore = store1.getBids().size();
            bid1 = market.addBid(store1.getStoreID(), item1.getItemID(), noRole.getId(), 5);
            assertEquals(bidsBefore + 1, store1.getBids().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void dGetBidsToReply() {
        try {
            Map<Integer, List<Bid>> x = market.getUserBidsToReply(storeOwner1.getId());
            assertEquals(1, x.size());
            assertTrue(x.keySet().contains(store1.getStoreID()));
            assertEquals(1, x.get(store1.getStoreID()).size());
            assertTrue(x.get(store1.getStoreID()).get(0).getRepliers().keySet().contains(storeOwner1.getId()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void eApproveNoBid() {
        try {
            market.approve(store1.getStoreID(), 999, storeOwner1.getId());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("Bid ID: 999 does not exist", e.getMessage());
        }
    }
    @Test
    public void fApproveNoPermission() {
        try {
            market.approve(store1.getStoreID(), bid1.getBidID(), noRole.getId());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("The user " + noRole.getId() + " is not allowed to reply to bid in this store", e.getMessage());
        }
    }

    @Test
    public void gApproveSuccess() {
        try {
            boolean result = market.approve(store1.getStoreID(), bid1.getBidID(), storeOwner1.getId());
            assertFalse(result);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void hApproveUserAlreadyReplied() {
        try {
            market.approve(store1.getStoreID(), bid1.getBidID(), storeOwner1.getId());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("The user " + storeOwner1.getId() + " has already replied to this bid", e.getMessage());
        }
    }
    @Test
    public void iRejectUserAlreadyReplied() {
        try {
            market.reject(store1.getStoreID(), bid1.getBidID(), storeOwner1.getId());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("The user " + storeOwner1.getId() + " has already replied to this bid", e.getMessage());
        }
    }

    @Test
    public void jCounterOfferNoBid() {
        try {
            market.counterOffer(store1.getStoreID(), 999, storeOwner3.getId(), 8);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("Bid ID: 999 does not exist", e.getMessage());
        }
    }
    @Test
    public void kCounterOfferNoPermission() {
        try {
            market.counterOffer(store1.getStoreID(), bid1.getBidID(), noRole.getId(), 8);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("The user " + noRole.getId() + " is not allowed to reply to bid in this store", e.getMessage());
        }
    }
    @Test
    public void lCounterOfferNonPositive() {
        try {
            market.counterOffer(store1.getStoreID(), bid1.getBidID(), storeOwner3.getId(), 0);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("The counter offer must be positive", e.getMessage());
        }
    }
    @Test
    public void mCounterOfferLowerThanBuyerOffer() {
        try {
            market.counterOffer(store1.getStoreID(), bid1.getBidID(), storeOwner3.getId(), 4);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("The counter offer must be higher than the offered price of the buyer", e.getMessage());
        }
    }
    @Test
    public void nCounterOfferSuccess() {
        try {
            boolean result = market.counterOffer(store1.getStoreID(), bid1.getBidID(), storeOwner3.getId(), 8);
            assertFalse(result);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void oCounterOfferAlreadyReplied() {
        try {
            market.counterOffer(store1.getStoreID(), bid1.getBidID(), storeOwner3.getId(), 8);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("The user " + storeOwner3.getId() + " has already replied to this bid", e.getMessage());
        }
    }

    @Test
    public void pRejectNoBid() {
        try {
            market.reject(store1.getStoreID(), 999, storeOwner2.getId());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("Bid ID: 999 does not exist", e.getMessage());
        }
    }
    @Test
    public void qRejectNoPermission() {
        try {
            market.reject(store1.getStoreID(), bid1.getBidID(), noRole.getId());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("The user " + noRole.getId() + " is not allowed to reply to bid in this store", e.getMessage());
        }
    }
    @Test
    public void rRejectSuccess() {
        try {
            boolean result = market.reject(store1.getStoreID(), bid1.getBidID(), storeOwner2.getId());
            assertTrue(result);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}