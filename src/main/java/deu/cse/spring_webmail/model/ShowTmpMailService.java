/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import deu.cse.spring_webmail.dto.MailDto;
import java.sql.*;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author host
 */
@Slf4j
public class ShowTmpMailService {

    private String mysqlServerIp;
    private String mysqlServerPort;
    private String userName;
    private String password;
    private String jdbcDriver;

    ArrayList<MailDto> list = new ArrayList<MailDto>();
    public ShowTmpMailService(String mysqlServerIp, String mysqlServerPort, String userName, String password, String jdbcDriver) {
        this.mysqlServerIp = mysqlServerIp;
        this.mysqlServerPort = mysqlServerPort;
        this.userName = userName;
        this.password = password;
        this.jdbcDriver = jdbcDriver;
        log.debug("SaveMailService(): mysqlServerIp = {}, jdbvDriver = {}", mysqlServerIp, jdbcDriver);
    }

    public ShowTmpMailService() {

    }

    public ArrayList<MailDto> ShowTmpMailList(String user) {
        int count = 1;
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/mail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL = {}", JDBC_URL);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName(jdbcDriver);

            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);
            String sql = "SELECT * FROM tmp_mail WHERE userid ='" + user + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                MailDto mail = new MailDto();
                mail.setCount(count);
                mail.setSubject(rs.getString("subject"));
                mail.setReference(rs.getString("reference"));
                mail.setBody(rs.getString("body"));
                mail.setDate(rs.getString("date"));
                list.add(mail);
                count++;
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생오류: {})", ex.getMessage());

        }
        return list;
    }

    public String getMessageTable(ArrayList<MailDto> message) {
        StringBuilder buffer = new StringBuilder();

        // 메시지 제목 보여주기
        buffer.append("<table>");  // table start
        buffer.append("<tr> "
                + " <th> No. </td> "
                + " <th> 제목 </td>     "
                + " <th> 저장 날짜 </td>   "
                + " <th> 삭제 </td>   "
                + " </tr>");

        for (int i = 0; i < message.size(); i++) {
            buffer.append("<tr> "
                    + " <td id=no>" + message.get(i).getCount() + " </td> "
                    + " <td id=subject> "
                    + " <a href=show_tmp_message?msgid=" + message.get(i).getCount() + " title=\"메일 보기\"> "
                    + message.get(i).getSubject() + "</a> </td>"
                    + " <td id=date>" + message.get(i).getDate() + "</td>"
                    + " <td id=delete>"
                    + "<a href=delete_tmp_mail.do"
                    + "?msgid=" + message.get(i).getCount() + "> 삭제 </a>" + "</td>"
                    + " </tr>");
        }
        buffer.append("</table>");

        return buffer.toString();
    }

    public String getMessage(ArrayList<MailDto> message, Integer msgid) {
        StringBuilder buffer = new StringBuilder();

        int count = msgid - 1;

        buffer.append("보낸 사람:   <br>");
        buffer.append("받은 사람: <br>");
        buffer.append("Cc &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : " + message.get(count).getReference() + " <br>");
        buffer.append("보낸 날짜: " + message.get(count).getDate() + " <br>");
        buffer.append("제 &nbsp;&nbsp;&nbsp;  목: " + message.get(count).getSubject() + " <br> <hr>");

        buffer.append(message.get(msgid).getBody());

        /*String attachedFile = parser.getFileName();
        if (attachedFile != null) {
            buffer.append("<br> <hr> 첨부파일: <a href=download"
                    + "?userid=" + this.userid
                    + "&filename=" + attachedFile.replaceAll(" ", "%20")
                    + " target=_top> " + attachedFile + "</a> <br>");
        }*/
        return buffer.toString();
    }

    public boolean deleteMessage(ArrayList<MailDto> message, String user, Integer msgid) {
        int count = msgid - 1;
        String date = message.get(count).getDate();
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/mail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL = {}", JDBC_URL);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName(jdbcDriver);

            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);
            String sql = "DELETE FROM tmp_mail WHERE userid = ? and date = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user);
            pstmt.setString(2, date);

            pstmt.executeUpdate();

            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }

        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생오류: {})", ex.getMessage());
            return false;
        }
        return true;

    }
}
