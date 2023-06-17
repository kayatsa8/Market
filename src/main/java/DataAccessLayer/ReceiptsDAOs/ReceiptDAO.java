package DataAccessLayer.ReceiptsDAOs;

import BusinessLayer.Market;
import BusinessLayer.Receipts.Pairs.ItemsPair;
import BusinessLayer.Receipts.Receipt.Receipt;
import BusinessLayer.Receipts.ReceiptItem.ReceiptItem;
import DataAccessLayer.Hibernate.DBConnector;

public class ReceiptDAO {

    public ReceiptDAO(){

    }

    public void addItems(Receipt receipt, ItemsPair pair, ReceiptItem item, boolean newPair) throws Exception {
        DBConnector<ReceiptItem> itemConnector =
                new DBConnector<>(ReceiptItem.class, Market.getInstance().getConfigurations());
        itemConnector.insert(item);

        DBConnector<ItemsPair> pairConnector =
                new DBConnector<>(ItemsPair.class, Market.getInstance().getConfigurations());

        if(newPair){
            pairConnector.insert(pair);
        }
        else{
            pairConnector.saveState(pair);
        }

        DBConnector<Receipt> receiptConnector =
                new DBConnector<>(Receipt.class, Market.getInstance().getConfigurations());
        receiptConnector.saveState(receipt);
    }

    public void deleteItem(ReceiptItem item) throws Exception {
        DBConnector<ReceiptItem> connector =
                new DBConnector<>(ReceiptItem.class, Market.getInstance().getConfigurations());
        connector.delete(item.getId());
    }

    public void update(Receipt receipt, ItemsPair pair, ReceiptItem toDelete, ReceiptItem toInsert) throws Exception {
        deleteItem(toDelete);
        addItems(receipt, pair, toInsert, false);
    }

}
