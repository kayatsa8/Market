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
        try {
            this.market.logIn(username, pass);
            System.out.println("Login Successful");
            endRun();
        }catch (Exception e)  {
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
