package Tests.Bridge;

public class ProxyBridge implements Bridge{

    private Bridge real = null;

    public void setRealBridge(Bridge implementation) {
        if (real == null)
            real = implementation;
    }


    @Override
    public int registerUser(String userName, String password) {
        if(real != null){
            return real.registerUser(userName, password);
        }
        return 1;
    }

    @Override
    public boolean loginUser(int id, String password) {
        if(real != null){
            return real.loginUser(id, password);
        }
        return true;
    }

    @Override
    public void exitSystem(int id) {
        if(real != null){
            real.exitSystem(id);
        }
    }

    @Override
    public void loadSystem(){
        if(real != null){
            real.loadSystem();
        }
    }

}
