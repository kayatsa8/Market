package BusinessLayer.Stores;

public class CatalogItem {
    private String itemName;
    private double price;
    private int itemID;
    private Category category;

    public CatalogItem(int itemID ,String itemName, double price, Category category)
    {
        this.itemID = itemID;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
    }

    public String getItemName()
    {
        return itemName;
    }
    public double getPrice()
    {
        return price;
    }
    public Category getCategory()
    {
        return category;
    }
    public int getItemID()
    {
        return itemID;
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof CatalogItem item)){
            return false;
        }

        return itemName.equals(item.itemName)
                && price == item.price
                && itemID == item.itemID
                && category == item.category;
    }
}
