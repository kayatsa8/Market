package BusinessLayer.CartAndBasket;

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

    public CartItemInfo(CartItemInfo other){
        itemID = other.itemID;
        amount = other.amount;
        percent = other.percent;
        originalPrice = other.originalPrice;
    }

    public int getItemID() { return itemID; }
    public int getAmount() { return amount; }
    public double getPercent() { return percent; }
    public double getOriginalPrice() { return originalPrice; }
    public void setPercent(double percent) { this.percent = percent; }
    public double getFinalPrice() { return originalPrice*amount*(1-percent); }

    public void setAmount(int _amount){
        amount = _amount;
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof CartItemInfo item)){
            return false;
        }

        return itemID == item.itemID
                && amount == item.amount
                && percent == item.percent
                && originalPrice == item.originalPrice;
    }
}
