package BusinessLayer.Stores;

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
    public CatalogItem(int itemID ,String itemName, double price, String category, String storeName, int storeID)
    {
        this.itemID = itemID;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.storeName = storeName;
        this.storeID = storeID;
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

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CatalogItem item)) {
            return false;
        }

        return itemName.equals(item.itemName)
                && price == item.price
                && itemID == item.itemID
                && category.equals(item.category);
    }

    public String setName(String newName) {
        String oldName = itemName;
        itemName = newName;
        return oldName;
    }
}
