package BusinessLayer.NotificationSystem.TestVersions;

import BusinessLayer.NotificationSystem.Chat;
import BusinessLayer.NotificationSystem.Message;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.NotificationSystem.Repositories.ChatRepository;
import BusinessLayer.NotificationSystem.Repositories.NotReadMessagesRepository;
import BusinessLayer.NotificationSystem.Repositories.ReadMessagesRepository;
import BusinessLayer.NotificationSystem.Repositories.SentMessagesRepository;
import BusinessLayer.StorePermissions.StoreEmployees;
import BusinessLayer.Stores.Store;

import java.util.List;
import java.util.stream.Collectors;

public class StoreMailboxTestVersion extends MailBoxTestVersion {
    private final Store owner;

    public StoreMailboxTestVersion(Store _owner, NotificationHubTestVersion _hub){
        owner = _owner;
        available = true;
        ownerID = owner.getStoreID();
        chats = new ChatRepository();
        hub = _hub;
    }

    @Override
    public void notifyOwner() throws Exception {
        List<Integer> IDs = owner.getStoreOwners().stream().map(StoreEmployees::getUserID).collect(Collectors.toList());
        IDs.addAll(owner.getStoreManagers().stream().map(StoreEmployees::getUserID).toList());
        Message notificationMessage;

        for(Integer id : IDs){
            notificationMessage = makeNotificationMessage(id);
            chats.putIfAbsent(id, new Chat(ownerID, id));
            chats.get(id).addMessage(notificationMessage);
            hub.passMessage(notificationMessage);
            //sentMessages.add(notificationMessage);
        }
    }

    private Message makeNotificationMessage(int id){
        String storeName = owner.getStoreName();
        String title = "A new message is waiting in " + storeName + "'s mailbox";
        String content = "You can view the message in the store's mailbox";

        return new Message(ownerID, id, content);
        //return new Message(ownerID, id, title, content);
    }

    public void sendMessage(int receiverID, String content){
        if(isAvailable()){
            super.sendMessage(receiverID, content);
        }
    }

    public void receiveMessage(Message message) throws Exception {
        if(isAvailable()){
            super.receiveMessage(message);
        }
    }

    public void sendMessageToList(List<Integer> staffIDs, String content){
        for(Integer id : staffIDs){
            sendMessage(id, content);
        }
    }
}
