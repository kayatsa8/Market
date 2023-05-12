package BusinessLayer.NotificationSystem.TestVersions;

import BusinessLayer.NotificationSystem.Repositories.ChatRepository;
import BusinessLayer.NotificationSystem.Repositories.NotReadMessagesRepository;
import BusinessLayer.NotificationSystem.Repositories.ReadMessagesRepository;
import BusinessLayer.NotificationSystem.Repositories.SentMessagesRepository;
import BusinessLayer.Users.RegisteredUser;

public class UserMailboxTestVersion extends MailBoxTestVersion{
    private final RegisteredUser owner;

    public UserMailboxTestVersion(RegisteredUser _owner, NotificationHubTestVersion _hub){
        owner = _owner;
        ownerID = owner.getId();
        chats = new ChatRepository();
        hub = _hub;
    }

    @Override
    public void notifyOwner() {
        // TODO: How to do this?
    }
}
