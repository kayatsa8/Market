package UnitTests.Receipts;

import BusinessLayer.Receipts.Receipt.Receipt;
import BusinessLayer.Receipts.ReceiptHandler;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.CatalogItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ReceiptHandlerTest {



    private ReceiptHandler handler;
    private int store1ID = 1;
    private int store2ID = 2;
    private int userId = 1;
    private int receipt1Id = 1;
    private Map<Integer, Map<CatalogItem, CartItemInfo>> items;
    @Before
    public void setUp() throws Exception {
        System.setProperty("env", "test");
        handler = new ReceiptHandler();

        items = new HashMap<>();
        HashMap<CatalogItem, CartItemInfo> itemsAndAmounts1 = new HashMap<>();
        CartItemInfo item1 = new CartItemInfo(11, 20, 10, "Books", "item11", 0.5);
        CartItemInfo item2 = new CartItemInfo(12, 20, 10, "Books", "item12", 0.5);
        CartItemInfo item3 = new CartItemInfo(21, 20, 10, "Books", "item21", 0.5);
        CartItemInfo item4 = new CartItemInfo(22, 20, 10, "Books", "item22", 0.5);
        item1.setPercent(0.2);
        item2.setPercent(0.2);
        item3.setPercent(0.2);
        item4.setPercent(0.2);

//        itemsAndAmounts1.put(new CatalogItem(11, "item11", 20, "Books", "name1", store1ID, 0.5), item1);
//        itemsAndAmounts1.put(new CatalogItem(12, "item12", 20, "Books", "name1", store1ID, 0.5), item2);
        HashMap<CatalogItem, CartItemInfo> itemsAndAmounts2 = new HashMap<>();
//        itemsAndAmounts2.put(new CatalogItem(21, "item21", 20, "Books", "name2", store2ID, 0.5), item3);
//        itemsAndAmounts2.put(new CatalogItem(22, "item22", 20, "Books", "name2", store2ID, 0.5), item4);
        items.put(store1ID, itemsAndAmounts1);
        items.put(store2ID, itemsAndAmounts2);
    }

    @After
    public void tearDown() throws Exception {
    }



// public void addReceipt(int ownerId, HashMap<Integer,HashMap<CatalogItem, Integer>> storeOrUserIdToItems){
    @Test
    public void addReceipt() throws Exception {
        receipt1Id = handler.addReceipt(userId, items);

        Receipt receipt1 = handler.getReceipt(receipt1Id);
        assertEquals(receipt1.getId(), receipt1Id);
        assertTrue(receipt1.itemExists(store1ID, 11));
        assertTrue(receipt1.itemExists(store1ID, 12));
        assertTrue(receipt1.itemExists(store2ID, 21));
        assertTrue(receipt1.itemExists(store2ID, 22));
    }

    @Test
    public void getStoreReceiptsFromUser() throws Exception {
        receipt1Id = handler.addReceipt(userId, items);
        ArrayList<Receipt> receipts = handler.getStoreReceiptsFromUser(store1ID);
        assertEquals(receipts.get(0).getId(), receipt1Id);
    }


    @Test
    public void getAllReceipts() throws Exception {
        receipt1Id = handler.addReceipt(userId, items);
        ArrayList<Receipt> receipts = handler.getAllReceipts();
        assertEquals(1, receipts.size());
    }

}