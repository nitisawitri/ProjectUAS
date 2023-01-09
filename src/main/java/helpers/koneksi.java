package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {
    public static Connection getConnection(){
        Connection koneksi = null;
        try {

    Class.forName("com.mysql.cj.jdbc.Driver");
    System.out.println("Connecting..");
    koneksi = DriverManager.getConnection(
            "jdbc:mysql://localhost/mahasiswa",
            "root",
            "");
    System.out.println("Connected!");
        } catch (ClassNotFoundException e) {
            System.out.println("Connection error!");
        } catch (SQLException e){
            System.out.println("SQL error!");
        } return koneksi;
    }
}
