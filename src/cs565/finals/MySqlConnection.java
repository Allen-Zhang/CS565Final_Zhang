package cs565.finals;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySqlConnection {

	private String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private String DB_URL = "jdbc:mysql://localhost:3306/cs565_final_zhang";
	private String DB_USERNAME = "cs";
	private String DB_PASSWORD = "java";
	
	public Connection getConnection() {		
		Connection conn = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;	
	}
	
}
