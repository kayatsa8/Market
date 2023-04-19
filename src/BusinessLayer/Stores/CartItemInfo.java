package BusinessLayer.Stores;

public class CartItemInfo {

    private int itemID;
    private int amount;
    private double percent;
    private double finalPrice;
    public CartItemInfo(int itemID, int amount, double percent, double finalPrice){
        this.itemID = itemID;
        this.amount = amount;
        this.percent = 0;
        this.finalPrice = 0;
    }

    public int getItemID() { return itemID; }
    public int getAmount() {
        return amount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public double getPercent() {
        return percent;
    }
    public void setPercent(double percent) { this.percent = percent; }
    public void setFinalPrice(double finalPrice) { this.finalPrice = finalPrice; }

}
