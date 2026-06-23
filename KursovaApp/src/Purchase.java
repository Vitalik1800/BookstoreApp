public class Purchase {

    private int purchaseId;
    private final String bookTitle;
    private final int quantity;
    private final double cost;
    private final String genre;

    public Purchase(String bookTitle, int quantity, double cost, String genre) {
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.cost = cost;
        this.genre = genre;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getCost() {
        return cost;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "purchaseId=" + purchaseId +
                ", bookTitle='" + bookTitle + '\'' +
                ", quantity=" + quantity +
                ", cost=" + cost +
                ", genre='" + genre + '\'' +
                '}';
    }

}
