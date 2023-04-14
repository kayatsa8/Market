package BusinessLayer.Receipts.ReceiptItem;

import BusinessLayer.CollectionI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReceiptItemCollection implements CollectionI<ReceiptItem> {

    private HashMap<Integer,ArrayList<ReceiptItem>> items;

    public ReceiptItemCollection(){
        items = new HashMap<>();
    }

    @Override
    public ReceiptItem get(int id) {
        for (Map.Entry<Integer,ArrayList<ReceiptItem>> set : items.entrySet()) {
            for(ReceiptItem receiptItem: set.getValue()){
                if(receiptItem.getId() == id){
                    return receiptItem;
                }
            }
        }
        return null;
    }

    @Override
    public void add(int id, ReceiptItem item) {
        items.putIfAbsent(id, new ArrayList<>());
        items.get(id).add(item);
    }

    @Override
    public boolean delete(int receiptItemId) {
        for (Map.Entry<Integer,ArrayList<ReceiptItem>> set : items.entrySet()) {
            for(ReceiptItem receiptItem: set.getValue()){
                if(receiptItem.getId() == receiptItemId){
                    set.getValue().remove(receiptItem);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean update(int itemId, ReceiptItem item) {
        for (Map.Entry<Integer,ArrayList<ReceiptItem>> set : items.entrySet()) {
            for(ReceiptItem receiptItem: set.getValue()){
                if(receiptItem.getId() == itemId){
                    set.getValue().remove(receiptItem);
                    set.getValue().add(item);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean exists(int itemId) {
        for (Map.Entry<Integer,ArrayList<ReceiptItem>> set : items.entrySet()) {
            for(ReceiptItem receiptItem: set.getValue()){
                if(receiptItem.getId() == itemId){
                    return true;
                }
            }
        }
        return false;
    }
}
