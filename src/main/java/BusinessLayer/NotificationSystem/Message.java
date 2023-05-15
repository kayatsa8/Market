package BusinessLayer.NotificationSystem;

import ServiceLayer.Objects.MessageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    /*
        TODO: check how to get the usernames of sender and receiver to show on screen.
        No point to save them in Message since they can be changed.
    */

    private final int senderID;
    private final int receiverID;
    private final String content;
    private final LocalDateTime sendingTime;

    public Message(int _senderID, int _receiverID, String _content){
        senderID = _senderID;
        receiverID = _receiverID;
        content = _content;
        sendingTime = LocalDateTime.now();
    }

    public Message(MessageService message){
        senderID = message.getSenderID();
        receiverID = message.getReceiverID();
        content = message.getContent();
        sendingTime = message.getSendingTime();
    }

    public int getSenderID(){
        return senderID;
    }

    public int getReceiverID(){
        return receiverID;
    }

    public String getContent(){
        return content;
    }

    public String getDateAsString(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"); // NOTICE: the format may cause problems, if so, try dd-MM-yyyy, HH:mm
        return sendingTime.format(format);
    }

    public LocalDateTime getSendingTime(){return sendingTime;}

    @Override
    public boolean equals(Object object){
        if(!(object instanceof Message other)){
            return false;
        }

        return senderID == other.senderID
                && receiverID == other.receiverID
                && content.equals(other.content)
                && sendingTime.equals(other.sendingTime);
    }

}
