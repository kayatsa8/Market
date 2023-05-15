package BusinessLayer.NotificationSystem.Observer;

import BusinessLayer.NotificationSystem.UserMailbox;

public interface NotificationObserver {

    void notify(String notification);

    void listenToNotifications(int userId) throws Exception;

}
