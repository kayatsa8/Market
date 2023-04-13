package CLI;

import BusinessLayer.Market;

public class LoginScreen extends Screen{

    public LoginScreen(Market market) {
        super(market, new String[]{});
    }

    public LoginScreen(Screen caller) {
        super(caller, new String[]{});
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Login Menu!");
        String username = getUserName();
        String pass = getPass();
        int output = this.market.logIn(username, pass);
        if (output==0) {
            System.out.println("Login Successful");
            endRun();
        } else if (output==-1) {
            System.out.println("Problem logging in. Retry please");
            new LoginScreen(this.caller).run();
        }
    }

    private String getPass() {
        return getString();
    }

    private String getUserName() {
        return getString();
    }

}
