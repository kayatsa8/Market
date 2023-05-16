package PresentationLayer.views.loginAndRegister;

import PresentationLayer.views.MainLayout;
import com.vaadin.flow.component.UI;

public final class UserPL {
    private static volatile UserPL instance = null;



    private int currUserID;
    private final int GUEST_ID =999999;
    public UserPL() {
        // private constructor
        currUserID= GUEST_ID;

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