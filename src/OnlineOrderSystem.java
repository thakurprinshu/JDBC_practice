import java.sql.*;
import java.util.Scanner;

public class OnlineOrderSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n--- Online Order System ---");
                System.out.println("1. Insert New Order");
                System.out.println("2. Fetch Order History by Customer ID");
                System.out.println("3. Show Top 3 Customers by Purchase Amount");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        insertNewOrder(sc);
                        break;
                    case 2:
                        fetchOrderHistory(sc);
                        break;
                    case 3:
                        showTopCustomers();
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        }
    }

    public static void insertNewOrder(Scanner sc) {
        System.out.print("Enter Order ID: ");
        int orderId = sc.nextInt();
        System.out.print("Enter Customer ID: ");
        int custId = sc.nextInt();
        System.out.print("Enter Order Amount: ");
        double amount = sc.nextDouble();

        String insertOrderSQL = "INSERT INTO Orders(order_id, cust_id, order_date, amount) VALUES (?, ?, CURDATE(), ?)";
        String updateCustomerSQL = "UPDATE Customer SET last_order_date = CURDATE() WHERE cust_id = ?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            con.setAutoCommit(false);

            try (PreparedStatement pstmtOrder = con.prepareStatement(insertOrderSQL);
                 PreparedStatement pstmtCustomer = con.prepareStatement(updateCustomerSQL)) {

                pstmtOrder.setInt(1, orderId);
                pstmtOrder.setInt(2, custId);
                pstmtOrder.setDouble(3, amount);
                pstmtOrder.executeUpdate();

                // Update Customer last_order_date
                pstmtCustomer.setInt(1, custId);
                pstmtCustomer.executeUpdate();

                con.commit();
                System.out.println(" Order inserted & Customer updated successfully!");

            } catch (SQLException e) {
                con.rollback();
                System.out.println(" Transaction failed. Rolled back.");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void fetchOrderHistory(Scanner sc) {
        System.out.print("Enter Customer ID: ");
        int custId = sc.nextInt();

        String sql = "SELECT order_id, order_date, amount FROM Orders WHERE cust_id = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, custId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Order History for Customer " + custId + ":");
            while (rs.next()) {
                System.out.println("OrderID: " + rs.getInt("order_id") +
                        ", Date: " + rs.getDate("order_date") +
                        ", Amount: " + rs.getDouble("amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showTopCustomers() {
        String sql = "SELECT c.cust_id, c.cust_name, SUM(o.amount) AS total_amount " +
                "FROM Customer c JOIN Orders o ON c.cust_id = o.cust_id " +
                "GROUP BY c.cust_id, c.cust_name " +
                "ORDER BY total_amount DESC LIMIT 3";

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Top 3 Customers by Purchase Amount:");
            while (rs.next()) {
                System.out.println(rs.getInt("cust_id") + " - " +
                        rs.getString("cust_name") + " - Total: " +
                        rs.getDouble("total_amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
