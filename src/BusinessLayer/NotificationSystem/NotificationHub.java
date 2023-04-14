package BusinessLayer.NotificationSystem;

import jdk.jshell.spi.ExecutionControl;

public class NotificationHub {

    private static NotificationHub instance = null;
    private static final Object instanceLock = new Object();

    public static NotificationHub getInstance(){
        synchronized (instanceLock){
            if(instance == null){
                instance = new NotificationHub();
            }
        }
        return instance;
    }

    private NotificationHub() {
        // TODO: implement
    }

    public void passMessage(Message message) throws IllegalArgumentException{
        // TODO: implement
        throw new IllegalArgumentException("NotificationHub::passMessage: Receiver does not exist!");
    }

}
