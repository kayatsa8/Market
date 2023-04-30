package BusinessLayer.Stores.Policies;

import BusinessLayer.Stores.Policies.Discounts.DiscountType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscountPolicy extends Policy{


    private Set<DiscountType> discountsAllowed;


    public DiscountPolicy(){
        super();
        discountsAllowed = new HashSet<>();
    }




    //this function should get the discount for this item, what should be the return type?
    //if there is a condition: 20% of you also buy milk...
    public double getDiscountForItem(int itemId){
        //default for version1
        return 0;
    }


    public void addDiscountType(DiscountType type){
        discountsAllowed.add(type);
    }

}
