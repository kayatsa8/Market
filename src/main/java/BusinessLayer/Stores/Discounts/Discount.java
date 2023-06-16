package BusinessLayer.Stores.Discounts;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Discounts.DiscountScopes.DiscountScope;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Discount {
    @Id
    private int discountID;
    public Discount(int discountID)
    {
        this.discountID = discountID;
    }

    public Discount() {

    }

    public abstract List<CartItemInfo> updateBasket(List<CartItemInfo> basketItems, List<String> coupons);
    public int getDiscountID() { return discountID; }
    public abstract boolean isDiscountApplyForItem(int itemID, String category);
    public abstract void removeItem(int itemID);

    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }
}
