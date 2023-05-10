package BusinessLayer.Receipts.Receipt;

import BusinessLayer.Receipts.ReceiptItem.ReceiptItem;
import BusinessLayer.Receipts.ReceiptItem.ReceiptItemCollection;

import java.util.*;

public class Receipt {

    private ReceiptItemCollection collection;
    private int receiptId;
    private int ownerId;
    private Date date;

    public Receipt(int id, int ownerId, Calendar instance){
        collection = new ReceiptItemCollection();
        this.receiptId = id;
        this.ownerId = ownerId;
        this.date = instance.getTime();
    }

    public int getId() {
        return receiptId;
    }

    public void addItems(int id, List<ReceiptItem> items){
        for(ReceiptItem item: items){
            collection.add(id, item);
        }
    }

    public boolean itemExists(int userStoreId, int id){
        return collection.exists(userStoreId, id);
    }

    public boolean deleteItem(int userStoreId, int id){
        return collection.delete(userStoreId, id);
    }

    public boolean update(int userStoreId, int id, ReceiptItem item) {
        return collection.update(userStoreId, id, item);
    }

    public ReceiptItem getItem(int userStoreId, int id){
        return collection.get(userStoreId, id);
    }

    public boolean hasKeyId(int ownerId) {
        return collection.hasKeyId(ownerId);
    }

    public Date getDate() {
        return date;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public Map<Integer, ArrayList<ReceiptItem>> getAllItems(){
        return collection.getAllItems();
    }
}
