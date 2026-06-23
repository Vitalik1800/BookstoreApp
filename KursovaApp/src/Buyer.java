public class Buyer {

    private int id;
    private final String storeName;
    private final String fullName;
    private final String phoneNumber;
    private final int purchaseId;

    public Buyer(String storeName, String fullName, String phoneNumber, int purchaseId) {
        this.storeName = storeName;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.purchaseId = purchaseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    @Override
    public String toString() {
        return "Buyer{" +
                "id=" + id +
                ", storeName='" + storeName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", purchaseId=" + purchaseId +
                '}';
    }

}
