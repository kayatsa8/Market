package BusinessLayer.NotificationSystem;

import BusinessLayer.Users.RegisteredUser;

import java.util.ArrayList;

public class UserMailbox extends Mailbox {

    private final RegisteredUser owner;

    public UserMailbox(RegisteredUser _owner){
        owner = _owner;
        ownerID = -1; // TODO: get the id from owner
        notReadMessages = new ArrayList<>();
        readMessages = new ArrayList<>();
        sentMessages = new ArrayList<>();
    }

    @Override
    protected void notifyOwner() {
        // TODO: implement when RegisteredUser implements this
    }

}
