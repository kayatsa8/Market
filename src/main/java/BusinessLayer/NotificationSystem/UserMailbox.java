package BusinessLayer.NotificationSystem;

import BusinessLayer.NotificationSystem.Repositories.ChatRepository;
import BusinessLayer.NotificationSystem.Repositories.NotReadMessagesRepository;
import BusinessLayer.NotificationSystem.Repositories.ReadMessagesRepository;
import BusinessLayer.NotificationSystem.Repositories.SentMessagesRepository;
import BusinessLayer.Users.RegisteredUser;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;

public class UserMailbox extends Mailbox {

    private final RegisteredUser owner;

    public UserMailbox(RegisteredUser _owner, NotificationHub _hub){
        owner = _owner;
        ownerID = owner.getId();
        chats = new ChatRepository();
        hub = _hub;


//        notReadMessages = new NotReadMessagesRepository();
//        readMessages = new ReadMessagesRepository();
//        sentMessages = new SentMessagesRepository();
    }

    @Override
    public void notifyOwner() {
        // TODO: How to do this?
    }

}