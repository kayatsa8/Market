package DataAccessLayer.CartAndBasketDAOs;

import BusinessLayer.CartAndBasket.Basket;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Market;
import DataAccessLayer.Hibernate.ConnectorConfigurations;
import DataAccessLayer.Hibernate.DBConnector;

public class BasketDAO {

    ConnectorConfigurations config;

    public BasketDAO() {
        try {
            config = Market.getInstance().getConfigurations();
        } catch (Exception e) {
        }
    }

    public void addItem(Basket basket, Basket.ItemWrapper wrapper, boolean wrapperPersistent) throws Exception {
        if (wrapperPersistent) {
            DBConnector<CartItemInfo> infoConnector =
                    new DBConnector<>(CartItemInfo.class, config);
            infoConnector.saveState(wrapper.info);
        } else {
            DBConnector<Basket.ItemWrapper> wrapperConnector =
                    new DBConnector<>(Basket.ItemWrapper.class, config);
            wrapperConnector.insert(wrapper);
        }

        DBConnector<Basket> basketConnector = new DBConnector<>(Basket.class, config);
        basketConnector.saveState(basket);
    }

    public void changeItemQuantity(CartItemInfo info) {
        DBConnector<CartItemInfo> infoConnector =
                new DBConnector<>(CartItemInfo.class, config);
        infoConnector.saveState(info);
    }

    public void removeItem(Basket.ItemWrapper wrapper) {
        DBConnector<CartItemInfo> infoConnector =
                new DBConnector<>(CartItemInfo.class, config);
        infoConnector.delete(wrapper.getInfo().getId());
        DBConnector<Basket.ItemWrapper> wrapperConnector =
                new DBConnector<>(Basket.ItemWrapper.class, config);
        wrapperConnector.delete(wrapper.getId());
    }

    public void saveItems(Basket basket) {
        DBConnector<Basket> basketConnector =
                new DBConnector<>(Basket.class, config);
        basketConnector.saveState(basket);
    }

    public void releaseItems(Basket basket) {
        DBConnector<Basket> basketConnector =
                new DBConnector<>(Basket.class, config);
        basketConnector.saveState(basket);
    }

    public void updateBasketByCartItemInfoList(CartItemInfo info) {
        DBConnector<CartItemInfo> infoConnector =
                new DBConnector<>(CartItemInfo.class, config);
        infoConnector.saveState(info);
    }

}
