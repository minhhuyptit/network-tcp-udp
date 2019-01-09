package LoginTCP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	public static Connection getConnection(){
        Connection connection = null;
       try {       
           Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
           String url = "jdbc:sqlserver://localhost:1433; databaseName=NhanVien";
           String user = "sa";
           String pass = "123456";
           connection = DriverManager.getConnection(url, user, pass);
       } catch (Exception ex) {
           ex.printStackTrace();
       }
       return connection;
   }
   
   public static void closeConnection(Connection con){
       if(con != null){
           try {
               con.close();
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
       }
   }
}
