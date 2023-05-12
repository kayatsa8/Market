package PresentationLayer.views.loginAndRegister;

public final class UserPL {
    private static volatile UserPL instance = null;



    private int currUserID;
    private final int GUESS_ID=9999;
    private UserPL() {
        // private constructor
        currUserID=GUESS_ID;

    }

    public static UserPL getInstance() {
        if (instance == null) {
            synchronized (UserPL.class) {
                if (instance == null) {
                    instance = new UserPL();
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
    public void setCurrIdToGuess() {
        this.currUserID =GUESS_ID;
    }
}