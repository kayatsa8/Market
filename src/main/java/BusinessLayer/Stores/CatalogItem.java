package BusinessLayer.Stores;

import BusinessLayer.Stores.Policies.Discounts.Discount;
import BusinessLayer.Stores.Policies.PurchasePolicies.PurchasePolicy;

import java.util.ArrayList;
import java.util.List;

public class CatalogItem {
    private String itemName;
    private double price;

    public String getStoreName() {
        return storeName;
    }

    public int getStoreID() {
        return storeID;
    }

    private int itemID;
    private String category;
    private String storeName;
    private int storeID;
    private double weight;
    private List<Discount> discounts;
    private List<PurchasePolicy> purchasePolicies;
    public CatalogItem(int itemID ,String itemName, double price, String category, String storeName, int storeID, double weight)
    {
        this.itemID = itemID;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.storeName = storeName;
        this.storeID = storeID;
        this.weight = weight;
        this.discounts = new ArrayList<>();
        this.purchasePolicies = new ArrayList<>();
    }

    public String getItemName()
    {
        return itemName;
    }
    public double getPrice()
    {
        return price;
    }
    public String getCategory()
    {
        return category;
    }
    public int getItemID()
    {
        return itemID;
    }
    public double getWeight() { return weight; }
    public List<Discount> getDiscounts() { return discounts; }
    public List<PurchasePolicy> getPurchasePolicies() { return purchasePolicies; }
    public void setDiscounts(List<Discount> discounts) { this.discounts = discounts; }
    public void setPurchasePolicies(List<PurchasePolicy> purchasePolicies) { this.purchasePolicies = purchasePolicies; }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CatalogItem item)) {
            return false;
        }

        return itemName.equals(item.itemName)
                && price == item.price
                && itemID == item.itemID
                && category.equals(item.category)
                && weight == item.weight ;
    }

    public String setName(String newName) {
        String oldName = itemName;
        itemName = newName;
        return oldName;
    }
}
