package Tests.Bridge;


public class RealBridge implements Bridge{

    //public Facade facade;

    public RealBridge(){
        //facade = new Facade;
    }


    @Override
    public int registerUser(String userName, String password) {
        //TODO real
        //facade.registerUser(userName, password);
        return 1;
    }


    @Override
    public boolean loginUser(int id, String password) {
        //TODO real
        //facade.loginUser(id, password);
        return false;
    }

    @Override
    public void exitSystem(int id) {
        //TODO real
        //facade.exitSystem(id);
    }

    public void loadSystem(){
        //TODO real
        //facade.loadSystem();

    }

}
