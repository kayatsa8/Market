package BusinessLayer.Stores.Discounts.DiscountsTypes;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Conditions.LogicalCompositions.*;
import BusinessLayer.Stores.Conditions.LogicalCompositions.Rules.Rule;
import BusinessLayer.Stores.Conditions.LogicalCompositions.Rules.BasketTotalPriceRule;
import BusinessLayer.Stores.Conditions.LogicalCompositions.Rules.MustItemsAmountsRule;
import BusinessLayer.Stores.Discounts.DiscountScopes.DiscountScope;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;



public class Conditional extends DiscountType {
    private LogicalComponent root;
    private boolean finished;
    private List<LogicalComponent> inProgressList;
    private int logicalComponentsIDsCounter;

    public Conditional(int discountID, double percent, Calendar endOfSale, DiscountScope discountScope)
    {
        super(discountID, percent, endOfSale, discountScope);
        this.root = null;
        finished = false;
        inProgressList = new ArrayList<>();
        logicalComponentsIDsCounter = 1;
    }

    protected boolean checkConditions(List<CartItemInfo> basketItems, List<String> coupons)
    {
        return finished && root.checkConditions(basketItems);
    }
    @Override
    public String toString()
    {
        return super.toString() + ", the condition is: " + root.toString();
    }
    private String getConditionString(LogicalComponent logicalComponent)
    {
        return  logicalComponent.getID() + ": " + logicalComponent.toString();
    }
    public String addBasketTotalPriceRule(double minimumPrice)
    {
        return addRule(new BasketTotalPriceRule(minimumPrice, logicalComponentsIDsCounter++));
    }
    public String addQuantityRule(Map<Integer, Integer> itemsAmounts)
    {
        return addRule(new MustItemsAmountsRule(itemsAmounts, logicalComponentsIDsCounter++));
    }
    public String addComposite(LogicalComposites logicalComposite, List<Integer> logicalComponentsIDs) throws Exception
    {
        List<LogicalComponent> logicalComponents = new ArrayList<>();
        for (Integer logicalComponentID : logicalComponentsIDs)
        {
            LogicalComponent logicalComponent = getLogicalComponentByIDFromInProgressList(logicalComponentID);
            if (logicalComponent == null)
            {
                throw new Exception("Can't find logical component with id: " + logicalComponentID + " in discount of id: " + getDiscountID());
            }
            logicalComponents.add(logicalComponent);
        }
        switch (logicalComposite)
        {
            case AND:
            {
                LogicalComposite and = new And(logicalComponents, logicalComponentsIDsCounter++);
                removeLogicalComponentsFromInProgressList(logicalComponentsIDs);
                inProgressList.add(and);
                return getConditionString(and);
            }
            case OR:
            {
                LogicalComposite or = new Or(logicalComponents, logicalComponentsIDsCounter++);
                removeLogicalComponentsFromInProgressList(logicalComponentsIDs);
                inProgressList.add(or);
                return getConditionString(or);
            }
            case CONDITIONING:
            {
                return "We are currently not support conditioning in conditional discounts";
            }
        }
        return "Unrecognized logical composite type";
    }
    public String finish() throws Exception
    {
        if (inProgressList.size() == 0)
        {
            throw new Exception("There is no logical component to give to the conditional discount, please create one and try again");
        }
        else if (inProgressList.size() > 1)
        {
            throw new Exception("There are at least 2 logical components left, please remove one of them or combine them and try again");
        }
        else
        {
            root = inProgressList.get(0);
            inProgressList.remove(0);
            finished = true;
            return toString();
        }
    }


    private String addRule(Rule newRule)
    {
        inProgressList.add(newRule);
        return getConditionString(newRule);
    }
    private void removeLogicalComponentsFromInProgressList(List<Integer> logicalComponentsIDs) {
        inProgressList.removeIf(x -> logicalComponentsIDs.contains(x.getID()));
    }
    private LogicalComponent getLogicalComponentByIDFromInProgressList(Integer logicalComponentID) {
        for (LogicalComponent logicalComponent : inProgressList)
        {
            if (logicalComponent.getID() == logicalComponentID.intValue())
            {
                return logicalComponent;
            }
        }
        return null;
    }
}
