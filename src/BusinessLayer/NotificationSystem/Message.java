package BusinessLayer.NotificationSystem;

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

}
