package BusinessLayer.ExternalSystems;

import BusinessLayer.ExternalSystems.Purchase.PurchaseClient;
import BusinessLayer.ExternalSystems.Supply.SupplyClient;

public class ESPurchaseManager {

    private PurchaseClient purchaseClient;
    private SupplyClient supplyClient;
    private PurchaseInfo purchaseInfo;
    private SupplyInfo supplyInfo;
    public ESPurchaseManager(PurchaseInfo purchaseInfo, SupplyInfo supplyInfo) {
        purchaseClient = new PurchaseClient();
        supplyClient = new SupplyClient();
        this.purchaseInfo = purchaseInfo;
        this.supplyInfo = supplyInfo;
    }

    public boolean handShake() throws Exception {
        return purchaseClient.handShake();
    }

    public int pay() throws Exception {
        return purchaseClient.pay(purchaseInfo.getCardNumber(), purchaseInfo.getMonth(), purchaseInfo.getYear(),
                purchaseInfo.getHolderName(), purchaseInfo.getCcv(), purchaseInfo.getBuyerId());
    }

    public void chooseSupplyService() {
        supplyClient.chooseService();
    }

    public int supply() throws Exception {
        return supplyClient.supply(supplyInfo.getName(), supplyInfo.getAddress(), supplyInfo.getCity(), supplyInfo.getCountry(), supplyInfo.getZip());
    }

    public void cancelSupply(int supplyTransId) throws Exception {
        supplyClient.cancelSupply(supplyTransId);
    }

    public void cancelPay(int purchaseTransId) throws Exception {
        purchaseClient.cancelPay(purchaseTransId);
    }
}
