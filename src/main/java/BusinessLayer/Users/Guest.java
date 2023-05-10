package BusinessLayer.Users;

public class Guest extends User{
    public static final int GUEST_USER_ID = 999999;
    public Guest () {
        super(GUEST_USER_ID);
    }
}
