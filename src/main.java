import BusinessLayer.Market;
import CLI.MainMenu;

public class main {

    public static void main(String[] args) throws Exception {
        new MainMenu(Market.getInstance()).run();//main
    }

}
