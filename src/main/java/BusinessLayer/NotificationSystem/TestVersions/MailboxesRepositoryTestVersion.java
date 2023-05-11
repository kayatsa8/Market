package BusinessLayer.NotificationSystem.TestVersions;

import java.util.concurrent.ConcurrentHashMap;

public class MailboxesRepositoryTestVersion {
    private final ConcurrentHashMap<Integer, MailBoxTestVersion> mailboxes; // <ID, Mailbox>

    public MailboxesRepositoryTestVersion(){
        mailboxes = new ConcurrentHashMap<>();
    }

    public void putIfAbsent(int userID, MailBoxTestVersion mailbox){
        mailboxes.putIfAbsent(userID, mailbox);
    }

    public void remove(int ID){
        mailboxes.remove(ID);
    }

    public boolean containsKey(int key){
        return mailboxes.containsKey(key);
    }

    public MailBoxTestVersion get(int userID){
        return mailboxes.get(userID);
    }

}
