package BusinessLayer.Receipts.Receipt;

import BusinessLayer.Pair;
import BusinessLayer.Receipts.Pairs.ItemsPair;
import BusinessLayer.Receipts.ReceiptItem.ReceiptItem;
import BusinessLayer.Receipts.ReceiptItem.ReceiptItemCollection;

import java.util.*;

public class Receipt {

    //each receipt composed of Id of user/store to the items bought.
    //for example: For user receipt the key is all the storeIds he bought from and the value are the items

    private List<ItemsPair> items;
    private int receiptId;
    private int ownerId;
    private Date date;

    public Receipt(int id, int ownerId, Calendar instance){
        items = new ArrayList<>();
        this.receiptId = id;
        this.ownerId = ownerId;
        this.date = instance.getTime();
    }

    public int getId() {
        return receiptId;
    }

    public void addItems(int id, List<ReceiptItem> _items){
        for(ReceiptItem item: _items){
            ItemsPair pair = (ItemsPair) Pair.searchPair(items, id);

            if(pair == null){
                pair = new ItemsPair(id, new ArrayList<>());
                items.add(pair);
            }

            pair.getReceiptItems().add(item);
        }
    }

    public boolean itemExists(int userStoreId, int id){
        ItemsPair pair = (ItemsPair) Pair.searchPair(items, userStoreId);

        if(pair == null){
            return false;
        }

        for( ReceiptItem receiptItem : pair.getValue()){
            if(receiptItem.getId() == id){
                return true;
            }
        }
        return false;
    }

    public boolean deleteItem(int userStoreId, int id){
        ItemsPair pair = (ItemsPair) Pair.searchPair(items, userStoreId);

        if(pair == null){
            return false;
        }

        for( ReceiptItem receiptItem : pair.getValue()){
            if(receiptItem.getId() == id){
                items.remove(pair);
                return true;
            }
        }
        return false;
    }

    public boolean update(int userStoreId, int id, ReceiptItem item) {
        ItemsPair pair = (ItemsPair) Pair.searchPair(items, userStoreId);

        if(pair == null){
            return false;
        }

        for( ReceiptItem receiptItem : pair.getValue()){
            if(receiptItem.getId() == id){
                pair.getValue().remove(receiptItem);
                pair.getValue().add(item);
                return true;
            }
        }
        return false;
    }

    public ReceiptItem getItem(int userStoreId, int id){
        ItemsPair pair = (ItemsPair) Pair.searchPair(items, userStoreId);

        if(pair == null){
            return null;
        }

        for( ReceiptItem receiptItem : pair.getValue()){
            if(receiptItem.getId() == id){
                return receiptItem;
            }
        }
        return null;
    }

    public boolean hasKeyId(int ownerId) {
        return Pair.searchPair(items, ownerId) != null;
    }

    public Date getDate() {
        return date;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public Map<Integer, ArrayList<ReceiptItem>> getAllItems(){
        Map<Integer, ArrayList<ReceiptItem>> map = new HashMap<>();

        for(ItemsPair pair : items){
            map.put(pair.getKey(), new ArrayList<>(pair.getValue()));
        }

        return map;
    }
}
