package deu.cse.spring_webmail.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author wwwhyuk
 */
@Slf4j
public class AddrbookAgent {

    private String mysqlServerIp;
    private String mysqlServerPort;
    private String userName;
    private String pass;
    private String jdbcDriver;

    private String server;
    private int port;
    Socket socket = null;
    InputStream is = null;
    OutputStream os = null;
    boolean isConnected = false;
    private String ROOT_ID;
    private String ROOT_PASSWORD;
    private String ADMIN_ID;
    // private final String EOL = "\n";
    private final String EOL = "\r\n";
    private String cwd;

    public AddrbookAgent() {

    }

    public AddrbookAgent(String server, int port, String cwd,
            String root_id, String root_pass, String admin_id,
            String mysqlServerIp, String mysqlServerPort, String userName, String pass, String jdbcDriver) {
        log.debug("UserAdminAgent created: server = " + server + ", port = " + port);
        this.server = server;  // 127.0.0.1
        this.port = port;  // 4555
        this.cwd = cwd;
        this.ROOT_ID = root_id;
        this.ROOT_PASSWORD = root_pass;
        this.ADMIN_ID = admin_id;
        this.mysqlServerIp = mysqlServerIp;
        this.mysqlServerPort = mysqlServerPort;
        this.userName = userName;
        this.pass = pass;
        this.jdbcDriver = jdbcDriver;
        log.debug("isConnected = {}, root.id = {}", isConnected, ROOT_ID);
        log.debug("UserAdminAgent(): mysqlServerIp = {}, jdbvDriver = {}", mysqlServerIp, jdbcDriver);

        try {
            socket = new Socket(server, port);
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (Exception e) {
            log.error("UserAdminAgent 생성자 예외: {}", e.getMessage());
        }

        //isConnected = connect();
    }

    public boolean addAddrbookDB(String email, String name, String phone, String adder) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/mail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL = {}", JDBC_URL);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(jdbcDriver);

            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.pass);
            String sql = "INSERT INTO addrbook VALUES(?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, email);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);
            pstmt.setString(4, adder);

            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

            return true;

        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생오류: {})", ex.getMessage());
            return false;
        }
    }

    public boolean updateAddrbookDB(String email, String name, String phone) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/mail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL = {}", JDBC_URL);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(jdbcDriver);

            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.pass);
            String sql = "UPDATE addrbook SET name = ?, phone = ? WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);

            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

            return true;

        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생오류: {})", ex.getMessage());
            return false;
        }
    }

    public boolean deleteAddrbookDB(String email) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/mail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL = {}", JDBC_URL);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(jdbcDriver);

            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.pass);
            String sql = "DELETE FROM addrbook WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, email);

            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

            return true;

        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생오류: {})", ex.getMessage());
            return false;
        }
    }

    public ArrayList<List<String>> searchAddrbookDB(String email) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/mail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL = {}", JDBC_URL);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<List<String>> resultList = new ArrayList<>();
        try {
            Class.forName(jdbcDriver);

            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.pass);
            String sql = "SELECT * FROM addrbook WHERE email = ?";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String emailResult = rs.getString("email");
                String nameResult = rs.getString("name");
                String phoneResult = rs.getString("phone");

                List<String> row = new ArrayList<>();
                row.add(nameResult);
                row.add(emailResult);
                row.add(phoneResult);

                resultList.add(row);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생오류: {})", ex.getMessage());

        }
        return resultList;
    }
}
