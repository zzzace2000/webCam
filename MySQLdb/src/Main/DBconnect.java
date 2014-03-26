package Main;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import com.mysql.jdbc.Driver;
import com.mysql.jdbc.Statement;

public class DBconnect {
	
	private Connection con;
	private Statement st;
	private ResultSet rs;
	
	public DBconnect() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ChatRoom", "root", "");
			st = (Statement) con.createStatement();
			
			
		} catch (Exception ex) {
			System.out.println("Erro: "+ex);
		}
		
		
		
	}
	
	public void getData() {
		try {
			
			String query = "select * from data";
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				System.out.println("Name: "+name+", email: "+email);
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
}
