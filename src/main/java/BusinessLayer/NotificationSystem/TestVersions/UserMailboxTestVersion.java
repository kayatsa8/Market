package BusinessLayer.NotificationSystem.TestVersions;

import BusinessLayer.NotificationSystem.Repositories.NotReadMessagesRepository;
import BusinessLayer.NotificationSystem.Repositories.ReadMessagesRepository;
import BusinessLayer.NotificationSystem.Repositories.SentMessagesRepository;
import BusinessLayer.Users.RegisteredUser;

public class UserMailboxTestVersion extends MailBoxTestVersion{
    private final RegisteredUser owner;

    public UserMailboxTestVersion(RegisteredUser _owner){
        owner = _owner;
        ownerID = owner.getId();
        notReadMessages = new NotReadMessagesRepository();
        readMessages = new ReadMessagesRepository();
        sentMessages = new SentMessagesRepository();
    }

    @Override
    public void notifyOwner() {
        // TODO: How to do this?
    }
}
