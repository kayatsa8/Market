package BusinessLayer.NotificationSystem;

import java.util.List;

public abstract class Mailbox {
    protected int ownerID;
    protected List<Message> notReadMessages;
    protected List<Message> readMessages;
    protected List<Message> sentMessages;

    public void sendMessage(int receiverID, String title, String content){
        Message message = new Message(ownerID, receiverID, title, content);

        try{
            NotificationHub.getInstance().passMessage(message);
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        sentMessages.add(message);
    }

    public void receiveMessage(Message message) throws Exception{
        if(message == null){
            throw new Exception("Mailbox::receiveMessage: the given message is null");
        }

        if(ownerID != message.getReceiverID()){
            throw new Exception("Mailbox::receiveMessage: A message for " + message.getReceiverID() +
                    "was sent to " + ownerID);
        }

        notReadMessages.add(message);
    }

    /**
     * notify the owner of the mailbox about a new message,
     * using observer pattern
     */
    abstract protected void notifyOwner();

    public void markMessageAsRead(Message message) throws Exception {

        if(message == null || !notReadMessages.contains(message)){
            throw new Exception("Mailbox::markMessageAsRead: given message is invalid");
        }

        notReadMessages.remove(message);
        readMessages.add(message);

    }

    public void markMessageAsNotRead(Message message) throws Exception {

        if(message == null || !readMessages.contains(message)){
            throw new Exception("Mailbox::markMessageAsNotRead: given message is invalid");
        }

        readMessages.remove(message);
        notReadMessages.add(message);

    }

}
