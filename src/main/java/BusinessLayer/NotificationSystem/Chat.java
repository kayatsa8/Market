package BusinessLayer.NotificationSystem;

import BusinessLayer.NotificationSystem.Repositories.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Chat {
    private final int mySideId;
    private final int otherSideId;
    private final MessageRepository messages;

    public Chat(int _mySideId, int _otherSideId){
        mySideId = _mySideId;
        otherSideId = _otherSideId;
        messages = new MessageRepository();
    }

    public int getMySideId(){
        return mySideId;
    }

    public int getOtherSideId(){
        return otherSideId;
    }

    public List<Message> getMessages(){
        return new ArrayList<>(messages.getMessages());
    }

    public void addMessage(Message message){
        messages.add(message);
    }
}
