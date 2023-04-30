package BusinessLayer.Stores.Policies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PurchasePolicy extends Policy{

    private Set<PurchaseMethod> methods;


    public PurchasePolicy(){
        super();
        methods = new HashSet<>();
        methods.add(PurchaseMethod.IMMEDIATE);
    }

    public void addMethod(PurchaseMethod method){
        methods.add(method);
    }





}
