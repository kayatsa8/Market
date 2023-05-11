package BusinessLayer.NotificationSystem.TestVersions;

import BusinessLayer.Log;
import BusinessLayer.NotificationSystem.Message;
import BusinessLayer.Stores.Store;
import BusinessLayer.Users.RegisteredUser;



public class NotificationHubTestVersion {

    private final MailboxesRepositoryTestVersion mailboxes;



    public NotificationHubTestVersion(){
        mailboxes = new MailboxesRepositoryTestVersion();
        Log.log.info("The notification hub has started successfully.");
    }

    public MailboxesRepositoryTestVersion getMailboxes() {
        return mailboxes;
    }

    public UserMailboxTestVersion registerToMailService(RegisteredUser user) throws Exception {
        int userID = user.getId();
        if (isRegistered(userID)) {
            Log.log.warning("ERROR: NotificationHub::registerToMailService: the user " + userID + " is already registered!");
            throw new Exception("NotificationHub::registerToMailService: the user " + userID + " is already registered!");
        }
        UserMailboxTestVersion mailbox = new UserMailboxTestVersion(user);
        mailboxes.putIfAbsent(userID, mailbox);

        Log.log.info("NotificationHub::registerToMailService: user "
                + user.getId() + " is registered to notification service");
        return mailbox;
    }

    public StoreMailboxTestVersion registerToMailService(Store store) throws Exception {
        int storeID = store.getStoreID();
        if (isRegistered(storeID)) {
            Log.log.warning("NotificationHub::registerToMailService: the store " + storeID + " is already registered!");
            throw new Exception("NotificationHub::registerToMailService: the store " + storeID + " is already registered!");
        }
        StoreMailboxTestVersion mailbox = new StoreMailboxTestVersion(store);
        mailboxes.putIfAbsent(storeID, mailbox);

        Log.log.info("NotificationHub::registerToMailService: store " + store.getStoreID()
                + " is registered to notification service");
        return mailbox;
    }

    public void removeFromService(int ID) throws Exception {
        if (!isRegistered(ID)) {
            Log.log.warning("NotificationHub::removeFromService: the given ID " + ID + " is not of a registered user or store!");
            throw new Exception("NotificationHub::removeFromService: the given ID " + ID + " is not of a registered user or store!");
        }

        mailboxes.remove(ID);
    }

    public boolean isRegistered(int ID) {
        return mailboxes.containsKey(ID);
    }

    public void passMessage(Message message) throws Exception {
        validatePassedMessage(message);

        MailBoxTestVersion mailbox = mailboxes.get(message.getReceiverID());
        mailbox.receiveMessage(message);

        Log.log.info("A message passed from " + message.getSenderID() + " to " + message.getReceiverID());
    }

    private void validatePassedMessage(Message message) throws Exception {
        if (message == null) {
            throw new Exception("NotificationHub::passMessage: given message is null");
        }

        if (!isRegistered(message.getSenderID())) {
            throw new Exception("NotificationHub::passMessage: the sender is not registered!");
        }

        if (!isRegistered(message.getReceiverID())) {
            throw new Exception("NotificationHub::passMessage: the receiver is not registered!");
        }

        if (message.getTitle() == null || message.getTitle().isBlank()) {
            throw new Exception("NotificationHub::passMessage: the title of the message is invalid!");
        }

        if (message.getContent() == null || message.getContent().isBlank()) {
            throw new Exception("NotificationHub::passMessage: the content of the message is invalid!");
        }
    }

}
