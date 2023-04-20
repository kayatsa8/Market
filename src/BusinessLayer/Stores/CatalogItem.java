package BusinessLayer.Stores;

public class CatalogItem {
    private String itemName;
    private double price;
    private int itemID;
    private String category;

    public CatalogItem(int itemID ,String itemName, double price, String category)
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
    public String getCategory()
    {
        return category;
    }
    public int getItemID()
    {
        return itemID;
    }

    public String setName(String newName) {
        String oldName = itemName;
        itemName = newName;
        return oldName;
    }
}
