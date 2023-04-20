package BusinessLayer.CartAndBasket.Repositories.Carts;

import BusinessLayer.Basket;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class BasketsRepository {
    //TODO: add DAO
    private final ConcurrentHashMap<Integer, Basket> baskets; // <storeID, Basket>

    public BasketsRepository(){
        baskets = new ConcurrentHashMap<>();
    }

    public void putIfAbsent(int storeID, Basket basket){
        baskets.putIfAbsent(storeID, basket);
    }

    public Basket get(int storeID){
        return baskets.get(storeID);
    }

    public boolean containsKey(int key){
        return baskets.containsKey(key);
    }

    public Collection<Basket> values(){
        return baskets.values();
    }

    public void clear(){
        baskets.clear();
    }

    public ConcurrentHashMap<Integer, Basket> getBaskets(){
        return baskets;
    }



}
