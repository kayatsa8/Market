package BusinessLayer.NotificationSystem.Observer;

import BusinessLayer.NotificationSystem.Observer.NotificationObserver;

public class SimpleObserver implements NotificationObserver {
    @Override
    public void notify(String notification) {
        System.out.println("The notification is:" + notification);
    }
}
