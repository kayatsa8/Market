package BusinessLayer.Stores.Discounts.DiscountScopes;

import BusinessLayer.CartAndBasket.CartItemInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class ItemsDiscount implements DiscountScope {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    private List<Integer> itemIDs;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ItemsDiscount(List<Integer> itemIDs) {
        this.itemIDs = itemIDs;
    }

    public ItemsDiscount() {

    }

    public void setItemsPercents(List<CartItemInfo> copyBasket, double percent) //ByItemsList
    {
        for (CartItemInfo item: copyBasket)
        {
            if (itemIDs.contains(item.getItemID()))
            {
                item.setPercent(percent);
            }
            else
            {
                item.setPercent(0);
            }
        }
    }
    @Override
    public String toString()
    {
        return "Discount is applied on the items IDs: " + itemIDs;
    }

    public boolean isDiscountApplyForItem(int itemID, String category)
    {
        return itemIDs.contains(itemID);
    }

    @Override
    public void removeItem(int itemID) {
        itemIDs.remove(itemID);
    }

    public List<Integer> getItemIDs() {
        return itemIDs;
    }

    public void setItemIDs(List<Integer> itemIDs) {
        this.itemIDs = itemIDs;
    }
}
