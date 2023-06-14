package BusinessLayer.Receipts.Receipt;

import BusinessLayer.Receipts.ReceiptItem.ReceiptItem;
import BusinessLayer.Receipts.ReceiptItem.ReceiptItemCollection;

import java.util.*;

public class Receipt {

    //each receipt composed of Id of user/store to the items bought.
    //for example: For user receipt the key is all the storeIds he bought from and the value are the items
    private HashMap<Integer,ArrayList<ReceiptItem>> items;
    private int receiptId;
    private int ownerId;
    private Date date;

    public Receipt(int id, int ownerId, Calendar instance){
        items = new HashMap<>();
        this.receiptId = id;
        this.ownerId = ownerId;
        this.date = instance.getTime();
    }

    public int getId() {
        return receiptId;
    }

    public void addItems(int id, List<ReceiptItem> _items){
        for(ReceiptItem item: _items){
            items.putIfAbsent(id, new ArrayList<>());
            items.get(id).add(item);
        }
    }

    public boolean itemExists(int userStoreId, int id){
        for( ReceiptItem receiptItem : items.get(userStoreId)){
            if(receiptItem.getId() == id){
                return true;
            }
        }
        return false;
    }

    public boolean deleteItem(int userStoreId, int id){
        for( ReceiptItem receiptItem : items.get(userStoreId)){
            if(receiptItem.getId() == id){
                items.get(userStoreId).remove(receiptItem);
                return true;
            }
        }
        return false;
    }

    public boolean update(int userStoreId, int id, ReceiptItem item) {
        for( ReceiptItem receiptItem : items.get(userStoreId)){
            if(receiptItem.getId() == id){
                items.get(userStoreId).remove(receiptItem);
                items.get(userStoreId).add(item);
                return true;
            }
        }
        return false;
    }

    public ReceiptItem getItem(int userStoreId, int id){
        for( ReceiptItem receiptItem : items.get(userStoreId)){
            if(receiptItem.getId() == id){
                return receiptItem;
            }
        }
        return null;
    }

    public boolean hasKeyId(int ownerId) {
        return items.containsKey(ownerId);
    }

    public Date getDate() {
        return date;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public Map<Integer, ArrayList<ReceiptItem>> getAllItems(){
        return items;
    }
}
