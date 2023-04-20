package BusinessLayer.CartAndBasket.Repositories.Baskets;

import BusinessLayer.Stores.CartItemInfo;

import java.util.List;

public class SavedItemsRepository {
    private List<CartItemInfo> savedItems;


    public SavedItemsRepository(){
        savedItems = null;
    }

    public void set(List<CartItemInfo> list){
        savedItems = list;
    }

    public List<CartItemInfo> getList(){
        return savedItems;
    }


}
