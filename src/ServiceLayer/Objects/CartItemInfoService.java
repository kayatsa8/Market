package ServiceLayer.Objects;

import BusinessLayer.Stores.CartItemInfo;

public class CartItemInfoService {
    private final int itemID;
    private final int amount;
    private final double percent;
    private final double originalPrice;
    private final double finalPrice;

    public CartItemInfoService(CartItemInfo info){
        itemID = info.getItemID();
        amount = info.getAmount();
        percent = info.getPercent();
        originalPrice = info.getFinalPrice();
        finalPrice = info.getFinalPrice();
    }

    public int getItemID(){
        return itemID;
    }

    public int getAmount(){
        return amount;
    }

    public double getPercent(){
        return percent;
    }

    public double getOriginalPrice(){
        return originalPrice;
    }

    public double getFinalPrice(){
        return finalPrice;
    }



}
