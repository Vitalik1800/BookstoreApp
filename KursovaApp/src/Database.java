import java.sql.*;

public class Database {

    private static final String URL = "jdbc:mysql://localhost:3306/bookstore";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public Connection connect() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void addSeller(Seller seller) throws SQLException {
        String query = "INSERT INTO sellers (store_name, full_name, address, phone, shift, track_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, seller.getStoreName());
            stmt.setString(2, seller.getFullName());
            stmt.setString(3, seller.getAddress());
            stmt.setString(4, seller.getPhoneNumber());
            stmt.setString(5, seller.getShift());
            stmt.setString(6, seller.getTrackNumber());
            stmt.execute();
        }
    }

    public ResultSet getSellers() throws SQLException {
        String query = "SELECT * FROM sellers";
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    public void updateSeller(Seller seller) throws SQLException {
        String sql = "UPDATE sellers SET store_name = ?, full_name = ?, address = ?, phone = ?, shift = ?, track_number = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, seller.getStoreName());
            stmt.setString(2, seller.getFullName());
            stmt.setString(3, seller.getAddress());
            stmt.setString(4, seller.getPhoneNumber());
            stmt.setString(5, seller.getShift());
            stmt.setString(6, seller.getTrackNumber());
            stmt.setInt(7, seller.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Seller updated successfully.");
            } else {
                System.out.println("No seller found with the given store name.");
            }
        }
    }

    public void deleteSeller(int sellerId) throws SQLException {
        String query = "DELETE FROM sellers WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sellerId);
            stmt.executeUpdate();
        }
    }

    public void addPurchase(Purchase purchase) throws SQLException {
        String query = "INSERT INTO purchase (bookTitle, quantity, cost, genre) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, purchase.getBookTitle());
            stmt.setInt(2, purchase.getQuantity());
            stmt.setDouble(3, purchase.getCost());
            stmt.setString(4, purchase.getGenre());
            stmt.execute();
        }
    }

    public ResultSet getPurchases() throws SQLException {
        String query = "SELECT * FROM purchase";
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    public void updatePurchase(Purchase purchase) throws SQLException {
        String sql = "UPDATE purchase SET bookTitle = ?, quantity = ?, cost = ?, genre = ? WHERE purchaseId = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, purchase.getBookTitle());
            stmt.setInt(2, purchase.getQuantity());
            stmt.setDouble(3, purchase.getCost());
            stmt.setString(4, purchase.getGenre());
            stmt.setInt(5, purchase.getPurchaseId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Purchase updated successfully.");
            } else {
                System.out.println("No purchase found with the given book title.");
            }
        }
    }

    public void deletePurchase(int purchaseId) throws SQLException {
        String query = "DELETE FROM purchase WHERE purchaseId = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, purchaseId);
            stmt.executeUpdate();
        }
    }

    public void addBuyer(Buyer buyer) throws SQLException{
        String query = "INSERT INTO buyer (storeName, fullName, phoneNumber, purchaseId) VALUES (?, ?, ?, ?)";

        try(Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, buyer.getStoreName());
            stmt.setString(2, buyer.getFullName());
            stmt.setString(3, buyer.getPhoneNumber());
            stmt.setInt(4, buyer.getPurchaseId());
            stmt.execute();
        }
    }

    public ResultSet getBookTitles() throws SQLException {
        String query = "SELECT bookTitle FROM purchase"; // Замість purchases вкажіть вашу таблицю
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    public ResultSet getBuyers() throws SQLException {
        String query = "SELECT * FROM buyer";
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    public void updateBuyer(Buyer buyer) throws SQLException {
        String sql = "UPDATE buyer SET storeName = ?, fullName = ?, phoneNumber = ?, purchaseId = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, buyer.getStoreName());
            stmt.setString(2, buyer.getFullName());
            stmt.setString(3, buyer.getPhoneNumber());
            stmt.setInt(4, buyer.getPurchaseId());
            stmt.setInt(5, buyer.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Purchase updated successfully.");
            } else {
                System.out.println("No purchase found with the given book title.");
            }
        }
    }

    public String getBookTitleByPurchaseId(int purchaseId) throws SQLException {
        String bookTitle = null;

        String query = "SELECT bookTitle FROM purchase WHERE purchaseId = ?";
        try (PreparedStatement preparedStatement = connect().prepareStatement(query)) {
            preparedStatement.setInt(1, purchaseId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                bookTitle = resultSet.getString("bookTitle");
            }
        } catch (SQLException ex) {
            throw new SQLException("Error retrieving book title for purchaseId: " + purchaseId, ex);
        }
        return bookTitle;
    }


    public void deleteBuyer(int buyerId) throws SQLException {
        String query = "DELETE FROM buyer WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, buyerId);
            stmt.executeUpdate();
        }
    }

    public int getNumberOfSellers() throws SQLException {
        String query = "SELECT COUNT(*) FROM sellers";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getNumberOfBuyers() throws SQLException {
        String query = "SELECT COUNT(*) FROM buyer";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public double getTotalSalesAmount() throws SQLException {
        String query = "SELECT SUM(cost * quantity) FROM purchase";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    public int getBooksSoldByGenre(String genre) throws SQLException {
        String query = "SELECT SUM(quantity) FROM purchase WHERE genre = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, genre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public String getMostPopularBook() throws SQLException {
        String query = "SELECT bookTitle, SUM(quantity) AS totalSold FROM purchase GROUP BY bookTitle ORDER BY totalSold DESC LIMIT 1";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("bookTitle");
            }
        }
        return null;
    }

    public String getBiggestSpender() throws SQLException {
        String query = "SELECT buyer.fullName, SUM(purchase.cost * purchase.quantity) AS totalSpent " +
                "FROM buyer JOIN purchase ON buyer.purchaseId = purchase.purchaseId " +
                "GROUP BY buyer.id ORDER BY totalSpent DESC LIMIT 1";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("fullName");
            }
        }
        return null;
    }


}
