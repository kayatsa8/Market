package CLI;

import BusinessLayer.Market;

public class MainMenu extends Screen{

    private static final String[] menuOptions = {
            "Register",     //1
            "Log in",     //2
    };

    public MainMenu(Market market) {
        super(market, menuOptions);
    }

    public MainMenu(Screen caller) {
        super(caller, menuOptions);
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Main Menu!");
        switch (runMenu()) {
            case 1:
                new RegistrationScreen(this).run();
                break;
            case 2:
                new LoginScreen(this).run();
                break;
//            case 1:
//                new Thread(new EmployeesMenu(this)).start();
//                break;
//            case 2:
//                new Thread(new ShiftsMenu(this)).start();
//                break;
//            case 3:
//                new Thread(new TruckMenu(this)).start();
//                break;
//            case 4:
//                new Thread(new TransportsMenu(this)).start();
//                break;
//            case 5:
//                new Thread(new DocumentMenu(this)).start();
//                break;
//            case 6:
//                endRun();
        }
    }
}
