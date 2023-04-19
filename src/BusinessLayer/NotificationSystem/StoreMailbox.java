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
        ownerID = owner.getStoreID();
        notReadMessages = new ArrayList<>();
        readMessages = new ArrayList<>();
        sentMessages = new ArrayList<>();
    }

    @Override
    public void notifyOwner() throws Exception {
        List<Integer> IDs = owner.getStoreOwners();
        IDs.addAll(owner.getStoreManagers());
        NotificationHub hub = NotificationHub.getInstance();
        Message notificationMessage;

        for(Integer id : IDs){
            notificationMessage = makeNotificationMessage(id);
            hub.passMessage(notificationMessage);
            sentMessages.add(notificationMessage);
        }
    }

    private Message makeNotificationMessage(int id){
        String storeName = owner.getStoreName();
        String title = "A new message is waiting in " + storeName + "'s mailbox";
        String content = "You can view the message in the store's mailbox";

        return new Message(ownerID, id, title, content);
    }

    public void setMailboxAsUnavailable(){
        available = false;
    }

    public void setMailboxAsAvailable(){
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
