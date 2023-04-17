package BusinessLayer.Stores.Policies.Discounts;

public class Conditional extends Discount{

    public Conditional(){
        super();
    }

    @Override
    public double getDiscountToItem() {
        return 0;
    }


}
