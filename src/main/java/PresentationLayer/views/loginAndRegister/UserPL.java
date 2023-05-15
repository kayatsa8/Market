package PresentationLayer.views.loginAndRegister;

import PresentationLayer.views.MainLayout;

public final class UserPL {
    private static volatile UserPL instance = null;



    private int currUserID;
    private final int GUEST_ID =9999;
    private UserPL() {
        // private constructor
        currUserID= GUEST_ID;

    }

    public static UserPL getInstance() {
        if (instance == null) {
            synchronized (UserPL.class) {
                if (instance == null) {
                    instance = new UserPL();
                    MainLayout.setGuestView();
                }
            }
        }
        return instance;
    }
    public int getCurrUserID() {
        return currUserID;
    }

    public void setCurrUserID(int currUserID) {
        this.currUserID = currUserID;
    }
    public void setCurrIdToGuest() {
        this.currUserID = GUEST_ID;
    }
}