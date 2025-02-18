import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {

    private static DatabaseHandler handler = null;
    private static Connection connection = null;

    private DatabaseHandler() {
        createConnection();
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    private void createConnection() {
        String dburl = "jdbc:mysql://localhost:3306/gotyme?serverTimezone=Asia/Kuala_Lumpur";
        String userName = "root";
        String password = "Armored440";

        try {
            connection = DriverManager.getConnection(dburl, userName, password);
            System.out.println("Connection to database established successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to connect to the database. Please check the URL, username, and password.");
        }
    }

    public ResultSet execQuery(String query) {
        ResultSet result = null;
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement(query);
            result = stmt.executeQuery();
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler " + ex.getLocalizedMessage());
        }
        return result;
    }

    public static boolean validateLogin(String uname, String pword) {
        getInstance();
        String query = "SELECT * FROM login WHERE username = ? AND userpassword = ?";
        ResultSet result = null;
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, uname);
            pstmt.setString(2, pword);
            result = pstmt.executeQuery();

            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean adduser(User user) {
        getInstance();
        PreparedStatement pstatement = null;
        try {
            pstatement = connection.prepareStatement("INSERT INTO login (username, userpassword) VALUES (?, ?)");
            pstatement.setString(1, user.getUsername());
            pstatement.setString(2, user.getPassword());

            return pstatement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstatement != null) pstatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean deleteUser(User user) {
        getInstance();
        PreparedStatement pstatement = null;
        try {
            pstatement = connection.prepareStatement("DELETE FROM login WHERE username = ?");
            pstatement.setString(1, user.getUsername());

            return pstatement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstatement != null) pstatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean updateUser(User user) {
        getInstance();
        PreparedStatement pstatement = null;
        try {
            pstatement = connection.prepareStatement("UPDATE login SET userpassword = ? WHERE username = ?");
            pstatement.setString(1, user.getPassword());
            pstatement.setString(2, user.getUsername());

            int rowsUpdated = pstatement.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated); // Debugging information

            return rowsUpdated > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstatement != null) pstatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
