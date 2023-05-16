package BusinessLayer.NotificationSystem;

import BusinessLayer.NotificationSystem.Observer.NotificationObserver;
import BusinessLayer.NotificationSystem.Repositories.ChatRepository;
import BusinessLayer.Users.RegisteredUser;

import java.util.ArrayList;
import java.util.List;

public class UserMailbox extends Mailbox {

    private final RegisteredUser owner;
    private List<NotificationObserver> listeners;


    public UserMailbox(RegisteredUser _owner, NotificationHub _hub){
        owner = _owner;
        ownerID = owner.getId();
        chats = new ChatRepository();
        hub = _hub;
        listeners = new ArrayList<>();


//        notReadMessages = new NotReadMessagesRepository();
//        readMessages = new ReadMessagesRepository();
//        sentMessages = new SentMessagesRepository();
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
