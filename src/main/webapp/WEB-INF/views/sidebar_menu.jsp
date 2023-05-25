<%-- 
    Document   : sidebar_menu
    Created on : 2022. 6. 10., 오후 3:25:30
    Author     : skylo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>웹메일 시스템 메뉴</title>
    </head>
    <body>
        <br> <br>
        
        <span style="color: indigo"> <strong>사용자: <%= session.getAttribute("userid") %> </strong> </span> <br>

        <p> <a href="main_menu"> 메일 읽기 </a> </p>
        <p> <a href="write_mail"> 메일 쓰기 </a> </p>
        <p><a href="show_tmp_mail">임시저장 메일 조회</a></p>

        <p> <a href="read_book"> 주소록 조회 </a> </p>
        <p> <a href="add_book"> 주소록 추가 </a> </p>
        <p> <a href="search_book"> 주소록 수정 </a> </p>
        <p> <a href="del_book"> 주소록 삭제 </a> </p>
        <p><a href="login.do?menu=<%= CommandType.LOGOUT %>">로그아웃</a></p>
    </body>
</html>
