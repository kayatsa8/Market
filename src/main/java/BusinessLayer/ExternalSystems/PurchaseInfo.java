package BusinessLayer.ExternalSystems;

public class PurchaseInfo {

    private String cardNumber;
    private int month;
    private int year;
    private String holderName;
    private int ccv;
    private int buyerId;

    public PurchaseInfo(String cardNumber, int month, int year, String holderName, int ccv, int buyerId){
        this.ccv = ccv;
        this.holderName = holderName;
        this.buyerId = buyerId;
        this.cardNumber = cardNumber;
        this.month = month;
        this.year = year;

    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getHolderName() {
        return holderName;
    }

    public int getCcv() {
        return ccv;
    }

    public int getBuyerId() {
        return buyerId;
    }
}
