package BusinessLayer.NotificationSystem;

import BusinessLayer.Market;
import BusinessLayer.NotificationSystem.Repositories.ChatRepository;
import BusinessLayer.StorePermissions.StoreEmployees;
import BusinessLayer.Stores.Store;
import DataAccessLayer.NotificationsSystemDAOs.MailboxDAO;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class StoreMailbox extends Mailbox{

    @OneToOne
    @JoinColumn(name = "ownerID")
    private Store owner;

    public StoreMailbox(Store _owner, NotificationHub _hub){
        owner = _owner;
        available = true;
        ownerID = owner.getStoreID();
        chats = new ArrayList<>();
        hub = _hub;

        mailboxDAO = new MailboxDAO();

//        notReadMessages = new NotReadMessagesRepository();
//        readMessages = new ReadMessagesRepository();
//        sentMessages = new SentMessagesRepository();
    }

    public StoreMailbox(){
        mailboxDAO = new MailboxDAO();

        try{
            hub = Market.getInstance().getNotificationHub();
        }
        catch(Exception e){
            System.err.println("Error in StoreMailbox empty constructor");
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void notifyOwner() throws Exception {
        List<Integer> IDs = owner.getStoreOwners().stream().map(StoreEmployees::getUserID).collect(Collectors.toList());
        IDs.addAll(owner.getStoreManagers().stream().map(StoreEmployees::getUserID).toList());
        Message notificationMessage;
        Chat chat;

        for(Integer id : IDs){
            notificationMessage = makeNotificationMessage(id);

            chat = chats_searchChat(id);

            if(chat == null){
                chat = new Chat(ownerID, id);
                chats.add(chat);
            }

            chat.addMessage(notificationMessage);
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

    public Store getOwner() {
        return owner;
    }

    public void setOwner(Store owner) {
        this.owner = owner;
    }
}
