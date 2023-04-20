package BusinessLayer.NotificationSystem;

import BusinessLayer.NotificationSystem.Repositories.NotReadMessagesRepository;
import BusinessLayer.NotificationSystem.Repositories.ReadMessagesRepository;
import BusinessLayer.NotificationSystem.Repositories.SentMessagesRepository;
import BusinessLayer.Users.RegisteredUser;

import java.util.ArrayList;

public class UserMailbox extends Mailbox {

    private final RegisteredUser owner;

    public UserMailbox(RegisteredUser _owner){
        owner = _owner;
        ownerID = owner.getId();
        notReadMessages = new NotReadMessagesRepository();
        readMessages = new ReadMessagesRepository();
        sentMessages = new SentMessagesRepository();
    }

    @Override
    protected void notifyOwner() {
        // TODO: How to do this?
    }

}
