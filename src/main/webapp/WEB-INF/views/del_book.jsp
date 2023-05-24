<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <title><%=session.getAttribute("userid")%>님의 주소록 삭제</title>
    </head>
    
    <c:if test="${not empty msg}">
        <script>
            var message = "${msg}";
            alert(message);
        </script>
    </c:if>
    <body>
        <%@include file="header.jspf"%>

        <div id="sidebar">
            <jsp:include page="sidebar_menu.jsp" />
        </div>


        <div id="main">

            <h3>삭제하시고자 하는 이메일을 작성 후, 삭제 버튼을 눌러주세요.</h3>
            <table border="1" summary="addrbook">
                <thead><th></th></thead>
                <tbody>
                <form action="delete" method="post">
                    <tr>
                        <td>이메일</td>
                        <td><input type="text" name="email" placeholder="이메일 입력" required></td>
                        <td><input type="submit" value="삭제" /></td>
                    </tr>
                </form>
                </tbody>
            </table>

            <c:catch var="errorReason">
                <mytags:addrbook user="root" password="1q2w3e4r" schema="mail" table="addrbook" />
            </c:catch>
            ${empty errorReason ? "<noerror/>" : errorReason}

        </div>
        <%@include file="footer.jspf"%>
    </body>
</html>