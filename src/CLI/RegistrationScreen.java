package CLI;

import BusinessLayer.Market;

public class RegistrationScreen extends Screen{

    public RegistrationScreen(Market market) {
        super(market, new String[]{});
    }

    public RegistrationScreen(Screen caller) {
        super(caller, new String[]{});
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Registration Menu!");
        String username = getUserName();
        String pass = getPass();
        try {
            this.market.register(username, pass);
            System.out.println("Registration Successful. You may now Log in");
            endRun();
        } catch (Exception e) {
            System.out.println("Problem Registering. Retry please");
            new RegistrationScreen(this.caller).run();
        }
    }

    private String getPass() {
        return getString();
    }

    private String getUserName() {
        return getString();
    }

}
