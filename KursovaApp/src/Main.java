import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        // Create the main window
        JFrame frame = new JFrame("Книгарня");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        // Create the main menu
        JMenu menu = new JMenu("Меню");
        menuBar.add(menu);
        // Create the menu items for buyers, sellers, and purchases
        JMenuItem itemBuyers = new JMenuItem("Покупці");
        JMenuItem itemSellers = new JMenuItem("Продавці");
        JMenuItem itemPurchases = new JMenuItem("Покупки");
        JMenuItem itemQueryResults = new JMenuItem("Результати запитів");
        // Add the menu items to the menu
        menu.add(itemBuyers);
        menu.add(itemSellers);
        menu.add(itemPurchases);
        menu.add(itemQueryResults);
        // Set the menu bar for the frame
        frame.setJMenuBar(menuBar);
        // Panel for visual elements
        JPanel panel = new JPanel();
        frame.add(panel);
        // Action listener for "Покупці" menu item
        itemBuyers.addActionListener(_ -> {
            // Open a new window to display the sellers
            JFrame buyersWindow = new JFrame("Список покупців");
            buyersWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            buyersWindow.setSize(800, 400);
            // Create a table to display the seller data
            String[] columns = {"Номер", "Назва магазину", "ПІБ покупця", "Номер телефону", "Покупка №"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable buyerTable = new JTable(model);
            buyerTable.setCellSelectionEnabled(true); // Enable cell editing
            JScrollPane scrollPane = new JScrollPane(buyerTable);
            buyersWindow.add(scrollPane, BorderLayout.CENTER);
            // Create the menu for adding and editing sellers
            JMenuBar buyerMenuBar = new JMenuBar();
            JMenu buyerMenu = new JMenu("Опції");
            JMenuItem addBuyerItem = new JMenuItem("Додати покупця");
            JMenuItem editBuyerItem = new JMenuItem("Редагувати покупця");
            JMenuItem deleteBuyerItem = new JMenuItem("Видалити покупця");
            buyerMenu.add(addBuyerItem);
            buyerMenu.add(editBuyerItem);
            buyerMenu.add(deleteBuyerItem);
            buyerMenuBar.add(buyerMenu);
            buyersWindow.setJMenuBar(buyerMenuBar);

            addBuyerItem.addActionListener(_ -> {
                // Create a panel for input fields
                JTextField storeNameField = new JTextField();
                JTextField fullNameField = new JTextField();
                JTextField phoneField = new JTextField();
                // Create the combo box for bookTitle
                JComboBox<String> bookTitleComboBox = new JComboBox<>();
                // Populate bookTitleComboBox with values from the database
                Database database = new Database();
                try {
                    ResultSet resultSet = database.getBookTitles(); // Get available book titles from the database
                    while (resultSet.next()) {
                        String bookTitle = resultSet.getString("bookTitle"); // Assuming bookTitle is a string
                        bookTitleComboBox.addItem(bookTitle);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(buyersWindow, "Помилка при завантаженні назв книг: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                }
                // Create the panel layout with input fields and the combo box
                JPanel buyerPanel = new JPanel(new GridLayout(4, 2));
                buyerPanel.add(new JLabel("Назва магазину:"));
                buyerPanel.add(storeNameField);
                buyerPanel.add(new JLabel("ПІБ покупця:"));
                buyerPanel.add(fullNameField);
                buyerPanel.add(new JLabel("Номер телефону:"));
                buyerPanel.add(phoneField);
                buyerPanel.add(new JLabel("Назва книги:"));
                buyerPanel.add(bookTitleComboBox);

                int option = JOptionPane.showConfirmDialog(buyersWindow, buyerPanel, "Введіть дані покупця", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String storeName = storeNameField.getText();
                    String fullName = fullNameField.getText();
                    String phone = phoneField.getText();
                    String selectedBookTitle = (String) bookTitleComboBox.getSelectedItem(); // Get the selected book title
                    // Get the bookId from the database based on the selected bookTitle
                    int bookId = -1;
                    try {
                        String checkBookQuery = "SELECT purchaseId FROM purchase WHERE bookTitle = ?";
                        PreparedStatement preparedStatement = database.connect().prepareStatement(checkBookQuery);
                        preparedStatement.setString(1, selectedBookTitle);
                        ResultSet rs = preparedStatement.executeQuery();
                        if (rs.next()) {
                            bookId = rs.getInt("purchaseId"); // Retrieve the bookId from the database
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(buyersWindow, "Помилка при перевірці книги: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                    }

                    if (bookId != -1) {
                        // If bookId was found, proceed with adding the buyer
                        Buyer buyer = new Buyer(storeName, fullName, phone, bookId); // Pass the bookId as an int

                        try {
                            database.addBuyer(buyer); // Call the method to add the buyer
                            JOptionPane.showMessageDialog(buyersWindow, "Покупець успішно доданий!");
                            // Refresh the table with the updated data
                            model.setRowCount(0); // Clear existing rows
                            ResultSet resultSet = database.getBuyers(); // Assuming there's a method to get all buyers
                            while (resultSet.next()) {
                                int id = resultSet.getInt("id");
                                String storeNameFromDb = resultSet.getString("storeName");
                                String fullNameFromDb = resultSet.getString("fullName");
                                String phoneFromDb = resultSet.getString("phoneNumber");
                                int bookIdFromDb = resultSet.getInt("purchaseId");
                                model.addRow(new Object[]{id, storeNameFromDb, fullNameFromDb, phoneFromDb, bookIdFromDb});
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(buyersWindow, "Помилка при додаванні покупця: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Show a message if the bookId was not found in the database
                        JOptionPane.showMessageDialog(buyersWindow, "Вибрана книга не існує в базі даних.", "Помилка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            // Action listener for "Додати продавця" menu item
            editBuyerItem.addActionListener(_ -> {
                int selectedRow = buyerTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(buyersWindow, "Будь ласка, виберіть покупця для редагування.");
                    return;
                }
                // Get current data of the selected buyer
                String storeName = (String) model.getValueAt(selectedRow, 1); // storeName
                String fullName = (String) model.getValueAt(selectedRow, 2); // fullName
                String phoneNumber = (String) model.getValueAt(selectedRow, 3); // phoneNumber
                int purchaseIdStr = (int) model.getValueAt(selectedRow, 4); // purchaseId (as String)

                int purchaseId;
                try {
                    // Ensure purchaseId is parsed correctly
                    purchaseId = purchaseIdStr;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(buyersWindow, "Невірний формат ID покупки: " + purchaseIdStr, "Помилка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Create input fields
                JTextField storeNameField = new JTextField(storeName);
                JTextField fullNameField = new JTextField(fullName);
                JTextField phoneNumberField = new JTextField(phoneNumber);
                // Create the combo box for bookTitle
                JComboBox<String> bookTitleComboBox = new JComboBox<>();
                // Populate bookTitleComboBox with values from the database
                Database database = new Database();
                try {
                    ResultSet resultSet = database.getBookTitles(); // Get available book titles from the database
                    while (resultSet.next()) {
                        String bookTitle = resultSet.getString("bookTitle"); // Assuming bookTitle is a string
                        bookTitleComboBox.addItem(bookTitle);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(buyersWindow, "Помилка при завантаженні назв книг: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                }
                // Select the current book title in the combo box
                try {
                    String currentBookTitle = database.getBookTitleByPurchaseId(purchaseId); // Get the current book title based on purchaseId
                    bookTitleComboBox.setSelectedItem(currentBookTitle);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(buyersWindow, "Помилка при завантаженні поточної книги: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                }
                // Create the panel layout with input fields and the combo box
                JPanel buyerPanel = new JPanel(new GridLayout(5, 2));
                buyerPanel.add(new JLabel("Назва магазину:"));
                buyerPanel.add(storeNameField);
                buyerPanel.add(new JLabel("ПІБ покупця:"));
                buyerPanel.add(fullNameField);
                buyerPanel.add(new JLabel("Номер телефону:"));
                buyerPanel.add(phoneNumberField);
                buyerPanel.add(new JLabel("Назва книги:"));
                buyerPanel.add(bookTitleComboBox);

                int option = JOptionPane.showConfirmDialog(buyersWindow, buyerPanel, "Редагувати дані покупця", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String storeNameUpdated = storeNameField.getText();
                    String fullNameUpdated = fullNameField.getText();
                    String phoneUpdated = phoneNumberField.getText();
                    String selectedBookTitle = (String) bookTitleComboBox.getSelectedItem(); // Get the selected book title
                    // Get the bookId from the database based on the selected bookTitle
                    int bookId = -1;
                    try {
                        String checkBookQuery = "SELECT purchaseId FROM purchase WHERE bookTitle = ?";
                        PreparedStatement preparedStatement = database.connect().prepareStatement(checkBookQuery);
                        preparedStatement.setString(1, selectedBookTitle);
                        ResultSet rs = preparedStatement.executeQuery();
                        if (rs.next()) {
                            bookId = rs.getInt("purchaseId"); // Retrieve the bookId from the database
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(buyersWindow, "Помилка при перевірці книги: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                    }

                    if (bookId != -1) {
                        // If bookId was found, proceed with updating the buyer
                        Buyer updatedBuyer = new Buyer(storeNameUpdated, fullNameUpdated, phoneUpdated, bookId); // Pass the bookId as an int
                        int buyerId = (int) model.getValueAt(selectedRow, 0); // Get buyerId from the table row
                        updatedBuyer.setId(buyerId); // Set the correct ID for the buyer update

                        try {
                            database.updateBuyer(updatedBuyer); // Call the method to update the buyer in the database
                            JOptionPane.showMessageDialog(buyersWindow, "Покупець успішно оновлений!");
                            // Refresh the table with the updated data
                            model.setRowCount(0); // Clear existing rows
                            ResultSet resultSet = database.getBuyers(); // Get all buyers after update
                            while (resultSet.next()) {
                                model.addRow(new Object[]{
                                        resultSet.getInt("id"),
                                        resultSet.getString("storeName"),
                                        resultSet.getString("fullName"),
                                        resultSet.getString("phoneNumber"),
                                        resultSet.getInt("purchaseId")
                                });
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(buyersWindow, "Помилка при редагуванні покупця: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Show a message if the bookId was not found in the database
                        JOptionPane.showMessageDialog(buyersWindow, "Вибрана книга не існує в базі даних.", "Помилка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            deleteBuyerItem.addActionListener(_ -> {
                int selectedRow = buyerTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(buyersWindow, "Будь ласка, виберіть покупця для видалення.");
                    return;
                }

                int buyerId = (int) model.getValueAt(selectedRow, 0); // Get seller ID from the first column
                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(buyersWindow, "Ви впевнені, що хочете видалити цього покупця?", "Підтвердження", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Database database = new Database();
                    try {
                        database.deleteBuyer(buyerId); // Delete seller from database
                        model.removeRow(selectedRow); // Remove row from table
                        JOptionPane.showMessageDialog(buyersWindow, "Продавець успішно видалений!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(buyersWindow, "Помилка при видаленні продавця: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            buyersWindow.setVisible(true);
            // Show the seller window
            buyersWindow.setVisible(true);
            // Load data into the table
            Database database = new Database();
            try {
                ResultSet resultSet = database.getBuyers();
                while (resultSet.next()) {
                    // Get the seller ID along with other fields
                    int id = resultSet.getInt("id");
                    String storeName = resultSet.getString("storeName");
                    String fullName = resultSet.getString("fullName");
                    String phone = resultSet.getString("phoneNumber");
                    int bookIdFromDb = resultSet.getInt("purchaseId");

                    // Add the data to the table model, including the seller ID and book title
                    model.addRow(new Object[]{id, storeName, fullName, phone, bookIdFromDb});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(buyersWindow, "Помилка при завантаженні даних: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Action listener for "Продавці" menu item
        itemSellers.addActionListener(_ -> {
            // Open a new window to display the sellers
            JFrame sellerWindow = new JFrame("Список продавців");
            sellerWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            sellerWindow.setSize(800, 400);
            // Create a table to display the seller data
            String[] columns = {"Номер", "Назва магазину", "ПІБ продавця", "Адреса", "Номер телефону", "Зміна", "Трек-номер"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable sellerTable = new JTable(model);
            sellerTable.setCellSelectionEnabled(true); // Enable cell editing
            JScrollPane scrollPane = new JScrollPane(sellerTable);
            sellerWindow.add(scrollPane, BorderLayout.CENTER);
            // Create the menu for adding and editing sellers
            JMenuBar sellerMenuBar = new JMenuBar();
            JMenu sellerMenu = new JMenu("Опції");
            JMenuItem addSellerItem = new JMenuItem("Додати продавця");
            JMenuItem editSellerItem = new JMenuItem("Редагувати продавця");
            JMenuItem deleteSellerItem = new JMenuItem("Видалити продавця");
            sellerMenu.add(addSellerItem);
            sellerMenu.add(editSellerItem);
            sellerMenu.add(deleteSellerItem);
            sellerMenuBar.add(sellerMenu);
            sellerWindow.setJMenuBar(sellerMenuBar);
            // Action listener for "Додати продавця" menu item
            addSellerItem.addActionListener(_ -> {
                // Create a panel for input fields
                JTextField storeNameField = new JTextField();
                JTextField fullNameField = new JTextField();
                JTextField addressField = new JTextField();
                JTextField phoneField = new JTextField();
                JTextField shiftField = new JTextField();
                JTextField trackNumberField = new JTextField();

                JPanel sellerPanel = new JPanel(new GridLayout(6, 2));
                sellerPanel.add(new JLabel("Назва магазину:"));
                sellerPanel.add(storeNameField);
                sellerPanel.add(new JLabel("ПІБ продавця:"));
                sellerPanel.add(fullNameField);
                sellerPanel.add(new JLabel("Адреса:"));
                sellerPanel.add(addressField);
                sellerPanel.add(new JLabel("Номер телефону:"));
                sellerPanel.add(phoneField);
                sellerPanel.add(new JLabel("Зміна:"));
                sellerPanel.add(shiftField);
                sellerPanel.add(new JLabel("Трек-номер:"));
                sellerPanel.add(trackNumberField);

                int option = JOptionPane.showConfirmDialog(sellerWindow, sellerPanel, "Введіть дані продавця", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String storeName = storeNameField.getText();
                    String fullName = fullNameField.getText();
                    String address = addressField.getText();
                    String phone = phoneField.getText();
                    String shift = shiftField.getText();
                    String trackNumber = trackNumberField.getText();
                    // Create a seller object
                    Seller seller = new Seller(storeName, fullName, address, phone, shift, trackNumber);
                    // Add the seller to the database
                    Database database = new Database();
                    try {
                        database.addSeller(seller);
                        JOptionPane.showMessageDialog(sellerWindow, "Продавець успішно доданий!");
                        // Refresh the table with the updated data
                        model.setRowCount(0); // Clear existing rows
                        ResultSet resultSet = database.getSellers();
                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");
                            String storeNameFromDb = resultSet.getString("store_name");
                            String fullNameFromDb = resultSet.getString("full_name");
                            String addressFromDb = resultSet.getString("address");
                            String phoneFromDb = resultSet.getString("phone");
                            String shiftFromDb = resultSet.getString("shift");
                            String trackNumberFromDb = resultSet.getString("track_number");
                            model.addRow(new Object[]{id, storeNameFromDb, fullNameFromDb, addressFromDb, phoneFromDb, shiftFromDb, trackNumberFromDb});
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(sellerWindow, "Помилка при додаванні продавця: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            // Action listener for "Редагувати продавця" menu item
            editSellerItem.addActionListener(_ -> {
                int selectedRow = sellerTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(sellerWindow, "Будь ласка, виберіть продавця для редагування.");
                    return;
                }
                // Отримання поточних даних обраного продавця
                String storeName = (String) model.getValueAt(selectedRow, 1);
                String fullName = (String) model.getValueAt(selectedRow, 2);
                String address = (String) model.getValueAt(selectedRow, 3);
                String phone = (String) model.getValueAt(selectedRow, 4);
                String shift = (String) model.getValueAt(selectedRow, 5);
                String trackNumber = (String) model.getValueAt(selectedRow, 6);
                // Показ діалогу для редагування даних продавця
                JTextField storeNameField = new JTextField(storeName);
                JTextField fullNameField = new JTextField(fullName);
                JTextField addressField = new JTextField(address);
                JTextField phoneField = new JTextField(phone);
                JTextField shiftField = new JTextField(shift);
                JTextField trackNumberField = new JTextField(trackNumber);

                JPanel sellerPanel = new JPanel(new GridLayout(6, 2));
                sellerPanel.add(new JLabel("Назва магазину:"));
                sellerPanel.add(storeNameField);
                sellerPanel.add(new JLabel("ПІБ продавця:"));
                sellerPanel.add(fullNameField);
                sellerPanel.add(new JLabel("Адреса:"));
                sellerPanel.add(addressField);
                sellerPanel.add(new JLabel("Номер телефону:"));
                sellerPanel.add(phoneField);
                sellerPanel.add(new JLabel("Зміна:"));
                sellerPanel.add(shiftField);
                sellerPanel.add(new JLabel("Трек-номер:"));
                sellerPanel.add(trackNumberField);

                int option = JOptionPane.showConfirmDialog(sellerWindow, sellerPanel, "Редагувати дані продавця", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    Seller updatedSeller = new Seller(storeNameField.getText(), fullNameField.getText(), addressField.getText(),
                            phoneField.getText(), shiftField.getText(), trackNumberField.getText());
                    updatedSeller.setId((int) model.getValueAt(selectedRow,0));
                    Database database = new Database();
                    try {
                        database.updateSeller(updatedSeller);
                        JOptionPane.showMessageDialog(sellerWindow, "Продавець успішно оновлений!");
                        model.setRowCount(0); // Очищення рядків, які існують
                        ResultSet resultSet = database.getSellers();
                        while (resultSet.next()) {
                            model.addRow(new Object[]{
                                    resultSet.getInt("id"),
                                    resultSet.getString("store_name"),
                                    resultSet.getString("full_name"),
                                    resultSet.getString("address"),
                                    resultSet.getString("phone"),
                                    resultSet.getString("shift"),
                                    resultSet.getString("track_number")
                            });
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(sellerWindow, "Помилка при редагуванні продавця: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            deleteSellerItem.addActionListener(_ -> {
                int selectedRow = sellerTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(sellerWindow, "Будь ласка, виберіть продавця для видалення.");
                    return;
                }

                int sellerId = (int) model.getValueAt(selectedRow, 0); // Get seller ID from the first column
                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(sellerWindow, "Ви впевнені, що хочете видалити цього продавця?", "Підтвердження", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Database database = new Database();
                    try {
                        database.deleteSeller(sellerId); // Delete seller from database
                        model.removeRow(selectedRow); // Remove row from table
                        JOptionPane.showMessageDialog(sellerWindow, "Продавець успішно видалений!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(sellerWindow, "Помилка при видаленні продавця: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            sellerWindow.setVisible(true);
            // Show the seller window
            sellerWindow.setVisible(true);
            // Load data into the table
            Database database = new Database();
            try {
                ResultSet resultSet = database.getSellers();
                while (resultSet.next()) {
                    // Get the seller ID along with other fields
                    int id = resultSet.getInt("id");
                    String storeName = resultSet.getString("store_name");
                    String fullName = resultSet.getString("full_name");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    String shift = resultSet.getString("shift");
                    String trackNumber = resultSet.getString("track_number");
                    // Add the data to the table model, including the seller ID
                    model.addRow(new Object[]{id, storeName, fullName, address, phone, shift, trackNumber});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(sellerWindow, "Помилка при завантаженні даних: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        });

        itemPurchases.addActionListener(_ -> {
            // Open a new window to display the sellers
            JFrame purchaseWindow = new JFrame("Список покупок");
            purchaseWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            purchaseWindow.setSize(800, 400);
            // Create a table to display the seller data
            String[] columns = {"Номер", "Назва книги", "Кількість", "Вартість", "Жанр"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable purchaseTable = new JTable(model);
            purchaseTable.setCellSelectionEnabled(true); // Enable cell editing
            JScrollPane scrollPane = new JScrollPane(purchaseTable);
            purchaseWindow.add(scrollPane, BorderLayout.CENTER);
            // Create the menu for adding and editing sellers
            JMenuBar purchaseMenuBar = new JMenuBar();
            JMenu purchaseMenu = new JMenu("Опції");
            JMenuItem addPurchaseItem = new JMenuItem("Додати покупку");
            JMenuItem editPurchaseItem = new JMenuItem("Редагувати покупку");
            JMenuItem deletePurchaseItem = new JMenuItem("Видалити покупку");
            purchaseMenu.add(addPurchaseItem);
            purchaseMenu.add(editPurchaseItem);
            purchaseMenu.add(deletePurchaseItem);
            purchaseMenuBar.add(purchaseMenu);
            purchaseWindow.setJMenuBar(purchaseMenuBar);
            // Action listener for "Додати продавця" menu item
            addPurchaseItem.addActionListener(_ -> {
                // Create a panel for input fields
                JTextField bookTitleField = new JTextField();
                JTextField quantityField = new JTextField();
                JTextField costField = new JTextField();
                JTextField genreField = new JTextField();

                JPanel purchasePanel = new JPanel(new GridLayout(4, 2));
                purchasePanel.add(new JLabel("Назва книги:"));
                purchasePanel.add(bookTitleField);
                purchasePanel.add(new JLabel("Кількість:"));
                purchasePanel.add(quantityField);
                purchasePanel.add(new JLabel("Вартість:"));
                purchasePanel.add(costField);
                purchasePanel.add(new JLabel("Жанр:"));
                purchasePanel.add(genreField);

                int option = JOptionPane.showConfirmDialog(purchaseWindow, purchasePanel, "Введіть дані покупки", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String bookTitle = bookTitleField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    double cost = Double.parseDouble(costField.getText());
                    String genre = genreField.getText();
                    // Create a seller object
                    Purchase purchase = new Purchase(bookTitle, quantity, cost, genre);
                    // Add the seller to the database
                    Database database = new Database();
                    try {
                        database.addPurchase(purchase);
                        JOptionPane.showMessageDialog(purchaseWindow, "Продавець успішно доданий!");
                        // Refresh the table with the updated data
                        model.setRowCount(0); // Clear existing rows
                        ResultSet resultSet = database.getPurchases();
                        while (resultSet.next()) {
                            int id = resultSet.getInt("purchaseId");
                            String bookTitleFromDb = resultSet.getString("bookTitle");
                            String quantityFromDb = resultSet.getString("quantity");
                            String costFromDb = resultSet.getString("cost");
                            String genreFromDb = resultSet.getString("genre");
                            model.addRow(new Object[]{id, bookTitleFromDb, quantityFromDb, costFromDb, genreFromDb});
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(purchaseWindow, "Помилка при додаванні продавця: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            // Action listener for "Редагувати продавця" menu item
            editPurchaseItem.addActionListener(_ -> {
                int selectedRow = purchaseTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(purchaseWindow, "Будь ласка, виберіть продавця для редагування.");
                    return;
                }
                // Отримання поточних даних обраного продавця
                String bookTitle = (String) model.getValueAt(selectedRow, 1);
                int quantity = (int) model.getValueAt(selectedRow, 2);
                double cost = (double) model.getValueAt(selectedRow, 3);
                String genre = (String) model.getValueAt(selectedRow, 4);
                // Показ діалогу для редагування даних продавця
                JTextField bookTitleField = new JTextField(bookTitle);
                JTextField quantityField = new JTextField(String.valueOf(quantity));
                JTextField costField = new JTextField(String.valueOf(cost));
                JTextField genreField = new JTextField(genre);

                JPanel sellerPanel = new JPanel(new GridLayout(4, 2));
                sellerPanel.add(new JLabel("Назва книги:"));
                sellerPanel.add(bookTitleField);
                sellerPanel.add(new JLabel("Кількість:"));
                sellerPanel.add(quantityField);
                sellerPanel.add(new JLabel("Вартість:"));
                sellerPanel.add(costField);
                sellerPanel.add(new JLabel("Жанр:"));
                sellerPanel.add(genreField);

                int option = JOptionPane.showConfirmDialog(purchaseWindow, sellerPanel, "Редагувати дані покупки", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    Purchase updatedPurchase = new Purchase(bookTitleField.getText(), Integer.parseInt(quantityField.getText()), Double.parseDouble(costField.getText()),
                            genreField.getText());
                    updatedPurchase.setPurchaseId((int) model.getValueAt(selectedRow,0));
                    Database database = new Database();
                    try {
                        database.updatePurchase(updatedPurchase);
                        JOptionPane.showMessageDialog(purchaseWindow, "Покупка успішно оновлена!");
                        model.setRowCount(0); // Очищення рядків, які існують
                        ResultSet resultSet = database.getPurchases();
                        while (resultSet.next()) {
                            model.addRow(new Object[]{
                                    resultSet.getInt("purchaseId"),
                                    resultSet.getString("bookTitle"),
                                    resultSet.getString("quantity"),
                                    resultSet.getString("cost"),
                                    resultSet.getString("genre"),
                            });
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(purchaseWindow, "Помилка при редагуванні покупки: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            deletePurchaseItem.addActionListener(_ -> {
                int selectedRow = purchaseTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(purchaseWindow, "Будь ласка, виберіть покупку для видалення.");
                    return;
                }
                int purchaseId = (int) model.getValueAt(selectedRow, 0); // Get seller ID from the first column
                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(purchaseWindow, "Ви впевнені, що хочете видалити цю покупку?", "Підтвердження", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Database database = new Database();
                    try {
                        database.deletePurchase(purchaseId); // Delete seller from database
                        model.removeRow(selectedRow); // Remove row from table
                        JOptionPane.showMessageDialog(purchaseWindow, "Покупка успішно видалена!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(purchaseWindow, "Помилка при видаленні покупки: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            purchaseWindow.setVisible(true);
            // Show the seller window
            purchaseWindow.setVisible(true);
            // Load data into the table
            Database database = new Database();
            try {
                ResultSet resultSet = database.getPurchases();
                while (resultSet.next()) {
                    // Get the seller ID along with other fields
                    int id = resultSet.getInt("purchaseId");
                    String bookTitle = resultSet.getString("bookTitle");
                    int quantity = resultSet.getInt("quantity");
                    double cost = resultSet.getDouble("cost");
                    String genre = resultSet.getString("genre");
                    // Add the data to the table model, including the seller ID
                    model.addRow(new Object[]{id, bookTitle, quantity, cost, genre});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(purchaseWindow, "Помилка при завантаженні даних: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        });
        itemQueryResults.addActionListener(_ -> {
            try {
                // Створення нового вікна для відображення результатів
                JFrame resultsFrame = new JFrame("Результати запитів");
                resultsFrame.setSize(400, 300);
                resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel panel1 = new JPanel();
                panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
                // Додавання компонентів для відображення результатів
                JLabel labelSellers = new JLabel("Кількість продавців: ");
                JLabel labelBuyers = new JLabel("Кількість покупців: ");
                JLabel labelTotalSales = new JLabel("Загальна сума продажів: ");
                JLabel labelBooksSold = new JLabel("Кількість проданих книг певного жанру: ");
                JLabel labelMostPopularBook = new JLabel("Найбільш популярна книга: ");
                JLabel labelBiggestSpender = new JLabel("Покупець з найбільшими витратами: ");

                panel1.add(labelSellers);
                panel1.add(labelBuyers);
                panel1.add(labelTotalSales);
                panel1.add(labelBooksSold);
                panel1.add(labelMostPopularBook);
                panel1.add(labelBiggestSpender);
                // Додавання панелі до нового вікна
                resultsFrame.add(panel1);
                resultsFrame.setVisible(true);
                Database database = new Database();
                // Отримання та встановлення результатів
                labelSellers.setText("Кількість продавців: " + database.getNumberOfSellers());
                labelBuyers.setText("Кількість покупців: " + database.getNumberOfBuyers());
                labelTotalSales.setText("Загальна сума продажів: " + database.getTotalSalesAmount());
                labelBooksSold.setText("Кількість проданих книг певного жанру: " + database.getBooksSoldByGenre("Класична література"));
                labelMostPopularBook.setText("Найбільш популярна книга: " + database.getMostPopularBook());
                labelBiggestSpender.setText("Покупець з найбільшими витратами: " + database.getBiggestSpender());
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Помилка під час отримання даних з бази.", "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Display the main window
        frame.setVisible(true);
    }
}
