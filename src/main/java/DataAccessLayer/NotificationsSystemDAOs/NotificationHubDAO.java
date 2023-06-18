package DataAccessLayer.NotificationsSystemDAOs;

import BusinessLayer.Market;
import BusinessLayer.NotificationSystem.Mailbox;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.NotificationSystem.StoreMailbox;
import BusinessLayer.NotificationSystem.UserMailbox;
import BusinessLayer.Users.Guest;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationHubDAO {

    public NotificationHubDAO(){

    }

    public ConcurrentHashMap<Integer, Mailbox> loadMailboxes(NotificationHub hub) throws Exception {
        DBConnector<Mailbox> connector =
                new DBConnector<>(Mailbox.class, Market.getInstance().getConfigurations());

        List<Mailbox> mailboxList = connector.getAll();

        ConcurrentHashMap<Integer, Mailbox> mailboxes = hub.getMailboxes();

        for(Mailbox mailbox : mailboxList){
            if (mailbox.getOwnerID()> Guest.MAX_GUEST_USER_ID)
                mailboxes.put(mailbox.getOwnerID(), mailbox);
        }
        return mailboxes;
    }

    public void registerToMailService(UserMailbox mailbox) throws Exception {
        DBConnector<UserMailbox> connector =
                new DBConnector<>(UserMailbox.class, Market.getInstance().getConfigurations());
        connector.insert(mailbox);
    }

    public void registerToMailService(StoreMailbox mailbox) throws Exception {
        DBConnector<StoreMailbox> connector =
                new DBConnector<>(StoreMailbox.class, Market.getInstance().getConfigurations());
        connector.insert(mailbox);
    }

    public void removeFromService(Mailbox mailbox) throws Exception {
        DBConnector<Mailbox> connector =
                new DBConnector<>(Mailbox.class, Market.getInstance().getConfigurations());
        connector.delete(mailbox.getOwnerID());
    }
}
