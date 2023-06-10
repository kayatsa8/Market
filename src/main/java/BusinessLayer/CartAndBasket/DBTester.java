package BusinessLayer.CartAndBasket;

import BusinessLayer.Market;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import DataAccessLayer.Hibernate.ConnectorConfigurations;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.ArrayList;

public class DBTester {

    public static void main(String[] args){
        String url = "jdbc:mysql://localhost:3306/shefaissashar";
        String driver = "com.mysql.cj.jdbc.Driver";
        ConnectorConfigurations conf = new ConnectorConfigurations("Name", url, "root", "S41r@kT1e", driver);

//        infoTest(conf);

//        wrapperTest(conf);

        basketTest(conf);
    }


    public static void infoTest(ConnectorConfigurations conf){
        DBConnector<CartItemInfo> infoConnector = new DBConnector<>(CartItemInfo.class, conf);

        CartItemInfo info1 = new CartItemInfo(1, 1, 1.0, "c1", "item1", 1.0);

        infoConnector.insert(info1);

        CartItemInfo info2 = new CartItemInfo(2, 2, 2.0, "c2", "item2", 2.0);
        CartItemInfo info3 = new CartItemInfo(3, 3, 3.0, "c3", "item3", 3.0);
        CartItemInfo info4 = new CartItemInfo(4, 4, 4.0, "c4", "item4", 4.0);

        infoConnector.insert(info2);
        infoConnector.insert(info3);
        infoConnector.insert(info4);

        info1.setAmount(17);
        infoConnector.saveState(info1);

        System.out.println(infoConnector.getAll());
    }

    public static void wrapperTest(ConnectorConfigurations conf){
//        DBConnector<Basket.ItemWrapper> wrapperConnector = new DBConnector<>(Basket.ItemWrapper.class, conf);
//
//        CatalogItem item1 = new CatalogItem(1, "item1", 1.0,
//                                           "c1", "store1", 11, 1.0);
//        Basket.ItemWrapper wrapper1 = new Basket.ItemWrapper(item1, 1);
//
//        CatalogItem item2 = new CatalogItem(2, "item2", 2.0,
//                "c2", "store2", 22, 2.0);
//        Basket.ItemWrapper wrapper2 = new Basket.ItemWrapper(item2, 2);
//
//        wrapperConnector.insert(wrapper1);
//        wrapperConnector.insert(wrapper2);
//
//
//        Basket.ItemWrapper w1 = wrapperConnector.getById(wrapper1.getId());
//        Basket.ItemWrapper w2 = wrapperConnector.getById(wrapper2.getId());
//        System.out.println(w1);
//        System.out.println(w2);

    }

    public static void basketTest(ConnectorConfigurations conf){
        try{
            Market.getInstance().setConfigurations(conf);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        Store store = new Store(1, 2, "store1");
        Basket basket = new Basket(store, 222);

        DBConnector<Basket> basketConnector = new DBConnector<>(Basket.class, conf);

        basketConnector.insert(basket);

        try{
            store.addCatalogItem(17, "item1", 53.22,
                    "category1", 82);
            store.addItemAmount(17, 33);

            basket.addItem(store.getItem(17), 22, new ArrayList<>());
        }
        catch(Exception e){
            System.err.println(e.getCause());
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            System.exit(1);
        }

        System.out.println(basketConnector.getAll());
    }

}
