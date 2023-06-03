package BusinessLayer.CartAndBasket;

public class CartItemInfo {

    private int itemID;
    private int amount;
    private double percent;
    private double originalPrice;
    private String category;
    private String itemName;
    private double weight;
    public CartItemInfo(int itemID, int amount, double originalPrice, String category, String itemName, double weight){
        this.itemID = itemID;
        this.amount = amount;
        this.percent = 0;
        this.originalPrice = originalPrice;
        this.category = category;
        this.itemName = itemName;
        this.weight = weight;
    }

    public CartItemInfo(CartItemInfo other){
        itemID = other.itemID;
        amount = other.amount;
        percent = other.percent;
        originalPrice = other.originalPrice;
        category = other.category;
        itemName = other.itemName;
        weight = other.weight;
    }

    public int getItemID() { return itemID; }
    public int getAmount() { return amount; }
    public double getPercent() { return percent; }
    public double getOriginalPrice() { return originalPrice; }
    public String getCategory() { return category; }
    public String getItemName() { return itemName; }
    public double getWeight() { return weight; }
    public void setPercent(double percent) { this.percent = percent; }
    public void setOriginalPrice(double newPrice) { originalPrice = newPrice; }
    public void setAmount(int _amount){
        amount = _amount;
    }
    public void setCategory(String newCategory) { category = newCategory; }
    public void setItemName(String newItemName) { itemName = newItemName; }
    public void setWeight(double newWeight) { weight = newWeight; }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof CartItemInfo item)){
            return false;
        }

        return     itemID == item.itemID
                && amount == item.amount
                && percent == item.percent
                && originalPrice == item.originalPrice
                && category.equals(item.category)
                && itemName.equals(item.itemName);
    }

    public double getFinalPrice() { return originalPrice*amount*(100-percent)/100; }
}
