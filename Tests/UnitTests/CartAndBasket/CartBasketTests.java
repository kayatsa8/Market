package UnitTests.CartAndBasket;

import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.ExternalSystems.Purchase.PurchaseClient;
import BusinessLayer.ExternalSystems.Supply.SupplyClient;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Market;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.StoreFacade;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;


/**
 * Cart and basket are tested together since Basket is an extension of Cart
 * and functioning as a Cart for a certain Store
 */
public class CartBasketTests {

    static RegisteredUser storeOwner;
    static RegisteredUser client;
    static Store store1;
    static Store store2;
    static Market market;
    static StoreFacade storeFacade;
    static UserFacade userFacade;
    static Cart cart;
    static CatalogItem item1;
    static CatalogItem item2;
    static CatalogItem item3;
    static CatalogItem item4;

    @BeforeClass
    public static void setUp() throws Exception {
        NotificationHub.testMode = true;
        market = Market.getInstance();
        storeFacade = market.getStoreFacade();
        userFacade = market.getUserFacade();
        int user1ID = market.register("storeOwnerCartTests", "111111");
        storeOwner = userFacade.getRegisteredUser(user1ID);
        int user2ID = market.register("clientCartTests", "123456");
        client = userFacade.getRegisteredUser(user2ID);
        int store1ID = market.addStore(storeOwner.getId(), "store1");
        store1 = market.getStoreInfo(store1ID);
        item1 = market.addItemToStore(store1ID, "1", 2, "Books");
        market.addItemAmount(store1ID, item1.getItemID(), 100);
        item2 = market.addItemToStore(store1ID, "2", 3, "Books");
        market.addItemAmount(store1ID, item2.getItemID(), 100);
        int store2ID = market.addStore(storeOwner.getId(), "store2");
        store2 = market.getStoreInfo(store2ID);
        item3 = market.addItemToStore(store2ID, "3", 7, "Books");
        market.addItemAmount(store2ID, item3.getItemID(), 100);
        item4 = market.addItemToStore(store2ID, "4", 5, "Books");
        market.addItemAmount(store2ID, item4.getItemID(), 100);
        cart = client.getCart();
    }

    @Test
    public void addItem(){
        try{
            cart.addItem(store1, item1, 1);
            assertTrue("The item is not inside the cart!",
                    cart.isItemInCart(item1.getItemID(), store1.getStoreID()));

            cart.addItem(store1, item2, 5);
            assertTrue("The item is not inside the cart!",
                    cart.isItemInCart(item2.getItemID(), store1.getStoreID()));

            cart.addItem(store2, item3, 83);
            assertTrue("The item is not inside the cart!",
                    cart.isItemInCart(item2.getItemID(), store1.getStoreID()));
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void removeItem(){
        try{
            cart.addItem(store1, item1, 1);
            cart.addItem(store1, item2, 5);
            cart.addItem(store2, item3, 83);

            cart.removeItem(store1.getStoreID(), item1.getItemID());
            assertFalse("The item is inside the cart!",
                    cart.isItemInCart(item1.getItemID(), store1.getStoreID()));

            cart.removeItem(store1.getStoreID(), item2.getItemID());
            assertFalse("The item is inside the cart!",
                    cart.isItemInCart(item2.getItemID(), store1.getStoreID()));

            cart.removeItem(store2.getStoreID(), item3.getItemID());
            assertFalse("The item is inside the cart!",
                    cart.isItemInCart(item3.getItemID(), store2.getStoreID()));
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void changeItemQuantity(){

        HashMap<CatalogItem, CartItemInfo> map;

        try{
            cart.addItem(store1, item1, 1);
            cart.addItem(store1, item2, 5);
            cart.addItem(store2, item3, 83);

            cart.changeItemQuantity(store1.getStoreID(), item1.getItemID(), 17);
            map = cart.getItemsInBasket(store1.getStoreName());

            assertEquals("The item quantity was not change!",
                    getCiiFromMap(map, item1).getAmount(), 17);

            cart.changeItemQuantity(store1.getStoreID(), item2.getItemID(), 52);
            map = cart.getItemsInBasket(store1.getStoreName());
            assertEquals("The item quantity was not change!",
                    getCiiFromMap(map, item2).getAmount(), 52);

            cart.changeItemQuantity(store2.getStoreID(), item3.getItemID(), 44);
            map = cart.getItemsInBasket(store2.getStoreName());
            assertEquals("The item quantity was not change!",
                    getCiiFromMap(map, item3).getAmount(), 44);

            cart.empty();

        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    private CartItemInfo getCiiFromMap(HashMap<CatalogItem, CartItemInfo> map, CatalogItem item){
        for(CatalogItem t : map.keySet()){
            if(item.equals(t)){
                return map.get(t);
            }
        }

        return null;
    }

    @Test
    public void buy(){
        try{
            //cart.addItem(store1, item1, 1);
            //cart.addItem(store1, item2, 5);
            //cart.addItem(store2, item3, 83);

            HashMap<Integer, HashMap<CatalogItem, CartItemInfo>> receiptDate =
                    cart.buyCart(new PurchaseClient(), new SupplyClient(), "David Ha'Melekh 7");

            assertTrue("The store is not in the receipt!",
                    receiptDate.containsKey(store1.getStoreID()));
            assertTrue("The store is not in the receipt!",
                    receiptDate.containsKey(store2.getStoreID()));

            HashMap<CatalogItem, CartItemInfo> items;

            items = receiptDate.get(store1.getStoreID());

            assertTrue("Item is not in the receipt!", items.containsKey(item1));
            assertEquals("Quantity", 1, items.get(item1).getAmount());
            assertEquals("ID", item1.getItemID(), items.get(item1).getItemID());
            assertEquals("price", item1.getPrice() * 1, items.get(item1).getFinalPrice(), 0.0);
            assertEquals("percent", 0.0, items.get(item1).getPercent(), 0.0);
            assertEquals("original", item1.getPrice(), items.get(item1).getOriginalPrice(), 0);

            assertTrue("Item is not in the receipt!", items.containsKey(item2));
            assertEquals("Quantity", 5, items.get(item2).getAmount());
            assertEquals("ID", item2.getItemID(), items.get(item2).getItemID());
            assertEquals("price", item2.getPrice() * 5, items.get(item2).getFinalPrice(), 0.0);
            assertEquals("percent", 0.0, items.get(item2).getPercent(), 0.0);
            assertEquals("original", item2.getPrice(), items.get(item2).getOriginalPrice(), 0);


            items = receiptDate.get(store2.getStoreID());

            assertTrue("Item is not in the receipt!", items.containsKey(item3));
            assertEquals("Quantity", 83, items.get(item3).getAmount());
            assertEquals("ID", item3.getItemID(), items.get(item3).getItemID());
            assertEquals("price", item3.getPrice() * 83, items.get(item3).getFinalPrice(), 0.0);
            assertEquals("percent", 0.0, items.get(item3).getPercent(), 0.0);
            assertEquals("original", item3.getPrice(), items.get(item3).getOriginalPrice(), 0);

        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }


}
