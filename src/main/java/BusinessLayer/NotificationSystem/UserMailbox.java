package BusinessLayer.NotificationSystem;

import BusinessLayer.NotificationSystem.Observer.NotificationObserver;
import BusinessLayer.NotificationSystem.Repositories.ChatRepository;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
public class UserMailbox extends Mailbox {
    @Transient
    private User owner;
    @Transient
    private List<NotificationObserver> listeners;


    public UserMailbox(User _owner, NotificationHub _hub){
        owner = _owner;
        ownerID = owner.getId();
        chats = new ArrayList<>();
        hub = _hub;
        listeners = new ArrayList<>();


//        notReadMessages = new NotReadMessagesRepository();
//        readMessages = new ReadMessagesRepository();
//        sentMessages = new SentMessagesRepository();
    }

    public UserMailbox() {

    }

    public void listen(NotificationObserver _listener){
        listeners.add(_listener);
    }

    @Override
    public void notifyOwner() {
        for(NotificationObserver listener : listeners){
            listener.notify("A new message is waiting for you!");
        }
    }

}
