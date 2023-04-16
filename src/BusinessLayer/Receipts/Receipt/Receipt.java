package BusinessLayer.Receipts.Receipt;

import BusinessLayer.Receipts.ReceiptItem.ReceiptItem;
import BusinessLayer.Receipts.ReceiptItem.ReceiptItemCollection;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public boolean itemExists(int id){
        return collection.exists(id);
    }

    public boolean deleteItem(int id){
        return collection.delete(id);
    }

    public boolean update(int id, ReceiptItem item) {
        return collection.update(id, item);
    }

    public ReceiptItem getItem(int id){
        return collection.get(id);
    }

}
