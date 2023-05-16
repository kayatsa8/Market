package PresentationLayer.views.loginAndRegister;

import BusinessLayer.Users.Guest;
import PresentationLayer.views.MainLayout;
import com.vaadin.flow.component.UI;

public final class UserPL {
    private static volatile UserPL instance = null;



    private int currUserID;
    private int GUEST_ID;
    public UserPL() {
        GUEST_ID = Guest.GUEST_USER_ID+1;
        currUserID = Guest.GUEST_USER_ID+1;

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