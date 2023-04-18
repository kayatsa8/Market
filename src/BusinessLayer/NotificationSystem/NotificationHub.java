package BusinessLayer.NotificationSystem;


import BusinessLayer.Stores.Store;
import BusinessLayer.Users.RegisteredUser;

import java.util.concurrent.ConcurrentHashMap;


/**
 * In order to register to the notification system:
 * NotificationHub.getInstance().registerToMailService(this);
 */
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
        int userID = user.getId();
        if(isRegistered(userID)){
            // LOG: ERROR: NotificationHub::registerToMailService: the user + userID + is already registered!
            throw new Exception("NotificationHub::registerToMailService: the user " + userID + " is already registered!");
        }
        UserMailbox mailbox = new UserMailbox(user);
        mailboxes.putIfAbsent(userID, mailbox);

        //LOG: Info: NotificationHub::registerToMailService: user + user.getID() + is registered to notification service
        return mailbox;
    }

    public StoreMailbox registerToMailService(Store store) throws Exception {
        int storeID = store.getStoreID();
        if(isRegistered(storeID)){
            // LOG: ERROR: NotificationHub::registerToMailService: the store + storeID + is already registered!
            throw new Exception("NotificationHub::registerToMailService: the store " + storeID + " is already registered!");
        }
        StoreMailbox mailbox = new StoreMailbox(store);
        mailboxes.putIfAbsent(storeID, mailbox);

        //LOG: Info: NotificationHub::registerToMailService: store + store.getID() + is registered to notification service
        return mailbox;
    }

    public void removeFromService(int ID) throws Exception {
        if(!isRegistered(ID)){
            // LOG: ERROR: NotificationHub::removeFromService: the given ID + ID + is not of a registered user or store!
            throw new Exception("NotificationHub::removeFromService: the given ID " + ID + " is not of a registered user or store!");
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
            // LOG: ERROR: copy the Exception message
            throw new Exception("NotificationHub::passMessage: given message is null");
        }

        if(!isRegistered(message.getSenderID())){
            // LOG: ERROR: copy the Exception message
            throw new Exception("NotificationHub::passMessage: the sender is not registered!");
        }

        if(!isRegistered(message.getReceiverID())){
            // LOG: ERROR: copy the Exception message
            throw new Exception("NotificationHub::passMessage: the receiver is not registered!");
        }

        if(message.getTitle() == null || message.getTitle().isBlank()){
            // LOG: ERROR: copy the Exception message
            throw new Exception("NotificationHub::passMessage: the title of the message is invalid!");
        }

        if(message.getContent() == null || message.getContent().isBlank()){
            // LOG: ERROR: copy the Exception message
            throw new Exception("NotificationHub::passMessage: the content of the message is invalid!");
        }
    }

}
