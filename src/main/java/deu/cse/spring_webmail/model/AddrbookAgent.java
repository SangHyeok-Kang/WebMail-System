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
    private String password;
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

    public AddrbookAgent(String mysqlServerIp, String mysqlServerPort, String userName, String password, String jdbcDriver) {
        this.mysqlServerIp = mysqlServerIp;
        this.mysqlServerPort = mysqlServerPort;
        this.userName = userName;
        this.password = password;
        this.jdbcDriver = jdbcDriver;
        log.debug("AddrbookAgent(): mysqlServerIp = {}, jdbvDriver = {}", mysqlServerIp, jdbcDriver);

        /*try {
            socket = new Socket(server, port);
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (Exception e) {
            log.error("UserAdminAgent 생성자 예외: {}", e.getMessage());
        }*/
        //isConnected = connect();
    }

    public boolean addAddrbookDB(String email, String name, String phone, String adder) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/mail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL = {}", JDBC_URL);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql;

        if (email.equals(adder)) {
            return false;
        } else {
            try {
                Class.forName(jdbcDriver);

                conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);
                sql = "SELECT * FROM userinfo where userid = '" + email + "' and username = '" + name + "'";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if (rs.next()) {

                    rs.close();
                    stmt.close();

                    sql = "SELECT * FROM addrbook WHERE email = '" + email + "' and adder = '" + adder + "'";
                    System.out.println(sql);
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        return false;
                    } else {
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(sql);
                        sql = "INSERT INTO addrbook VALUES(?,?,?,?)";
                        PreparedStatement pstmt = conn.prepareStatement(sql);

                        pstmt.setString(1, email);
                        pstmt.setString(2, name);
                        pstmt.setString(3, phone);
                        pstmt.setString(4, adder);

                        pstmt.executeUpdate();

                        pstmt.close();
                        conn.close();

                        return true;
                    }
                } else {
                    return false;
                }

            } catch (Exception ex) {
                log.error("오류가 발생했습니다. (발생오류: {})", ex.getMessage());
                return false;
            }
        }
    }

    public boolean deleteAddrbookDB(String email, String userid) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/mail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL = {}", JDBC_URL);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql;
        try {

            Class.forName(jdbcDriver);

            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);
            sql = "SELECT * FROM addrbook WHERE email = '" + email + "' and adder = '" + userid + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                sql = "DELETE FROM addrbook WHERE email = ? and adder = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, email);
                pstmt.setString(2, userid);

                pstmt.executeUpdate();

                pstmt.close();
                conn.close();

            } else {
                return false;
            }

        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생오류: {})", ex.getMessage());
            return false;
        }
        return true;
    }

}
