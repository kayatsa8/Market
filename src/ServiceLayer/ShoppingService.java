package ServiceLayer;

import BusinessLayer.Log;
import BusinessLayer.Market;

import java.util.logging.Logger;

public class ShoppingService {

    private static final Logger log = Log.log;
    private final Market market;

    public ShoppingService() {
        market = Market.getInstance();
    }


}
