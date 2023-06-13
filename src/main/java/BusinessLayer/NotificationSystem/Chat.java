package BusinessLayer.NotificationSystem;

import BusinessLayer.NotificationSystem.Repositories.MessageRepository;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Chat {
    private int mySideId;
    private int otherSideId;
    private ConcurrentLinkedDeque<Message> messages;

    public Chat(int _mySideId, int _otherSideId){
        mySideId = _mySideId;
        otherSideId = _otherSideId;
        messages = new ConcurrentLinkedDeque<>();
    }

    public int getMySideId(){
        return mySideId;
    }

    public int getOtherSideId(){
        return otherSideId;
    }

    public List<Message> getMessages(){
        return new ArrayList<>(messages);
    }

    public void addMessage(Message message){
        messages.add(message);
    }

    public boolean contains(Message message){
        return messages.contains(message);
    }
}
