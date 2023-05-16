package BusinessLayer.Users;

public class Guest extends User{
    public static final int MAX_GUEST_USER_ID = 999999;
    public static int GUEST_USER_ID = 999999;
    public Guest () {
        super(GUEST_USER_ID);
    }
}
