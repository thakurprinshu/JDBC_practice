//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.sql.*;
import java.util.Scanner;

public class JdbcEmployeeDemo {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/employ_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Prinshu@123";

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n--- Employee Management ---");
                System.out.println("1. Get Employees by Department");
                System.out.println("2. Insert New Employee");
                System.out.println("3. Update Employee Salary");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter Department Name: ");
                        String deptName = sc.nextLine();
                        getEmployeesByDepartment(deptName);
                        break;
                    case 2:
                        System.out.print("Enter Emp ID: ");
                        int empId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Designation: ");
                        String designation = sc.nextLine();
                        System.out.print("Enter Salary: ");
                        double salary = sc.nextDouble();
                        System.out.print("Enter Dept ID: ");
                        int deptId = sc.nextInt();
                        insertEmployee(empId, name, designation, salary, deptId);
                        break;
                    case 3:
                        System.out.print("Enter Emp ID: ");
                        int id = sc.nextInt();
                        System.out.print("Enter New Salary: ");
                        double newSalary = sc.nextDouble();
                        updateSalary(id, newSalary);
                        break;
                    case 4:
                        System.out.print("Enter Emp ID to Delete: ");
                        int delId = sc.nextInt();
                        deleteEmployee(delId);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1. Get employees by department
    public static void getEmployeesByDepartment(String deptName) {
        String sql = "SELECT e.emp_id, e.name, e.designation, e.salary " +
                "FROM Employee e JOIN Department d ON e.dept_id = d.dept_id " +
                "WHERE d.dept_name = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, deptName);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Employees in " + deptName + " Department:");
            while (rs.next()) {
                System.out.println(rs.getInt("emp_id") + " " +
                        rs.getString("name") + " " +
                        rs.getString("designation") + " " +
                        rs.getDouble("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. Insert employee
    public static void insertEmployee(int empId, String name, String designation, double salary, int deptId) {
        String sql = "INSERT INTO Employee VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, empId);
            pstmt.setString(2, name);
            pstmt.setString(3, designation);
            pstmt.setDouble(4, salary);
            pstmt.setInt(5, deptId);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " employee(s) inserted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. Update salary
    public static void updateSalary(int empId, double newSalary) {
        String sql = "UPDATE Employee SET salary = ? WHERE emp_id = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setDouble(1, newSalary);
            pstmt.setInt(2, empId);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " employee(s) updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. Delete employee
    public static void deleteEmployee(int empId) {
        String sql = "DELETE FROM Employee WHERE emp_id = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, empId);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " employee(s) deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
