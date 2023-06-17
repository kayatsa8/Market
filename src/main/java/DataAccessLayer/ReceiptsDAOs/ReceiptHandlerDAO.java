package DataAccessLayer.ReceiptsDAOs;

import BusinessLayer.Market;
import BusinessLayer.Receipts.Receipt.Receipt;
import BusinessLayer.Receipts.ReceiptHandler;
import DataAccessLayer.Hibernate.DBConnector;

public class ReceiptHandlerDAO {

    public ReceiptHandlerDAO(){

    }

    public void addReceipt(ReceiptHandler handler, Receipt receipt) throws Exception {
        DBConnector<Receipt> receiptConnector =
                new DBConnector<>(Receipt.class, Market.getInstance().getConfigurations());
        receiptConnector.insert(receipt);

        DBConnector<ReceiptHandler> handlerConnector =
                new DBConnector<>(ReceiptHandler.class, Market.getInstance().getConfigurations());
        handlerConnector.saveState(handler);

    }

}
