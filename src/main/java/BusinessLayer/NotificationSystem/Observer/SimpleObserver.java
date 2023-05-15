package BusinessLayer.NotificationSystem.Observer;

import BusinessLayer.NotificationSystem.Observer.NotificationObserver;

public class SimpleObserver implements NotificationObserver {
    private String givenNotification;

    public SimpleObserver(){
        givenNotification = null;
    }

    @Override
    public void notify(String notification) {
        System.out.println("The notification is: " + notification);
        givenNotification = notification;
    }

    public String getGivenNotification(){
        return givenNotification;
    }
}
