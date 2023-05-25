<%-- 
    Document   : insert_form
    Created on : 2023. 5. 23., 오전 12:54:28
    Author     : wwwhyuk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <title><%=session.getAttribute("userid")%>님의 주소록 추가</title>
    </head>
    <body>
        <%@include file="header.jspf"%>

        <div id="sidebar">
            <jsp:include page="sidebar_menu.jsp" />
        </div>

        <div id="main">
            <h3>추가하시고자 하는 이메일, 이름, 전화번호를 작성 후,<br>
                제출 버튼을 눌러주세요.
            </h3>
            <form action="insert.do" method="POST">
                <table border="2" summary="addrbook">
                    <tr>
                        <th scope = "col" colspan="2" style="background-color: rgba( 255, 255, 255, 0 ); border: 0px">    
                        </th>
                    </tr>
                    <tbody>
                        <tr>
                            <td >이름</td>
                            <td scope="col"><input type="text" name="name" required/></td>
                        </tr>
                        <tr>
                            <td>이메일</td>
                            <td><input type="text" name="email" required/></td>
                        </tr>
                        <tr>
                            <td>전화번호</td>
                            <td>
                                <input type="text" name="phone" required/>
                                <input type="hidden" name="adder" value=<%=session.getAttribute("userid")%>>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <input type="submit" value="제출"/>
                                <input type="reset" value="초기화"/>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
        <%@include file="footer.jspf"%>
    </body>
</html>
