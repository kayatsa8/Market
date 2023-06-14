package DataAccessLayer.CartAndBasketDAOs;

import BusinessLayer.CartAndBasket.Basket;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Market;
import DataAccessLayer.Hibernate.ConnectorConfigurations;
import DataAccessLayer.Hibernate.DBConnector;

public class BasketDAO {

    public BasketDAO(){

    }

    public void addItem(Basket basket, Basket.ItemWrapper wrapper, boolean wrapperPersistent) throws Exception {
        if(wrapperPersistent){
            DBConnector<CartItemInfo> infoConnector =
                    new DBConnector<>(CartItemInfo.class, Market.getInstance().getConfigurations());
            infoConnector.saveState(wrapper.info);
        }
        else{
            DBConnector<Basket.ItemWrapper> wrapperConnector =
                    new DBConnector<>(Basket.ItemWrapper.class, Market.getInstance().getConfigurations());
            wrapperConnector.insert(wrapper);
        }

        DBConnector<Basket> basketConnector = new DBConnector<>(Basket.class, Market.getInstance().getConfigurations());
        basketConnector.saveState(basket);
    }

    public void changeItemQuantity(CartItemInfo info) throws Exception {
        DBConnector<CartItemInfo> infoConnector =
                new DBConnector<>(CartItemInfo.class, Market.getInstance().getConfigurations());
        infoConnector.saveState(info);
    }

    public void removeItem(Basket.ItemWrapper wrapper) throws Exception {
        DBConnector<CartItemInfo> infoConnector =
                new DBConnector<>(CartItemInfo.class, Market.getInstance().getConfigurations());
        infoConnector.delete(wrapper.getInfo().getId());
        DBConnector<Basket.ItemWrapper> wrapperConnector =
                new DBConnector<>(Basket.ItemWrapper.class, Market.getInstance().getConfigurations());
        wrapperConnector.delete(wrapper.getId());
    }

    public void saveItems(Basket basket) throws Exception {
        DBConnector<Basket> basketConnector =
                new DBConnector<>(Basket.class, Market.getInstance().getConfigurations());
        basketConnector.saveState(basket);
    }

    public void releaseItems(Basket basket) throws Exception {
        DBConnector<Basket> basketConnector =
                new DBConnector<>(Basket.class, Market.getInstance().getConfigurations());
        basketConnector.saveState(basket);
    }

    public void updateBasketByCartItemInfoList(CartItemInfo info) throws Exception {
        DBConnector<CartItemInfo> infoConnector =
                new DBConnector<>(CartItemInfo.class, Market.getInstance().getConfigurations());
        infoConnector.saveState(info);
    }

}
