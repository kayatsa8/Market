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
    private final String title;
    private final String content;
    private final LocalDateTime sendingTime;

    public Message(int _senderID, int _receiverID, String _title, String _content){
        senderID = _senderID;
        receiverID = _receiverID;
        title = _title;
        content = _content;
        sendingTime = LocalDateTime.now();
    }

    public Message(MessageService message){
        senderID = message.getSenderID();
        receiverID = message.getReceiverID();
        title = message.getTitle();
        content = message.getContent();
        sendingTime = message.getSendingTime();
    }

    public int getSenderID(){
        return senderID;
    }

    public int getReceiverID(){
        return receiverID;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public String getDate(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"); // NOTICE: the format may cause problems, if so, try dd-MM-yyyy, HH:mm
        return sendingTime.format(format);
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof Message other)){
            return false;
        }

        return senderID == other.senderID
                && receiverID == other.receiverID
                && title.equals(other.title)
                && content.equals(other.content)
                && sendingTime.equals(other.sendingTime);
    }

    public LocalDateTime getSendingTime(){return sendingTime;}

}
