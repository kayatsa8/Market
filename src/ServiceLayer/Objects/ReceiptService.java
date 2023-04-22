package ServiceLayer.Objects;

import BusinessLayer.Receipts.Receipt.Receipt;

import java.util.Date;

public class ReceiptService {

    private int id;
    private Date date;
    private int ownerId;
    public ReceiptService(Receipt receipt) {
        this.id = receipt.getId();
        this.date = receipt.getDate();
        this.ownerId = receipt.getOwnerId();
    }
}
