package ServiceLayer.Objects;

import BusinessLayer.NotificationSystem.Chat;
import BusinessLayer.NotificationSystem.Message;
import BusinessLayer.NotificationSystem.Repositories.MessageRepository;

import java.util.ArrayList;
import java.util.List;

public class ChatService {
    private final int mySideId;
    private final int otherSideId;
    private final List<MessageService> messages;

    public ChatService(Chat chat){
        mySideId = chat.getMySideId();
        otherSideId = chat.getOtherSideId();
        messages = new ArrayList<>();
        copyMessages(chat);
    }

    private void copyMessages(Chat chat){
        for(Message m : chat.getMessages()){
            messages.add(new MessageService(m));
        }
    }
}
