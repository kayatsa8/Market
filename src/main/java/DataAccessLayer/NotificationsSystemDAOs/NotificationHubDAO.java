package DataAccessLayer.NotificationsSystemDAOs;

import BusinessLayer.Market;
import BusinessLayer.NotificationSystem.Mailbox;
import BusinessLayer.NotificationSystem.NotificationHub;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.List;
import java.util.Map;

public class NotificationHubDAO {

    public NotificationHubDAO(){

    }

    public void loadMailboxes(NotificationHub hub) throws Exception {
        DBConnector<Mailbox> connector =
                new DBConnector<>(Mailbox.class, Market.getInstance().getConfigurations());

        List<Mailbox> mailboxList = connector.getAll();

        Map<Integer, Mailbox> mailboxes = hub.getMailboxes();

        for(Mailbox mailbox : mailboxList){
            mailboxes.put(mailbox.getOwnerID(), mailbox);
        }
    }

    public void registerToMailService(Mailbox mailbox) throws Exception {
        DBConnector<Mailbox> connector =
                new DBConnector<>(Mailbox.class, Market.getInstance().getConfigurations());
        connector.insert(mailbox);
    }

    public void removeFromService(Mailbox mailbox) throws Exception {
        DBConnector<Mailbox> connector =
                new DBConnector<>(Mailbox.class, Market.getInstance().getConfigurations());
        connector.delete(mailbox.getOwnerID());
    }
}
