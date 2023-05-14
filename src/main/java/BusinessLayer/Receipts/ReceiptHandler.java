package BusinessLayer.Receipts;


import BusinessLayer.Log;
import BusinessLayer.Receipts.Receipt.Receipt;
import BusinessLayer.Receipts.Receipt.ReceiptCollection;
import BusinessLayer.Receipts.ReceiptItem.ReceiptItem;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.CatalogItem;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ReceiptHandler {
    private static final Logger log = Log.log;
    private static AtomicInteger counterIds = new AtomicInteger(1);
    private ReceiptCollection receipts;
    public ReceiptHandler(){
        receipts = new ReceiptCollection();
    }


    /**
     * add receipts to the owner of this class
     * @param ownerId the id of the class owner (store or user)
     * @param storeOrUserIdToItems
     *        - If the ownerId is userId than the keys of this map would be store Ids he bought at.
     *          If the ownerId is storeId than the key of this map would be the user bought from this store.
     *
     *        - The value is a map of catalogItems to CartItemInfo
     *          CartItemInfo : (amount, percent, finalPrice)
     *
     *        for store: key = userId (you only have 1 key)
     *        for user: keys = storesId (you have multiple keys if he bought from several store)
     *
     */
    public int addReceipt(int ownerId, Map<Integer,Map<CatalogItem, CartItemInfo>> storeOrUserIdToItems){
        Receipt newReceipt = new Receipt(counterIds.getAndIncrement(), ownerId, Calendar.getInstance());

        for (Map.Entry<Integer,Map<CatalogItem, CartItemInfo>> set : storeOrUserIdToItems.entrySet()) {
            ArrayList<ReceiptItem> items = convertToReceiptItems(set.getValue());
            newReceipt.addItems(set.getKey(), items);
        }
        log.info("Created receipt successfully.");
        receipts.add(ownerId, newReceipt);
        log.info("Added receipt successfully.");
        return newReceipt.getId();
    }

    private ArrayList<ReceiptItem> convertToReceiptItems(Map<CatalogItem, CartItemInfo> catalogItemsToAmount){
        ArrayList<ReceiptItem> items = new ArrayList<>();
        for (Map.Entry<CatalogItem, CartItemInfo> set : catalogItemsToAmount.entrySet()) {
            CatalogItem catalogItem = set.getKey();
            CartItemInfo cartItemInfo = set.getValue();
            int amount = cartItemInfo.getAmount();
            double finalPrice = cartItemInfo.getFinalPrice();
            items.add(new ReceiptItem(catalogItem.getItemID(), catalogItem.getItemName(),amount, catalogItem.getPrice(), finalPrice));
            log.info("Added items to receipt successfully.");
        }
        return items;
    }

    public ArrayList<Receipt> getStoreReceiptsFromUser(int storeId){
        return receipts.getByOwnerId(storeId);
    }

    public ArrayList<Receipt> getUserReceiptsFromStore(int userId){
        return receipts.getByOwnerId(userId);
    }

    public ArrayList<Receipt> getAllReceipts(){
        return receipts.getAll();
    }

    public Receipt getReceipt(int receiptId){
        return receipts.get(receiptId);
    }


}
