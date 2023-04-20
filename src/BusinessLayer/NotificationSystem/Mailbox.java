package BusinessLayer.NotificationSystem;

import BusinessLayer.NotificationSystem.Repositories.NotReadMessagesRepository;
import BusinessLayer.NotificationSystem.Repositories.ReadMessagesRepository;
import BusinessLayer.NotificationSystem.Repositories.SentMessagesRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class Mailbox {
    protected int ownerID;
    protected NotReadMessagesRepository notReadMessages;
    protected ReadMessagesRepository readMessages;
    protected SentMessagesRepository sentMessages;

    public void sendMessage(int receiverID, String title, String content){
        Message message = new Message(ownerID, receiverID, title, content);

        try{
            NotificationHub.getInstance().passMessage(message);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            // LOG: ERROR: Mailbox::sendMessage:e.getMessage()
            return;
        }

        sentMessages.add(message);
    }

    public void receiveMessage(Message message) throws Exception{
        if(message == null){
            // LOG: ERROR: Mailbox::receiveMessage: the given message is null
            throw new Exception("Mailbox::receiveMessage: the given message is null");
        }

        if(ownerID != message.getReceiverID()){
            // LOG: ERROR: Mailbox::receiveMessage: A message for " + message.getReceiverID() +
            //                    "was sent to " + ownerID
            throw new Exception("Mailbox::receiveMessage: A message for " + message.getReceiverID() +
                    "was sent to " + ownerID);
        }

        notReadMessages.add(message);
        notifyOwner();
    }

    /**
     * notify the owner of the mailbox about a new message,
     * using observer pattern
     */
    abstract protected void notifyOwner() throws Exception;

    public void markMessageAsRead(Message message) throws Exception {

        if(message == null || !notReadMessages.contains(message)){
            // LOG: ERROR: Mailbox::markMessageAsRead: given message is invalid
            throw new Exception("Mailbox::markMessageAsRead: given message is invalid");
        }

        notReadMessages.remove(message);
        readMessages.add(message);

    }

    public void markMessageAsNotRead(Message message) throws Exception {

        if(message == null || !readMessages.contains(message)){
            // LOG: ERROR: Mailbox::markMessageAsNotRead: given message is invalid
            throw new Exception("Mailbox::markMessageAsNotRead: given message is invalid");
        }

        readMessages.remove(message);
        notReadMessages.add(message);

    }

    public List<Message> watchNotReadMessages(){
        return new ArrayList<>(notReadMessages.getMessages());
    }

    public List<Message> watchReadMessages(){
        return new ArrayList<>(readMessages.getMessages());
    }

    public List<Message> watchSentMessages(){
        return new ArrayList<>(sentMessages.getMessages());
    }

}
