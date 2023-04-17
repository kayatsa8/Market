package BusinessLayer.Receipts.ReceiptItem;

public class ReceiptItem {

    private int id;
    private String name;
    private double price;
    private int amount;

    public ReceiptItem(int id, String name, int amount, double price){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
