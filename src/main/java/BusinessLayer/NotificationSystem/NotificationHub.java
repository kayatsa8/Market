package BusinessLayer.NotificationSystem;


import BusinessLayer.Log;
import BusinessLayer.NotificationSystem.Repositories.MailboxesRepository;
import BusinessLayer.Stores.Store;
import BusinessLayer.Users.RegisteredUser;


/**
 * In order to register to the notification system:
 * NotificationHub.getInstance().registerToMailService(this);
 */
public class NotificationHub {

    private static final Object instanceLock = new Object();
    // class attributes
    private static NotificationHub instance = null;

    public static boolean testMode = false;

    // fields
    //private final ConcurrentHashMap<Integer, Mailbox> mailboxes; // <ID, Mailbox>
    private final MailboxesRepository mailboxes;


    // object methods
    protected NotificationHub() {
        mailboxes = new MailboxesRepository();
        Log.log.info("The notification hub has started successfully.");
    }

    // class methods
    public static NotificationHub getInstance() {
        synchronized (instanceLock) {
            if(!testMode){
                if (instance == null) {
                    instance = new NotificationHub();
                }
            }
            else{
                return new NotificationHubForTests();
            }
        }
        return instance;
    }

    public MailboxesRepository getMailboxes() {
        return mailboxes;
    }

    public UserMailbox registerToMailService(RegisteredUser user) throws Exception {
        int userID = user.getId();
        if (isRegistered(userID)) {
            Log.log.warning("ERROR: NotificationHub::registerToMailService: the user " + userID + " is already registered!");
            throw new Exception("NotificationHub::registerToMailService: the user " + userID + " is already registered!");
        }
        UserMailbox mailbox = new UserMailbox(user);
        mailboxes.putIfAbsent(userID, mailbox);

        Log.log.info("NotificationHub::registerToMailService: user "
                + user.getId() + " is registered to notification service");
        return mailbox;
    }

    public StoreMailbox registerToMailService(Store store) throws Exception {
        int storeID = store.getStoreID();
        if (isRegistered(storeID)) {
            Log.log.warning("NotificationHub::registerToMailService: the store " + storeID + " is already registered!");
            throw new Exception("NotificationHub::registerToMailService: the store " + storeID + " is already registered!");
        }
        StoreMailbox mailbox = new StoreMailbox(store);
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

        Mailbox mailbox = mailboxes.get(message.getReceiverID());
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
