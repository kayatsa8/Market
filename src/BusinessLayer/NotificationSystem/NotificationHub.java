package BusinessLayer.NotificationSystem;


import BusinessLayer.stores.Store;
import BusinessLayer.users.RegisteredUser;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationHub {

    // class attributes
    private static NotificationHub instance = null;
    private static final Object instanceLock = new Object();
    // class methods
    public static NotificationHub getInstance(){
        synchronized (instanceLock){
            if(instance == null){
                instance = new NotificationHub();
            }
        }
        return instance;
    }



    // fields
    private ConcurrentHashMap<Integer, Mailbox> mailboxes; // <ID, Mailbox>


    // object methods
    private NotificationHub() {
        mailboxes = new ConcurrentHashMap<>();
    }

    public UserMailbox registerToMailService(RegisteredUser user) throws Exception {
        int userID = -1; // TODO: get the ID from user
        if(isRegistered(userID)){
            throw new Exception("NotificationHub::registerToMailService: the user is already registered!");
        }
        UserMailbox mailbox = new UserMailbox(user);
        mailboxes.putIfAbsent(userID, mailbox);

        return mailbox;
    }

    public StoreMailbox registerToMailService(Store store) throws Exception {
        int storeID = -1; // TODO: get the ID from store
        if(isRegistered(storeID)){
            throw new Exception("NotificationHub::registerToMailService: the store is already registered!");
        }
        StoreMailbox mailbox = new StoreMailbox(store);
        mailboxes.putIfAbsent(storeID, mailbox);

        return mailbox;
    }

    public void removeFromService(int ID) throws Exception {
        if(!isRegistered(ID)){
            throw new Exception("NotificationHub::removeFromService: the given ID is not of a registered user or store!");
        }

        mailboxes.remove(ID);
    }

    public boolean isRegistered(int ID){
        return mailboxes.containsKey(ID);
    }

    public void passMessage(Message message) throws Exception{
        validatePassedMessage(message);

        Mailbox mailbox = mailboxes.get(message.getReceiverID());
        mailbox.receiveMessage(message);
    }

    private void validatePassedMessage(Message message) throws Exception {
        if(message == null){
            throw new Exception("NotificationHub::passMessage: given message is null");
        }

        if(!isRegistered(message.getSenderID())){
            throw new Exception("NotificationHub::passMessage: the sender is not registered!");
        }

        if(!isRegistered(message.getReceiverID())){
            throw new Exception("NotificationHub::passMessage: the receiver is not registered!");
        }

        if(message.getTitle() == null || message.getTitle().isBlank()){
            throw new Exception("NotificationHub::passMessage: the title of the message is invalid!");
        }

        if(message.getContent() == null || message.getContent().isBlank()){
            throw new Exception("NotificationHub::passMessage: the content of the message is invalid!");
        }
    }

}
