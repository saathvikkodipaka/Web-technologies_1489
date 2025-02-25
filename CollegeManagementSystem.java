import java.sql.*;
import java.util.Scanner;

public class CollegeManagementSystem {
    // JDBC URL, username, and password of MySQL Server
    private static final String URL = "jdbc:mysql://localhost:3306/CollegeManagementSystem";
    private static final String USER = "root"; // Change to your MySQL username
    private static final String PASSWORD = "saathvik"; // Change to your MySQL password

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to MySQL database.");

            createTable(conn);
            Scanner scanner = new Scanner(System.in);
            
            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Insert Course");
                System.out.println("2. Update Course Credits");
                System.out.println("3. Delete Course");
                System.out.println("4. Display Courses");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> insertCourse(conn, scanner);
                    case 2 -> updateCourseCredits(conn, scanner);
                    case 3 -> deleteCourse(conn, scanner);
                    case 4 -> displayCourses(conn);
                    case 5 -> {
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice! Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to create the Courses table if it doesn't exist
    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Courses (" +
                     "CourseID INT PRIMARY KEY, " +
                     "Name VARCHAR(100) NOT NULL, " +
                     "Credits INT NOT NULL)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table 'Courses' ensured.");
        }
    }

    // Method to insert a new course
    private static void insertCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Course ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Course Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Credits: ");
        int credits = scanner.nextInt();

        String sql = "INSERT INTO Courses (CourseID, Name, Credits) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, credits);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully.");
        }
    }

    // Method to update course credits
    private static void updateCourseCredits(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Course ID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new Credits: ");
        int credits = scanner.nextInt();

        String sql = "UPDATE Courses SET Credits = ? WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, credits);
            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("Course ID not found.");
            }
        }
    }

    // Method to delete a course
    private static void deleteCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Course ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM Courses WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("Course ID not found.");
            }
        }
    }

    // Method to display all courses
    private static void displayCourses(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Courses";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nCourses Table Records:");
            System.out.println("-----------------------------------------");
            while (rs.next()) {
                System.out.println("CourseID: " + rs.getInt("CourseID") +
                                   " | Name: " + rs.getString("Name") +
                                   " | Credits: " + rs.getInt("Credits"));
            }
        }
    }
}
