<%-- 
    Document   : signup
    Created on : 2023. 4. 11., 오후 7:34:25
    Author     : host
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>회원가입</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <%@include file="header.jspf"%>

        <div id="signup_main">
            사용하실 ID와 암호를 입력해 주시기 바랍니다! <br> <br>

            <form name="AddUser" action="signup_user.do" method="POST">
                <table border="0" align="left">
                    <tr>
                        <td>이름 </td>
                        <td> <input type="text" name="username" value="" /> </td>
                    </tr>
                    <tr>
                        <td>사용자 ID</td>
                        <td> <input type="text" name="id" value="" size="20" />  </td>
                    </tr>
                    <tr>
                        <td>암호 </td>
                        <td> <input type="password" name="password" value="" /> </td>
                    </tr>
                    
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="회원가입" name="register" />
                            <input type="reset" value="초기화" name="reset" />
                            <button type="button" onclick="history.back()">뒤로가기</button>
                        </td>
                    </tr>
                </table>

            </form>
        </div>

        <%@include file="footer.jspf"%>
    </body>
</html>
