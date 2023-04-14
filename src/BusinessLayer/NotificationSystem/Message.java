package BusinessLayer.NotificationSystem;

public class Message {

    private final int senderID;
    private final int receiverID;
    private final String title;
    private final String content;

    public Message(int _senderID, int _receiverID, String _title, String _content){
        senderID = _senderID;
        receiverID = _receiverID;
        title = _title;
        content = _content;
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

}
