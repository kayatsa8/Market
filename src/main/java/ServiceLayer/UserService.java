package ServiceLayer;

import BusinessLayer.Log;
import BusinessLayer.Market;
import BusinessLayer.NotificationSystem.Message;
import BusinessLayer.Users.RegisteredUser;
import ServiceLayer.Objects.MessageService;
import ServiceLayer.Objects.UserInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserService {
    private static final Logger log = Log.log;
    private final Market market;

    public UserService() throws Exception {
        market = Market.getInstance();
    }

    public void start() {
        log.info("Starting System");
        market.systemStart();
    }

    public Result<Integer> login(String userName, String pass) {
        try {
            int id=market.login(userName,pass);
            log.info("logIn succeeded");
            return new Result<>(false, id);//login == true
        } catch (Exception e) {
            log.info("logIn failed");
            return new Result<>(true, e.getMessage());//login==false
        }
    }

    public Result<Integer> register(String userName, String pass) {
        try {
            int id=market.register(userName, pass);
            log.info("logIn succeeded");
            return new Result<>(false, id);//login == true,isErr==false
        } catch (Exception e) {
            log.info("logIn failed");
            return new Result<>(true, e.getMessage());//login==false
        }
    }

    public Result<Boolean> logout(int userID) {
        try {
            market.logout(userID);
            log.info("Logout succeeded");
            return new Result<>(false, true);//login == true,isErr==false
        } catch (Exception e) {
            log.info("Logout failed");
            return new Result<>(true, e.getMessage());//login==false
        }
    }

    public Result<Boolean> addOwner(int userID, int userToAddID, int storeID) {
        try {
            market.addOwner(userID, userToAddID, storeID);
            log.info("Added user to list of store owners");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to add user to store owners");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> addManager(int userID, int userToAdd, int storeID) {
        try {
            market.addManager(userID, userToAdd, storeID);
            log.info("Added user to list of store managers");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to add user to store managers");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> removeOwner(int userID, int userToRemove, int storeID) {
        try {
            market.removeOwner(userID, userToRemove, storeID);
            log.info("removed owner and subsequent owners/managers");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to remove owner or subsequent owners/managers");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> removeManager(int userID, int userToRemove, int storeID) {
        try {
            market.removeManager(userID, userToRemove, storeID);
            log.info("removed manager");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to remove store manager");
            return new Result<>(true, e.getMessage());
        }
    }

    /*
    here instead of StoreService bc only system admin can do this?
     */
    public Result<Boolean> closeStorePermanently(int userID, int storeID) throws Exception
    {
        try {
            market.closeStorePermanently(userID, storeID);
            log.info("closed store");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to close store");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> sendMessage(int userID, int receiverID, String title, String content) throws Exception
    {
        boolean answer = market.sendMessage(userID, receiverID, title, content);

        if(answer){
            return new Result<Boolean>(false, "Success");
        }
        else{
            return new Result<Boolean>(true, "Failure");
        }
    }

    public Result<Boolean> markMessageAsRead(int userID, MessageService messageService){
        try{
            Message message = new Message(messageService);

            market.markMessageAsRead(userID, message);

            return new Result<Boolean>(false, "Success");
        }
        catch(Exception e){
            return new Result<Boolean>(true, e.getMessage());
        }
    }

    public Result<Boolean> markMessageAsNotRead(int userID, MessageService messageService){
        try{
            Message message = new Message(messageService);

            market.markMessageAsNotRead(userID, message);

            return new Result<Boolean>(false, "Success");
        }
        catch(Exception e){
            return new Result<Boolean>(true, e.getMessage());
        }
    }

    public Result<List<MessageService>> watchNotReadMessages(int userID) throws Exception
    {
        List<MessageService> messageServices;
        List<Message> messages = market.watchNotReadMessages(userID);

        if(messages == null){
            return new Result<>(true, "Failure");
        }

        messageServices = messageListToMessageServiceList(messages);

        return new Result<>(false, messageServices);

    }

    private List<MessageService> messageListToMessageServiceList(List<Message> messages){
        List<MessageService> toReturn = new ArrayList<>();

        for(Message message : messages){
            toReturn.add(new MessageService(message));
        }

        return toReturn;
    }

    public Result<List<MessageService>> watchReadMessages(int userID) throws Exception
    {
        List<MessageService> messageServices;
        List<Message> messages = market.watchReadMessages(userID);

        if(messages == null){
            return new Result<>(true, "Failure");
        }

        messageServices = messageListToMessageServiceList(messages);

        return new Result<>(false, messageServices);

    }

    public Result<List<MessageService>> watchSentMessages(int userID) throws Exception
    {
        List<MessageService> messageServices;
        List<Message> messages = market.watchSentMessages(userID);

        if(messages == null){
            return new Result<>(true, "Failure");
        }

        messageServices = messageListToMessageServiceList(messages);

        return new Result<>(false, messageServices);

    }


    public Result<ArrayList<UserInfoService>> getAllRegisteredUsers() {
        try{
            ArrayList<RegisteredUser> users = market.getAllRegisteredUsers();
            ArrayList<UserInfoService> usersService = new ArrayList<>();

            for(RegisteredUser user : users){
                usersService.add(new UserInfoService(user));
            }
            log.info("Users information received successfully");
            return new Result<>(false, usersService);
        }
        catch (Exception e){
            log.info("Users information not received");
            return new Result<>(true, e.getMessage());
        }
    }
}
