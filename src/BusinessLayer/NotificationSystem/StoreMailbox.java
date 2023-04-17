package BusinessLayer.NotificationSystem;

import BusinessLayer.Stores.Store;

import java.util.ArrayList;
import java.util.List;

public class StoreMailbox extends Mailbox{

    private final Store owner;
    private boolean available;

    public StoreMailbox(Store _owner){
        owner = _owner;
        available = true;
        ownerID = -1; // TODO: get the id from owner
        notReadMessages = new ArrayList<>();
        readMessages = new ArrayList<>();
        sentMessages = new ArrayList<>();
    }

    @Override
    protected void notifyOwner() throws Exception {
        List<Integer> IDs = new ArrayList<>(); // TODO: get from store
        NotificationHub hub = NotificationHub.getInstance();
        Message notificationMessage;

        for(Integer id : IDs){
            notificationMessage = makeNotificationMessage(id);
            hub.passMessage(notificationMessage);
            sentMessages.add(notificationMessage);
        }
    }

    private Message makeNotificationMessage(int id){
        String storeName = "NAME"; // TODO: take name from store
        String title = "A new message is waiting in " + storeName + "'s mailbox";
        String content = "You can view the message in the store's mailbox";

        return new Message(ownerID, id, title, content);
    }

    private void setMailboxAsUnavailable(){
        available = false;
    }

    private void setMailboxAsAvailable(){
        available = true;
    }

    public boolean isAvailable(){
        return available;
    }

    public void sendMessage(int receiverID, String title, String content){
        if(isAvailable()){
            super.sendMessage(receiverID, title, content);
        }
    }

    public void receiveMessage(Message message) throws Exception {
        if(isAvailable()){
            super.receiveMessage(message);
        }
    }

    public void sendMessageToList(List<Integer> staffIDs, String title, String content){
        for(Integer id : staffIDs){
            sendMessage(id, title, content);
        }
    }
}
