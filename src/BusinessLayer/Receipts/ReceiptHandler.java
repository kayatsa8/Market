package BusinessLayer.Receipts;

import BusinessLayer.Items.CatalogItem;
import BusinessLayer.Log;
import BusinessLayer.Receipts.Receipt.Receipt;
import BusinessLayer.Receipts.Receipt.ReceiptCollection;
import BusinessLayer.Receipts.ReceiptItem.ReceiptItem;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ReceiptHandler {
    private static final Logger log = Log.log;
    private static AtomicInteger counterIds = new AtomicInteger(0);
    private ReceiptCollection collection;
    public ReceiptHandler(){
        collection = new ReceiptCollection();
    }


    /**
     * add receipts to the owner of this class
     * Receipt of User will be : <StoreId, Items> (it may be more than one key in this receipt)
     * Receipt of Store will be: <UserId, Items>  (will be one key in this receipt)
     * @param ownerId the id of the class owner (store or user)
     * @param storeOrUserIdToItems :
     *        - If the ownerId is userId than the keys of this map would be store Ids he bought at.
     *          If the ownerId is storeId than the keys of this map would be the user bought from this store.
     *        - The value is a map of catalogItems to amount bought
     */
    public void addReceipt(int ownerId, HashMap<Integer,HashMap<CatalogItem, Integer>> storeOrUserIdToItems){
        Receipt newReceipt = new Receipt(ownerId, counterIds.getAndIncrement(), Calendar.getInstance());

        for (Map.Entry<Integer,HashMap<CatalogItem, Integer>> set : storeOrUserIdToItems.entrySet()) {
            ArrayList<ReceiptItem> items = convertToReceiptItems(set.getValue());
            newReceipt.addItems(set.getKey(), items);
        }
        log.info("Created receipt successfully.");
        collection.add(ownerId, newReceipt);
        log.info("Added receipt successfully.");
    }

    private ArrayList<ReceiptItem> convertToReceiptItems(HashMap<CatalogItem, Integer> catalogItemsToAmount){
        ArrayList<ReceiptItem> items = new ArrayList<>();
        for (Map.Entry<CatalogItem, Integer> set : catalogItemsToAmount.entrySet()) {
            CatalogItem catalogItem = set.getKey();
            int amount = set.getValue();
            items.add(new ReceiptItem(1, "TempConvert", amount,1.5));
            //items.add(new ReceiptItem(catalogItem.getId(), catalogItem.getName(), catalogItem.get, amount));
        }
        return items;
    }

    public List<Receipt> getStoreReceiptsFromUser(int storeId){
        return collection.getByOwnerId(storeId);
    }

    public List<Receipt> getUserReceiptsFromStore(int userId){
        return collection.getByOwnerId(userId);
    }

    public List<Receipt> getAllReceipts(){
        return collection.getAll();
    }

    public Receipt getReceipt(int receiptId){
        return collection.get(receiptId);
    }


}

