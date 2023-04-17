package BusinessLayer.NotificationSystem;

import BusinessLayer.Users.RegisteredUser;

import java.util.ArrayList;

public class UserMailbox extends Mailbox {

    private final RegisteredUser owner;

    public UserMailbox(RegisteredUser _owner){
        owner = _owner;
        ownerID = owner.getId();
        notReadMessages = new ArrayList<>();
        readMessages = new ArrayList<>();
        sentMessages = new ArrayList<>();
    }

    @Override
    protected void notifyOwner() {
        // TODO: How to do this?
    }

}
