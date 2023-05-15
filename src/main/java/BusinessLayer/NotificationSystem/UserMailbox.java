package BusinessLayer.NotificationSystem;

import BusinessLayer.NotificationSystem.Observer.NotificationObserver;
import BusinessLayer.NotificationSystem.Repositories.ChatRepository;
import BusinessLayer.Users.RegisteredUser;

public class UserMailbox extends Mailbox {

    private final RegisteredUser owner;
    private NotificationObserver listener;


    public UserMailbox(RegisteredUser _owner, NotificationHub _hub){
        owner = _owner;
        ownerID = owner.getId();
        chats = new ChatRepository();
        hub = _hub;
        listener = null;


//        notReadMessages = new NotReadMessagesRepository();
//        readMessages = new ReadMessagesRepository();
//        sentMessages = new SentMessagesRepository();
    }

    public void listen(NotificationObserver _listener){
        listener = _listener;
    }

    @Override
    public void notifyOwner() {
        listener.notify("A new message is wait for you!");
    }

}
