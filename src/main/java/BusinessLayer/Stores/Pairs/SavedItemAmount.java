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
    public Integer getKey(){
        return itemId;
    }

    @Override
    public Integer getValue(){
        return amount;
    }

    @Override
    public void setKey(Integer k) {
        itemId = k;
    }

    @Override
    public void setValue(Integer v) {
        amount = v;
    }

    public int getId(){
        return id;
    }

    public void setId(int _id){
        id = _id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
