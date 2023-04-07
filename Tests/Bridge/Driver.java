package Tests.Bridge;

public abstract class Driver {

    public static Bridge getBridge() {
        ProxyBridge bridge = new ProxyBridge();

        // Uncomment this line  //TODO
        //bridge.setRealBridge(new RealBridge());

        return bridge;
    }

}
