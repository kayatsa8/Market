package BusinessLayer.NotificationSystem;

import BusinessLayer.Log;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
@MappedSuperclass
public abstract class Mailbox {
    @Id
    protected int ownerID;
    protected boolean available;
    @Transient
    protected List<Chat> chats; // <otherSideId, Chat>

    @Transient
    protected NotificationHub hub;


//    protected NotReadMessagesRepository notReadMessages;
//    protected ReadMessagesRepository readMessages;
//    protected SentMessagesRepository sentMessages;

    public void sendMessage(int receiverID, String content){
        Message message = new Message(ownerID, receiverID, content);

        try{
            Chat chat = chats_searchChat(receiverID);

            if(chat == null){
                chat = new Chat(ownerID, receiverID);
                chats.add(chat);
            }

            chat.addMessage(message);

            hub.passMessage(message);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            // LOG: ERROR: Mailbox::sendMessage:e.getMessage()
            Log.log.severe("ERROR: Mailbox::sendMessage: " + e.getMessage());
            return;
        }

        //sentMessages.add(message);
    }

    public void receiveMessage(Message message) throws Exception{
        if(message == null){
            Log.log.warning("ERROR: Mailbox::receiveMessage: the given message is null");
            throw new Exception("Mailbox::receiveMessage: the given message is null");
        }

        if(ownerID != message.getReceiverID()){
            Log.log.severe("ERROR: Mailbox::receiveMessage: A message for "
                    + message.getReceiverID()
                    + "was sent to " + ownerID);
            throw new Exception("Mailbox::receiveMessage: A message for " + message.getReceiverID() +
                    "was sent to " + ownerID);
        }

        Chat chat = chats_searchChat(message.getSenderID());

        if(chat == null){
            chat = new Chat(ownerID, message.getSenderID());
            chats.add(chat);
        }

        chat.addMessage(message);
        //notReadMessages.add(message);
        notifyOwner();
    }

    /**
     * notify the owner of the mailbox about a new message,
     * using observer pattern
     */
    abstract public void notifyOwner() throws Exception;

//    public void markMessageAsRead(Message message) throws Exception {
//
//        if(message == null || !notReadMessages.contains(message)){
//            Log.log.warning("ERROR: Mailbox::markMessageAsRead: given message is invalid");
//            throw new Exception("Mailbox::markMessageAsRead: given message is invalid");
//        }
//
//        notReadMessages.remove(message);
//        readMessages.add(message);
//
//    }
//
//    public void markMessageAsNotRead(Message message) throws Exception {
//
//        if(message == null || !readMessages.contains(message)){
//            Log.log.warning("ERROR: Mailbox::markMessageAsNotRead: given message is invalid");
//            throw new Exception("Mailbox::markMessageAsNotRead: given message is invalid");
//        }
//
//        readMessages.remove(message);
//        notReadMessages.add(message);
//
//    }
//
//    public List<Message> watchNotReadMessages(){
//        return new ArrayList<>(notReadMessages.getMessages());
//    }
//
//    public List<Message> watchReadMessages(){
//        return new ArrayList<>(readMessages.getMessages());
//    }
//
//    public List<Message> watchSentMessages(){
//        return new ArrayList<>(sentMessages.getMessages());
//    }

    public ConcurrentHashMap<Integer, Chat> getChatsAsMap(){
        ConcurrentHashMap<Integer, Chat> _chats = new ConcurrentHashMap<>();
        for(Chat chat : chats){
            _chats.putIfAbsent(chat.getOtherSideId(), chat);
        }

        return _chats;
    }

    public void setMailboxAsUnavailable(){
        available = false;
        Log.log.info("The mailbox of " + ownerID + " was marked as unavailable.");
    }

    public void setMailboxAsAvailable(){
        available = true;
        Log.log.info("The mailbox of " + ownerID + " was marked as available.");
    }

    public boolean isAvailable(){
        return available;
    }

    protected Chat chats_searchChat(int receiverId){
        for(Chat chat : chats){
            if(chat.getOtherSideId() == receiverId){
                return chat;
            }
        }

        return null;
    }

}
