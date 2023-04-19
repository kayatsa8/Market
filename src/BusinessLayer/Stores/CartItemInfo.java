package BusinessLayer.Stores;

public class CartItemInfo {

    private int itemID;
    private int amount;
    private double percent;
    private double originalPrice;
    public CartItemInfo(int itemID, int amount, double percent, double originalPrice){
        this.itemID = itemID;
        this.amount = amount;
        this.percent = 0;
        this.originalPrice = originalPrice;
    }

    public int getItemID() { return itemID; }
    public int getAmount() {
        return amount;
    }

    public double getFinalPrice() {
        return originalPrice*amount*(1-percent);
    }

    public double getPercent() {
        return percent;
    }
    public double getOriginalPrice() { return originalPrice; }
    public void setPercent(double percent) { this.percent = percent; }
}
