package ServiceLayer.Objects;

import BusinessLayer.Stores.CatalogItem;

public class CatalogItemService {

    private String itemName;
    private double price;
    private int itemID;
    private String category;
    private boolean inStock;

    public CatalogItemService(CatalogItem item, boolean inStock)
    {
        this.itemID = item.getItemID();
        this.itemName = item.getItemName();
        this.category = item.getCategory();
        this.price = item.getPrice();
        this.inStock = inStock;
    }

    public String getItemName(){
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public int getItemID() {
        return itemID;
    }

    public String getCategory() {
        return category;
    }

    public boolean isInStock() {
        return inStock;
    }
}
