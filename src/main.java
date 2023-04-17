import BusinessLayer.Market;
import CLI.MainMenu;

public class main {

    public static void main(String[] args) {
        new MainMenu(Market.getInstance()).run();//main
    }

}
