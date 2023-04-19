package BusinessLayer.Stores;

public class CartItemInfo {

    private int amount;
    private double percent;
    private double finalPrice;
    public CartItemInfo(int amount, double percent, double finalPrice){
        this.amount = amount;
        this.percent = percent;
        this.finalPrice = finalPrice;
    }

    public int getAmount() {
        return amount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public double getPercent() {
        return percent;
    }
}
