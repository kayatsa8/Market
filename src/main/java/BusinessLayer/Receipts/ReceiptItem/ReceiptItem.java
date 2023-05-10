package BusinessLayer.Receipts.ReceiptItem;

public class ReceiptItem {

    private int id;
    private String name;
    private double priceBeforeDiscount;
    private int amount;
    private double finalPrice;

    public ReceiptItem(int id, String name, int amount, double priceBeforeDiscount, double finalPrice){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.finalPrice = finalPrice;
    }

    public double getPrice() {
        return priceBeforeDiscount;
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

    public double getFinalPrice() {
        return finalPrice;
    }
}
