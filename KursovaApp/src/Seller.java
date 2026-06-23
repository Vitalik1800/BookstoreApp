public class Seller {

    private int id;
    private final String storeName;
    private final String fullName;
    private final String address;
    private final String phoneNumber;
    private final String shift;
    private final String trackNumber;

    public Seller(String storeName, String fullName, String address, String phoneNumber, String shift, String trackNumber) {
        this.storeName = storeName;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.shift = shift;
        this.trackNumber = trackNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getShift() {
        return shift;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
