package BusinessLayer.NotificationSystem;

import BusinessLayer.NotificationSystem.Repositories.MailboxesRepository;
import BusinessLayer.Stores.Store;
import BusinessLayer.Users.RegisteredUser;

public class NotificationHubForTests extends NotificationHub {

    public NotificationHubForTests(){

    }

    @Override
    public MailboxesRepository getMailboxes(){
        return null;
    }

    @Override
    public UserMailbox registerToMailService(RegisteredUser user) throws Exception{
        return new UserMailbox(user);
    }

    @Override
    public StoreMailbox registerToMailService(Store store) throws Exception{
        return new StoreMailbox(store);
    }

    @Override
    public void removeFromService(int ID) throws Exception{

    }

    @Override
    public boolean isRegistered(int ID){
        return false;
    }

    @Override
    public void passMessage(Message message) throws Exception{

    }



}
