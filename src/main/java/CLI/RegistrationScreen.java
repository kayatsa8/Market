package CLI;

import BusinessLayer.Market;

import java.time.LocalDate;

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
        String address =getAddress();
        LocalDate localDate=getBDay();
        try {
            this.market.register(username, pass,address,localDate);
            System.out.println("Registration Successful. You may now Log in");
            endRun();
        } catch (Exception e) {
            System.out.println("Problem Registering. Retry please");
            new RegistrationScreen(this.caller).run();
        }
    }

    private LocalDate getBDay() {
        return LocalDate.of(2022,2,2);
    }

    private String getAddress() {
        return getString();
    }

    private String getPass() {
        return getString();
    }

    private String getUserName() {
        return getString();
    }

}
