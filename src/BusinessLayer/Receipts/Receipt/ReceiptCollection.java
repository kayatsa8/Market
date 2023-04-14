package BusinessLayer.Receipts.Receipt;

import BusinessLayer.CollectionI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Collection of receipts, used bu receipt handler
public class ReceiptCollection implements CollectionI<Receipt> {


    private HashMap<Integer,ArrayList<Receipt>> receipts;

    public ReceiptCollection(){
        receipts = new HashMap<>();
    }

    /**
     *
     * @param receiptId id of the receipt
     * @return the receipt
     */
    @Override
    public Receipt get(int receiptId) {
        for (Map.Entry<Integer,ArrayList<Receipt>> set : receipts.entrySet()) {
            for(Receipt receipt: set.getValue()){
                if(receipt.getId() == receiptId){
                    return receipt;
                }
            }
        }
        return null;
    }

    /**
     * @param ownerId id of user\store :
     *               If its collection in store the id will be of the user
     *               If its collection in user the id will be of the store
     * @param obj  the receipt to add
     */
    @Override
    public void add(int ownerId, Receipt obj) {
        receipts.putIfAbsent(ownerId, new ArrayList<>());
        receipts.get(ownerId).add(obj);
    }

    /**
     *
     * @param id of the receipt id we want to delete
     * @return true if successful
     */
    @Override
    public boolean delete(int id) {
        for (ArrayList<Receipt> receipts : receipts.values()) {
            for(Receipt receipt: receipts){
                if(receipt.getId() == id){
                    receipts.remove(receipt);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param id of the receipt id we want to delete
     * @param obj the receipt to update
     * @return true if successful
     */
    @Override
    public boolean update(int id, Receipt obj) {
        for (ArrayList<Receipt> receipts : receipts.values()) {
            for(Receipt receipt: receipts){
                if(receipt.getId() == id){
                    receipts.remove(receipt);
                    receipts.add(obj);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean exists(int id) {
        for (ArrayList<Receipt> receipts : receipts.values()) {
            for(Receipt receipt: receipts){
                if(receipt.getId() == id){
                    return true;
                }
            }
        }
        return false;
    }


    public List<Receipt> getByOwnerId(int ownerId) {
        return receipts.get(ownerId);
    }

    public List<Receipt> getAll() {
        ArrayList<Receipt> result = new ArrayList<>();
        for (Map.Entry<Integer,ArrayList<Receipt>> set : receipts.entrySet()) {
            result.addAll(set.getValue());
        }
        return result;
    }
}
