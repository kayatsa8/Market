package ServiceLayer.Objects;

import BusinessLayer.NotificationSystem.Message;

import java.time.LocalDateTime;

public class MessageService {
    private final int senderID;
    private final int receiverID;
    private final String title;
    private final String content;
    private final LocalDateTime sendingTime;

    public MessageService(Message message){
        senderID = message.getSenderID();
        receiverID = message.getReceiverID();
        title = message.getTitle();
        content = message.getContent();
        sendingTime = message.getSendingTime();
    }

    public int getSenderID() {
        return senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getSendingTime(){
        return sendingTime;
    }
}
