package UnitTests.CartAndBasket;

import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Market;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.StoreFacade;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
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
        market = Market.getInstance();
        storeFacade = market.getStoreFacade();
        userFacade = market.getUserFacade();
        String addressOk="addressOk";
        LocalDate bDayOk=LocalDate.of(1999, 7, 11);
        int user1ID = market.register("storeOwnerCartTests", "111111",addressOk,bDayOk);
        storeOwner = userFacade.getRegisteredUser(user1ID);
        userFacade.logIn(storeOwner.getUsername(), "111111");
        int user2ID = market.register("clientCartTests", "123456",addressOk,bDayOk);
        client = userFacade.getRegisteredUser(user2ID);
        userFacade.logIn(client.getUsername(), "123456");
        int store1ID = market.addStore(storeOwner.getId(), "store1");
        store1 = market.getStoreInfo(store1ID);
        item1 = market.addItemToStore(store1ID, "1", 2, "Books", 1);
        market.addItemAmount(store1ID, item1.getItemID(), 100);
        item2 = market.addItemToStore(store1ID, "2", 3, "Books", 1);
        market.addItemAmount(store1ID, item2.getItemID(), 100);
        int store2ID = market.addStore(storeOwner.getId(), "store2");
        store2 = market.getStoreInfo(store2ID);
        item3 = market.addItemToStore(store2ID, "3", 7, "Books", 1);
        market.addItemAmount(store2ID, item3.getItemID(), 100);
        item4 = market.addItemToStore(store2ID, "4", 5, "Books", 1);
        market.addItemAmount(store2ID, item4.getItemID(), 100);
        cart = client.getCart();
    }

    @Test
    public void addItem_positiveResults(){
        try{
            //GOOD
            cart.addItem(store1, item1, 1);
            assertTrue("The item is not inside the cart!",
                    cart.isItemInCart(item1.getItemID(), store1.getStoreID()));

            cart.addItem(store1, item2, 5);
            assertTrue("The item is not inside the cart!",
                    cart.isItemInCart(item2.getItemID(), store1.getStoreID()));

            cart.addItem(store2, item3, 83);
            assertTrue("The item is not inside the cart!",
                    cart.isItemInCart(item3.getItemID(), store2.getStoreID()));

        }
        catch(Exception e){
            fail(e.getMessage());
        }

        cart.empty();
    }

    @Test
    public void addItem_unrecognizedItem(){
        try{
            cart.addItem(store1, item3, 3);
            fail("The cart added an item of one store to another.");
        }
        catch (Exception e){
            assertEquals("ERROR: Basket::addItemToCart: the item is not in the store!", e.getMessage());
        }

        cart.empty();
    }

    @Test
    public void addItem_nonPositiveAmount(){
        try{
            cart.addItem(store2, item4, -17);
            fail("The cart added a non-positive amount of the item.");
        }
        catch(Exception e){
            assertEquals("ERROR: Basket::addItemToCart: given quantity is not valid!", e.getMessage());
        }

        cart.empty();
    }

    @Test
    public void addItem_alreadyInCart(){
        try{
            cart.addItem(store1, item1, 1);
            cart.addItem(store1, item1, 1);
        }
        catch(Exception e){
            fail("should be able to increase the amount in the cart. got error:\n" + e.getMessage());
        }

        cart.empty();
    }

    @Test
    public void removeItem_positiveResults(){
        try{
            cart.addItem(store1, item1, 1);
            cart.addItem(store1, item2, 5);
            cart.addItem(store2, item3, 83);

            //GOOD
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
        finally {
            cart.empty();
        }
    }

    @Test
    public void removeItem_storeAndItemNotExist(){
        try{
            cart.addItem(store1, item1, 1);
            cart.addItem(store1, item2, 5);
            cart.addItem(store2, item3, 83);

            cart.removeItem(78952, 2311);
            fail("The store and item does not exist!");
        }
        catch(Exception e){
            assertEquals("Cart::removeItemFromCart: the store " + 78952 + " was not found!", e.getMessage());
        }
        finally {
            cart.empty();
        }
    }

    @Test
    public void removeItem_itemNotExist(){
        try{
            cart.addItem(store1, item1, 1);
            cart.addItem(store1, item2, 5);
            cart.addItem(store2, item3, 83);

            cart.removeItem(store1.getStoreID(), 3251351);
            fail("An item that does not exists was removed!");
        }
        catch(Exception e){
            assertEquals("ERROR: Basket::removeItemFromCart: no such item in basket!", e.getMessage());
        }
        finally {
            cart.empty();
        }
    }

    @Test
    public void changeItemQuantity_positiveResults(){

        HashMap<CatalogItem, CartItemInfo> map;

        try{
            cart.addItem(store1, item1, 1);
            cart.addItem(store1, item2, 5);
            cart.addItem(store2, item3, 83);

            cart.changeItemQuantity(store1.getStoreID(), item1.getItemID(), 17);
            map = cart.getItemsInBasket(store1.getStoreName());

            //GOOD
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

        cart.empty();
    }

    @Test
    public void changeItemQuantity_storeAndItemNotExist(){
        try{
            cart.addItem(store1, item1, 1);
            cart.addItem(store1, item2, 5);
            cart.addItem(store2, item3, 83);

            cart.changeItemQuantity(78952, 2311, 1);
            fail("The store and item does not exist!");
        }
        catch(Exception e){
            assertEquals("Cart::changeItemQuantityInCart: the store " + 78952 + " was not found!", e.getMessage());
        }
        finally{
            cart.empty();
        }
    }

    @Test
    public void changeItemQuantity_itemNotExist(){
        try{
            cart.addItem(store1, item1, 1);
            cart.changeItemQuantity(store1.getStoreID(), 3251351, 1);
            fail("An item that does not exists was changed!");
        }
        catch(Exception e){
            assertEquals("ERROR: Basket::changeItemQuantityInCart: the item is not in the basket!", e.getMessage());
        }
        finally{
            cart.empty();
        }
    }

    @Test
    public void changeItemQuantity_nonPositiveQuantity(){
        try{
            cart.addItem(store1, item1, 1);
            cart.changeItemQuantity(store1.getStoreID(), item1.getItemID(), -50);
            fail("An item quantity was changed to non-positive!");
        }
        catch(Exception e){
            assertEquals("ERROR: Basket::changeItemQuantityInCart: given quantity is not valid!", e.getMessage());
        }
        finally{
            cart.empty();
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


    //Moved to BuyTestsIntegration
    /**
    @Test
    public void buy_positiveResults(){
        try{
            cart.addItem(store1, item1, 1);
            cart.addItem(store1, item2, 5);
            cart.addItem(store2, item3, 83);

            Map<Integer, Map<CatalogItem, CartItemInfo>> receiptData =
                    cart.buyCart(new PurchaseClient(), new SupplyClient(), getPurchaseInfo(), getSupplyInfo());

            assertNotEquals(null, receiptData);


            assertTrue("The store is not in the receipt!",
                    receiptData.containsKey(store1.getStoreID()));
            assertTrue("The store is not in the receipt!",
                    receiptData.containsKey(store2.getStoreID()));

            Map<CatalogItem, CartItemInfo> items;

            items = receiptData.get(store1.getStoreID());

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


            items = receiptData.get(store2.getStoreID());

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
        finally{
            refillStock();
        }
    }

    @Test
    public void buy_notBoughtItemInReceipt(){
        try{
            cart.addItem(store1, item1, 1);
            cart.addItem(store1, item2, 5);
            cart.addItem(store2, item3, 83);

            Map<Integer, Map<CatalogItem, CartItemInfo>> receiptData =
                    cart.buyCart(new PurchaseClient(), new SupplyClient(), getPurchaseInfo(), getSupplyInfo());

            Map<CatalogItem, CartItemInfo> items;

            items = receiptData.get(store1.getStoreID());
            assertFalse(items.containsKey(item4));

            items = receiptData.get(store2.getStoreID());
            assertFalse(items.containsKey(item4));
        }
        catch(Exception e){
            fail(e.getMessage());
        }
        finally{
            refillStock();
        }
    }

    @Test
    public void buy_unableToPurchase(){
        HashMap<String, HashMap<CatalogItem, CartItemInfo>> before = new HashMap<>();
        HashMap<String, HashMap<CatalogItem, CartItemInfo>> after;

        HashMap<Integer, Integer> beforeStore1 = new HashMap<>();
        HashMap<Integer, Integer> afterStore1 = new HashMap<>();

        HashMap<Integer, Integer> beforeStore2 = new HashMap<>();
        HashMap<Integer, Integer> afterStore2 = new HashMap<>();

        try{
            cart.addItem(store1, item1, 1);
            cart.addItem(store1, item2, 5);
            cart.addItem(store2, item3, 83);

            before = new HashMap<>(makeMapFor_buy_unableToPurchase());
            beforeStore1 = new HashMap<>(store1.getItemsAmount());
            beforeStore2 = new HashMap<>(store2.getItemsAmount());

            Map<Integer, Map<CatalogItem, CartItemInfo>> receiptData =
                    cart.buyCart(new PurchaseClientMock(false), new SupplyClient(), getPurchaseInfo(), getSupplyInfo());

        }
        catch(Exception e){
            assertEquals("Problem with Supply or Purchase", e.getMessage());

            after = new HashMap<>(makeMapFor_buy_unableToPurchase());
            assertTrue(checkSameQuantitiesInCarts(before, after));

            afterStore1 = new HashMap<>(store1.getItemsAmount());
            afterStore2 = new HashMap<>(store2.getItemsAmount());
            assertTrue(checkStoreStockUnchanged(beforeStore1, afterStore1));
            assertTrue(checkStoreStockUnchanged(beforeStore2, afterStore2));
        }
        finally{
            refillStock();
            cart.empty();
        }
    }

    @Test
    public void buy_unableToSupply(){
        HashMap<String, HashMap<CatalogItem, CartItemInfo>> before = new HashMap<>();
        HashMap<String, HashMap<CatalogItem, CartItemInfo>> after;

        HashMap<Integer, Integer> beforeStore1 = new HashMap<>();
        HashMap<Integer, Integer> afterStore1 = new HashMap<>();

        HashMap<Integer, Integer> beforeStore2 = new HashMap<>();
        HashMap<Integer, Integer> afterStore2 = new HashMap<>();

        try{
            cart.addItem(store1, item1, 1);
            cart.addItem(store1, item2, 5);
            cart.addItem(store2, item3, 83);

            before = new HashMap<>(makeMapFor_buy_unableToPurchase());
            beforeStore1 = new HashMap<>(store1.getItemsAmount());
            beforeStore2 = new HashMap<>(store2.getItemsAmount());

            Map<Integer, Map<CatalogItem, CartItemInfo>> receiptData =
                    cart.buyCart(new PurchaseClient(), new SupplyClientMock(false), getPurchaseInfo(), getSupplyInfo());

        }
        catch(Exception e){
            assertEquals("Problem with Supply or Purchase", e.getMessage());

            after = new HashMap<>(makeMapFor_buy_unableToPurchase());
            assertTrue(checkSameQuantitiesInCarts(before, after));

            afterStore1 = new HashMap<>(store1.getItemsAmount());
            afterStore2 = new HashMap<>(store2.getItemsAmount());
            assertTrue(checkStoreStockUnchanged(beforeStore1, afterStore1));
            assertTrue(checkStoreStockUnchanged(beforeStore2, afterStore2));
        }
        finally{
            refillStock();
            cart.empty();
        }
    }

    private HashMap<String, HashMap<CatalogItem, CartItemInfo>> makeMapFor_buy_unableToPurchase(){
        HashMap<String, HashMap<CatalogItem, CartItemInfo>> map = new HashMap<>();
        List<String> stores = cart.getStoresOfBaskets();

        try{
            for(String storeName : stores){
                map.putIfAbsent(storeName, cart.getItemsInBasket(storeName));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return map;
    }

    private boolean checkSameQuantitiesInCarts(HashMap<String, HashMap<CatalogItem, CartItemInfo>> before,
                                               HashMap<String, HashMap<CatalogItem, CartItemInfo>> after){
        HashMap<CatalogItem, CartItemInfo> beforeItems;
        HashMap<CatalogItem, CartItemInfo> afterItems;

        CatalogItem similar;

        if(before.keySet().size() != after.keySet().size()){
            return false;
        }

        for(String storeName : before.keySet()){
            if(!after.containsKey(storeName)){
                return false;
            }

            beforeItems = before.get(storeName);
            afterItems = after.get(storeName);

            if(beforeItems.keySet().size() != afterItems.keySet().size()){
                return false;
            }

            for(CatalogItem item : beforeItems.keySet()){
                similar = isItemInMap(item, afterItems);
                if(similar == null){
                    return false;
                }

                if(beforeItems.get(item).getAmount() != afterItems.get(similar).getAmount()){
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkStoreStockUnchanged(HashMap<Integer, Integer> before, HashMap<Integer, Integer> after){

        if(before.keySet().size() != after.keySet().size()){
            return false;
        }

        for(Integer itemId : before.keySet()){
            if(!after.containsKey(itemId)){
                return false;
            }

            if(!Objects.equals(before.get(itemId), after.get(itemId))){
                return false;
            }
        }

        return true;
    }

    // use after every buy test
    private void refillStock(){
        try{
            market.addItemAmount(store1.getStoreID(), item1.getItemID(), 100);
            market.addItemAmount(store1.getStoreID(), item2.getItemID(), 100);
            market.addItemAmount(store2.getStoreID(), item3.getItemID(), 100);
            market.addItemAmount(store2.getStoreID(), item4.getItemID(), 100);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private CatalogItem isItemInMap(CatalogItem item, HashMap<CatalogItem, CartItemInfo> map){
        for(CatalogItem ci : map.keySet()){
            if(item.equals(ci)){
                return ci;
            }
        }

        return null;
    }


    public PurchaseInfo getPurchaseInfo(){
        return new PurchaseInfo("123", 1, 2222, "asd", 1222, 1, LocalDate.of(2000, 1, 1));
    }

    public SupplyInfo getSupplyInfo(){
        return new SupplyInfo("Name", "address", "city", "counyrt", "asd");
    }
    */

}
