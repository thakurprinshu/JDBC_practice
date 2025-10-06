import java.sql.*;
import java.util.Scanner;

public class LibraryManagement {

    private static final String URL = "jdbc:mysql://localhost:3306/librarydb"; // change db name
    private static final String USER = "root"; // change username
    private static final String PASS = "password"; // change password

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             Scanner sc = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n===== Library Menu =====");
                System.out.println("1. Issue Book");
                System.out.println("2. Return Book");
                System.out.println("3. Search Book");
                System.out.println("4. Student-wise Book Issue History");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 -> issueBook(con, sc);
                    case 2 -> returnBook(con, sc);
                    case 3 -> searchBook(con, sc);
                    case 4 -> studentHistory(con, sc);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void issueBook(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Student ID: ");
        int sid = sc.nextInt();
        System.out.print("Enter Book ID: ");
        int bid = sc.nextInt();

        String check = "SELECT available_copies FROM Book WHERE book_id=?";
        try (PreparedStatement ps = con.prepareStatement(check)) {
            ps.setInt(1, bid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int available = rs.getInt(1);
                if (available > 0) {
                    String issue = "INSERT INTO Issue(student_id, book_id, issue_date) VALUES (?, ?, CURDATE())";
                    try (PreparedStatement ps2 = con.prepareStatement(issue)) {
                        ps2.setInt(1, sid);
                        ps2.setInt(2, bid);
                        ps2.executeUpdate();
                    }

                    String update = "UPDATE Book SET available_copies = available_copies - 1 WHERE book_id=?";
                    try (PreparedStatement ps3 = con.prepareStatement(update)) {
                        ps3.setInt(1, bid);
                        ps3.executeUpdate();
                    }

                    System.out.println("Book issued successfully!");
                } else {
                    System.out.println("Book not available!");
                }
            }
        }
    }

    private static void returnBook(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Issue ID: ");
        int iid = sc.nextInt();

        String query = "SELECT book_id FROM Issue WHERE issue_id=? AND return_date IS NULL";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, iid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int bid = rs.getInt(1);

                String updateIssue = "UPDATE Issue SET return_date=CURDATE() WHERE issue_id=?";
                try (PreparedStatement ps2 = con.prepareStatement(updateIssue)) {
                    ps2.setInt(1, iid);
                    ps2.executeUpdate();
                }

                String updateBook = "UPDATE Book SET available_copies = available_copies + 1 WHERE book_id=?";
                try (PreparedStatement ps3 = con.prepareStatement(updateBook)) {
                    ps3.setInt(1, bid);
                    ps3.executeUpdate();
                }

                System.out.println("Book returned successfully!");
            } else {
                System.out.println("Invalid Issue ID or already returned!");
            }
        }
    }

    private static void searchBook(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter title/author: ");
        sc.nextLine(); // consume newline
        String keyword = sc.nextLine();

        String query = "SELECT * FROM Book WHERE title LIKE ? OR author LIKE ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Book ID: " + rs.getInt("book_id") +
                        ", Title: " + rs.getString("title") +
                        ", Author: " + rs.getString("author") +
                        ", Available Copies: " + rs.getInt("available_copies"));
            }
        }
    }

    private static void studentHistory(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Student ID: ");
        int sid = sc.nextInt();

        String query = "SELECT I.issue_id, B.title, I.issue_date, I.return_date " +
                "FROM Issue I JOIN Book B ON I.book_id = B.book_id " +
                "WHERE I.student_id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, sid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("Issue ID: " + rs.getInt("issue_id") +
                        ", Book: " + rs.getString("title") +
                        ", Issued: " + rs.getDate("issue_date") +
                        ", Returned: " + rs.getDate("return_date"));
            }
        }
    }
}

