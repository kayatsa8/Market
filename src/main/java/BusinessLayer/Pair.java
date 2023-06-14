package BusinessLayer;

/**
 * this interface is supposed to replace the use of Map,
 * providing the same key-value services yet meant to be
 * a part of a list, in order to better fit the project
 * to work with hibernate.
 */
public interface Pair<Key, Value> {

    Key getItemId();

    Value getAmount();

    void setItemId(Key k);

    void setAmount(Value v);

}
