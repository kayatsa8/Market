package BusinessLayer.Stores.Pairs;

import BusinessLayer.Pair;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SavedItemAmount implements Pair<Integer, Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int itemId;
    private int amount;

    public SavedItemAmount(int k, int v){
        itemId = k;
        amount = v;
    }

    public SavedItemAmount(){

    }

    @Override
    public Integer getItemId(){
        return itemId;
    }

    @Override
    public Integer getAmount(){
        return amount;
    }

    @Override
    public void setItemId(Integer k) {
        itemId = k;
    }

    @Override
    public void setAmount(Integer v) {
        amount = v;
    }

    public int getId(){
        return id;
    }

    public void setId(int _id){
        id = _id;
    }


}
